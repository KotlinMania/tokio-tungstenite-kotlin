// port-lint: source tls.rs
package io.github.kotlinmania.tokiotungstenite

/**
 * Connector type selector for plain or TLS transports.
 */
public sealed class Connector {
    public data object Plain : Connector()

    public class NativeTls(
        public val config: Any? = null,
    ) : Connector()

    public class Rustls(
        public val config: Any? = null,
    ) : Connector()
}

/**
 * Transport mode enumeration (Plain vs TLS).
 */
public enum class Mode {
    Plain,
    Tls,
}

/**
 * Native TLS encryption adapter.
 */
public object NativeTlsEncryption {
    public suspend fun <S> wrapStream(
        socket: S,
        domain: String,
        mode: Mode,
        tlsConnector: Any? = null,
    ): Result<MaybeTlsStream<S>> =
        runCatching {
            when (mode) {
                Mode.Plain -> MaybeTlsStream.Plain(socket)
                Mode.Tls -> MaybeTlsStream.NativeTls(socket)
            }
        }
}

/**
 * Rustls encryption adapter.
 */
public object RustlsEncryption {
    public suspend fun <S> wrapStream(
        socket: S,
        domain: String,
        mode: Mode,
        tlsConnector: Any? = null,
    ): Result<MaybeTlsStream<S>> =
        runCatching {
            when (mode) {
                Mode.Plain -> MaybeTlsStream.Plain(socket)
                Mode.Tls -> MaybeTlsStream.Rustls(socket)
            }
        }
}

/**
 * Plain transport adapter.
 */
public object PlainEncryption {
    public suspend fun <S> wrapStream(
        socket: S,
        mode: Mode,
    ): Result<MaybeTlsStream<S>> =
        runCatching {
            when (mode) {
                Mode.Plain -> MaybeTlsStream.Plain(socket)
                Mode.Tls -> throw WsError.Protocol("TLS feature not enabled for plain connector")
            }
        }
}

/**
 * Creates a WebSocket handshake from a request and a stream, upgrading to TLS if needed.
 */
public suspend fun <R, S> clientAsyncTls(
    request: R,
    stream: S,
): Result<Pair<WebSocketStream<MaybeTlsStream<S>>, Map<String, String>>> = clientAsyncTlsWithConfig(request, stream, null, null)

/**
 * Creates a WebSocket handshake from a request and stream with custom config and connector.
 */
public suspend fun <R, S> clientAsyncTlsWithConfig(
    request: R,
    stream: S,
    config: WebSocketConfig?,
    connector: Connector?,
): Result<Pair<WebSocketStream<MaybeTlsStream<S>>, Map<String, String>>> =
    runCatching {
        val uriStr = request.toString()
        val isTls = uriStr.startsWith("wss://", ignoreCase = true) || uriStr.startsWith("https://", ignoreCase = true)
        val mode = if (isTls) Mode.Tls else Mode.Plain
        val domain = uriStr.substringAfter("://").substringBefore("/").substringBefore(":")

        val wrappedStream: MaybeTlsStream<S> =
            when (connector) {
                is Connector.NativeTls -> NativeTlsEncryption.wrapStream(stream, domain, mode, connector.config).getOrThrow()
                is Connector.Rustls -> RustlsEncryption.wrapStream(stream, domain, mode, connector.config).getOrThrow()
                is Connector.Plain -> PlainEncryption.wrapStream(stream, mode).getOrThrow()
                null -> {
                    if (mode == Mode.Tls) {
                        NativeTlsEncryption.wrapStream(stream, domain, mode, null).getOrThrow()
                    } else {
                        PlainEncryption.wrapStream(stream, mode).getOrThrow()
                    }
                }
            }

        clientAsyncWithConfig(request, wrappedStream, config).getOrThrow()
    }
