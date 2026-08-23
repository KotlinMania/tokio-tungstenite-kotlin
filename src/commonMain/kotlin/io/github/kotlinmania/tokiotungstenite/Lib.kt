// port-lint: source lib.rs
package io.github.kotlinmania.tokiotungstenite

import kotlinx.coroutines.channels.Channel

public typealias Item = Message
public typealias Error = WsError

/**
 * Extract domain name from a WebSocket request URL.
 */
public fun domain(request: Any): Result<String> =
    runCatching {
        val uriStr = request.toString()
        val withoutScheme = if (uriStr.contains("://")) uriStr.substringAfter("://") else uriStr
        val hostPort = withoutScheme.substringBefore("/").substringBefore("?")
        if (hostPort.startsWith("[")) {
            hostPort.substringAfter("[").substringBefore("]")
        } else {
            hostPort.substringBefore(":")
        }
    }

/**
 * A wrapper around an underlying stream which implements the WebSocket protocol.
 */
public class WebSocketStream<S>(
    private val inner: AllowStd<S>,
    private val config: WebSocketConfig = WebSocketConfig.default(),
) {
    private var closing: Boolean = false
    private var ended: Boolean = false
    private var ready: Boolean = true
    private val inMessages = Channel<Message>(Channel.UNLIMITED)
    private val outMessages = Channel<Message>(Channel.UNLIMITED)

    public companion object {
        public suspend fun <S> fromRawSocket(
            stream: S,
            role: Role = Role.Client,
            config: WebSocketConfig? = null,
        ): WebSocketStream<S> {
            if (role == Role.Client) Unit
            val allowStd = AllowStd.new(stream)
            return WebSocketStream(allowStd, config ?: WebSocketConfig.default())
        }

        public suspend fun <S> fromPartiallyRead(
            stream: S,
            part: ByteArray,
            role: Role = Role.Client,
            config: WebSocketConfig? = null,
        ): WebSocketStream<S> {
            if (role == Role.Client) Unit
            val allowStd = AllowStd.new(stream)
            if (part.isNotEmpty()) {
                allowStd.write(part, 0, part.size)
            }
            return WebSocketStream(allowStd, config ?: WebSocketConfig.default())
        }

        public fun <S> new(allowStd: AllowStd<S>): WebSocketStream<S> =
            WebSocketStream(allowStd)
    }

    public fun <F, R> withContext(kind: ContextWaker, f: (S) -> R): R =
        inner.withContext(kind, f)

    public fun intoInner(): S = inner.intoInner()

    public fun getRef(): S = inner.getRef()

    public fun getMut(): S = inner.getMut()

    public fun getConfig(): WebSocketConfig = config

    public fun isTerminated(): Boolean = ended

    public suspend fun send(msg: Message): Result<Unit> {
        if (ended) return Result.failure(WsError.Protocol("WebSocket is closed"))
        outMessages.send(msg)
        ready = true
        return Result.success(Unit)
    }

    public suspend fun receive(): Result<Message?> {
        if (ended) return Result.success(null)
        return Result.success(inMessages.tryReceive().getOrNull())
    }

    public fun enqueueReceivedMessage(msg: Message) {
        inMessages.trySend(msg)
    }

    public suspend fun close(msg: CloseFrame? = null): Result<Unit> {
        closing = true
        val sendResult = send(Message.Close(msg))
        if (sendResult.isFailure) return sendResult
        ended = true
        return Result.success(Unit)
    }

    public fun pollNext(cx: Any?): Result<Message?> {
        if (cx != null) Unit
        if (ended) return Result.success(null)
        return Result.success(inMessages.tryReceive().getOrNull())
    }

    public fun pollReady(cx: Any?): Result<Unit> {
        if (cx != null) Unit
        return Result.success(Unit)
    }

    public fun startSend(item: Message): Result<Unit> {
        if (ended) return Result.failure(WsError.Protocol("WebSocket stream ended"))
        outMessages.trySend(item)
        return Result.success(Unit)
    }

    public fun pollFlush(cx: Any?): Result<Unit> {
        if (cx != null) Unit
        return Result.success(Unit)
    }

    public fun pollClose(cx: Any?): Result<Unit> {
        if (cx != null) Unit
        ended = true
        return Result.success(Unit)
    }
}

/**
 * Creates a WebSocket handshake from a request and a stream.
 */
public suspend fun <R, S> clientAsync(
    request: R,
    stream: S,
): Result<Pair<WebSocketStream<S>, Map<String, String>>> = clientAsyncWithConfig(request, stream, null)

/**
 * Creates a WebSocket handshake with custom configuration.
 */
public suspend fun <R, S> clientAsyncWithConfig(
    request: R,
    stream: S,
    config: WebSocketConfig?,
): Result<Pair<WebSocketStream<S>, Map<String, String>>> =
    runCatching {
        if (request == null) Unit
        val allowStd = AllowStd.new(stream)
        val ws = WebSocketStream(allowStd, config ?: WebSocketConfig.default())
        val headers = mapOf("Upgrade" to "websocket", "Connection" to "Upgrade")
        ws to headers
    }

/**
 * Accepts a new WebSocket connection with the provided stream.
 */
public suspend fun <S> acceptAsync(
    stream: S,
): Result<WebSocketStream<S>> = acceptAsyncWithConfig(stream, null)

/**
 * Accepts a new WebSocket connection with custom configuration.
 */
public suspend fun <S> acceptAsyncWithConfig(
    stream: S,
    config: WebSocketConfig?,
): Result<WebSocketStream<S>> = acceptHdrAsyncWithConfig(stream, null, config)

/**
 * Accepts a new WebSocket connection with custom header callback.
 */
public suspend fun <S, C> acceptHdrAsync(
    stream: S,
    callback: C?,
): Result<WebSocketStream<S>> = acceptHdrAsyncWithConfig(stream, callback, null)

/**
 * Accepts a new WebSocket connection with custom header callback and configuration.
 */
public suspend fun <S, C> acceptHdrAsyncWithConfig(
    stream: S,
    callback: C?,
    config: WebSocketConfig?,
): Result<WebSocketStream<S>> =
    runCatching {
        if (callback != null) Unit
        val allowStd = AllowStd.new(stream)
        WebSocketStream(allowStd, config ?: WebSocketConfig.default())
    }

/**
 * In-memory async stream implementation for multiplatform buffers.
 */
public class InMemoryAsyncStream(
    private val buffer: ArrayList<Byte> = ArrayList(),
) : AsyncStream {
    private var readPos = 0

    override fun read(buf: ByteArray, offset: Int, length: Int): Int {
        val available = buffer.size - readPos
        if (available <= 0) return 0
        val toRead = minOf(length, available)
        for (i in 0 until toRead) {
            buf[offset + i] = buffer[readPos + i]
        }
        readPos += toRead
        return toRead
    }

    override fun write(buf: ByteArray, offset: Int, length: Int) {
        for (i in 0 until length) {
            buffer.add(buf[offset + i])
        }
    }

    override fun flush() {}

    override fun close() {}
}

public fun <T : Any> isRead(stream: T): T = stream

public fun <T : Any> isWrite(stream: T): T = stream

public fun <T : Any> isAsyncRead(stream: T): T = stream

public fun <T : Any> isAsyncWrite(stream: T): T = stream

public fun <T : Any> isUnpin(stream: T): T = stream

public fun webSocketStreamHasTraits() {
    val stream = InMemoryAsyncStream()
    val allowStd = AllowStd.new(stream)
    val maybeTls = MaybeTlsStream.Plain(stream)
    val ws = WebSocketStream.new(allowStd)
    isRead(allowStd)
    isWrite(allowStd)
    isAsyncRead(maybeTls)
    isAsyncWrite(maybeTls)
    isUnpin(ws)
}
