// port-lint: source proxy.rs
package io.github.kotlinmania.tokiotungstenite

private const val MAX_CONNECT_RESPONSE_SIZE = 8192

/**
 * Proxy authentication credentials.
 */
public data class ProxyAuth(
    public val username: String,
    public val password: String,
)

/**
 * Proxy scheme enumeration.
 */
public enum class ProxyScheme {
    Http,
    Socks5,
    Socks5h,
}

/**
 * Proxy configuration container.
 */
public data class ProxyConfig(
    public val scheme: ProxyScheme,
    public val host: String,
    public val port: Int,
    public val auth: ProxyAuth? = null,
) {
    public fun authority(): String = "$host:$port"

    public companion object {
        public fun fromEnv(uri: String): ProxyConfig? = null
    }
}

/**
 * Establish a proxy tunnel over an async stream before WebSocket handshake.
 */
public suspend fun <S : AsyncStream> connectViaProxy(
    stream: S,
    proxy: ProxyConfig,
    host: String,
    port: Int,
): Result<S> =
    runCatching {
        when (proxy.scheme) {
            ProxyScheme.Http -> httpConnect(stream, host, port, proxy.auth).getOrThrow()
            ProxyScheme.Socks5, ProxyScheme.Socks5h -> socks5Handshake(stream, host, port, proxy.auth).getOrThrow()
        }
        stream
    }

/**
 * Perform HTTP CONNECT proxy negotiation.
 */
public suspend fun <S : AsyncStream> httpConnect(
    stream: S,
    host: String,
    port: Int,
    auth: ProxyAuth?,
): Result<Unit> =
    runCatching {
        val authority = "$host:$port"
        val reqBuilder = StringBuilder()
        reqBuilder.append("CONNECT $authority HTTP/1.1\r\n")
        reqBuilder.append("Host: $authority\r\n")
        if (auth != null) {
            val userPass = "${auth.username}:${auth.password}"
            val encoded = encodeBase64(userPass.encodeToByteArray())
            reqBuilder.append("Proxy-Authorization: Basic $encoded\r\n")
        }
        reqBuilder.append("\r\n")
        val reqBytes = reqBuilder.toString().encodeToByteArray()
        stream.write(reqBytes, 0, reqBytes.size)
        stream.flush()

        val response = readConnectResponse(stream).getOrThrow()
        val responseStr = response.decodeToString()
        val firstLine = responseStr.lineSequence().firstOrNull() ?: ""
        val parts = firstLine.split(" ")
        if (parts.size < 2) {
            throw WsError.Protocol("Invalid HTTP CONNECT response")
        }
        val statusCode = parts[1].toIntOrNull() ?: throw WsError.Protocol("Invalid HTTP status")
        if (statusCode !in 200..299) {
            throw WsError.Protocol("HTTP CONNECT failed with status $statusCode")
        }
    }

/**
 * Read the complete HTTP CONNECT response header from the stream.
 */
public suspend fun <S : AsyncStream> readConnectResponse(stream: S): Result<ByteArray> =
    runCatching {
        val buf = ArrayList<Byte>()
        val chunk = ByteArray(512)
        while (true) {
            if (buf.size >= MAX_CONNECT_RESPONSE_SIZE) {
                throw WsError.Protocol("HTTP CONNECT response too large")
            }
            val n = stream.read(chunk, 0, chunk.size)
            if (n <= 0) break
            for (i in 0 until n) {
                buf.add(chunk[i])
            }
            if (buf.size >= 4) {
                val sz = buf.size
                if (buf[sz - 4] == '\r'.code.toByte() &&
                    buf[sz - 3] == '\n'.code.toByte() &&
                    buf[sz - 2] == '\r'.code.toByte() &&
                    buf[sz - 1] == '\n'.code.toByte()
                ) {
                    break
                }
            }
        }
        buf.toByteArray()
    }

/**
 * Perform SOCKS5 handshake over stream.
 */
public suspend fun <S : AsyncStream> socks5Handshake(
    stream: S,
    host: String,
    port: Int,
    auth: ProxyAuth?,
): Result<Unit> =
    runCatching {
        val methods = if (auth != null) byteArrayOf(0x05, 0x02, 0x00, 0x02) else byteArrayOf(0x05, 0x01, 0x00)
        stream.write(methods, 0, methods.size)
        stream.flush()

        val choice = ByteArray(2)
        var readTotal = 0
        while (readTotal < 2) {
            val r = stream.read(choice, readTotal, 2 - readTotal)
            if (r <= 0) throw WsError.Io("SOCKS5 EOF during method selection")
            readTotal += r
        }
        if (choice[0].toInt() != 0x05) {
            throw WsError.Protocol("SOCKS5: invalid response version")
        }

        when (choice[1].toInt() and 0xFF) {
            0x00 -> Unit
            0x02 -> {
                val nonNullAuth = auth ?: throw WsError.Protocol("SOCKS5: auth required but none provided")
                socks5UserpassAuth(stream, nonNullAuth).getOrThrow()
            }
            0xFF -> throw WsError.Protocol("SOCKS5: no acceptable authentication method")
            else -> throw WsError.Protocol("SOCKS5: unsupported authentication method")
        }

        sendSocks5Connect(stream, host, port).getOrThrow()
    }

/**
 * Perform SOCKS5 username/password authentication.
 */
public suspend fun <S : AsyncStream> socks5UserpassAuth(
    stream: S,
    auth: ProxyAuth,
): Result<Unit> =
    runCatching {
        val userBytes = auth.username.encodeToByteArray()
        val passBytes = auth.password.encodeToByteArray()
        if (userBytes.size > 255 || passBytes.size > 255) {
            throw WsError.Protocol("SOCKS5 auth credentials too long")
        }
        val buf = ByteArray(3 + userBytes.size + passBytes.size)
        buf[0] = 0x01
        buf[1] = userBytes.size.toByte()
        userBytes.copyInto(buf, 2)
        buf[2 + userBytes.size] = passBytes.size.toByte()
        passBytes.copyInto(buf, 3 + userBytes.size)

        stream.write(buf, 0, buf.size)
        stream.flush()

        val resp = ByteArray(2)
        var readTotal = 0
        while (readTotal < 2) {
            val r = stream.read(resp, readTotal, 2 - readTotal)
            if (r <= 0) throw WsError.Io("SOCKS5 EOF during auth response")
            readTotal += r
        }
        if (resp[0].toInt() != 0x01 || resp[1].toInt() != 0x00) {
            throw WsError.Protocol("SOCKS5 authentication failed")
        }
    }

/**
 * Send SOCKS5 connect request.
 */
public suspend fun <S : AsyncStream> sendSocks5Connect(
    stream: S,
    host: String,
    port: Int,
): Result<Unit> =
    runCatching {
        val hostBytes = host.encodeToByteArray()
        if (hostBytes.size > 255) {
            throw WsError.Protocol("SOCKS5 domain name too long")
        }
        val req = ByteArray(4 + 1 + hostBytes.size + 2)
        req[0] = 0x05
        req[1] = 0x01
        req[2] = 0x00
        req[3] = 0x03 // Domain name
        req[4] = hostBytes.size.toByte()
        hostBytes.copyInto(req, 5)
        req[5 + hostBytes.size] = ((port shr 8) and 0xFF).toByte()
        req[6 + hostBytes.size] = (port and 0xFF).toByte()

        stream.write(req, 0, req.size)
        stream.flush()

        val header = ByteArray(4)
        var readTotal = 0
        while (readTotal < 4) {
            val r = stream.read(header, readTotal, 4 - readTotal)
            if (r <= 0) throw WsError.Io("SOCKS5 EOF during connect response")
            readTotal += r
        }
        if (header[0].toInt() != 0x05) {
            throw WsError.Protocol("SOCKS5: invalid response version")
        }
        if (header[1].toInt() != 0x00) {
            throw WsError.Protocol("SOCKS5: connection failed with code ${header[1]}")
        }

        val addrLen =
            when (header[3].toInt() and 0xFF) {
                0x01 -> 4
                0x03 -> {
                    val lenBuf = ByteArray(1)
                    stream.read(lenBuf, 0, 1)
                    lenBuf[0].toInt() and 0xFF
                }
                0x04 -> 16
                else -> throw WsError.Protocol("SOCKS5: invalid address type")
            }

        val discard = ByteArray(addrLen + 2)
        var discardTotal = 0
        while (discardTotal < discard.size) {
            val r = stream.read(discard, discardTotal, discard.size - discardTotal)
            if (r <= 0) break
            discardTotal += r
        }
    }

public suspend fun httpConnectHandshake() {
    val stream = InMemoryAsyncStream()
    val proxy = ProxyConfig(ProxyScheme.Http, "proxy.local", 3128, ProxyAuth("user", "pass"))
    val authBytes = "HTTP/1.1 200 Connection Established\r\n\r\n".encodeToByteArray()
    stream.write(authBytes, 0, authBytes.size)
    connectViaProxy(stream, proxy, "example.com", 443)
}

private fun encodeBase64(src: ByteArray): String {
    val table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
    val sb = StringBuilder((src.size * 4 + 2) / 3)
    var i = 0
    while (i < src.size) {
        val b0 = src[i++].toInt() and 0xFF
        val b1 = if (i < src.size) src[i++].toInt() and 0xFF else -1
        val b2 = if (i < src.size) src[i++].toInt() and 0xFF else -1

        sb.append(table[b0 shr 2])
        if (b1 != -1) {
            sb.append(table[((b0 and 0x03) shl 4) or (b1 shr 4)])
            if (b2 != -1) {
                sb.append(table[((b1 and 0x0F) shl 2) or (b2 shr 6)])
                sb.append(table[b2 and 0x3F])
            } else {
                sb.append(table[(b1 and 0x0F) shl 2])
                sb.append('=')
            }
        } else {
            sb.append(table[(b0 and 0x03) shl 4])
            sb.append("==")
        }
    }
    return sb.toString()
}
