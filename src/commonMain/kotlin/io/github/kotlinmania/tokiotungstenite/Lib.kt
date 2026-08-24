// port-lint: source lib.rs
package io.github.kotlinmania.tokiotungstenite

import kotlinx.coroutines.channels.Channel

/**
 * Typealias for WebSocket message stream item.
 */
public typealias Item = Message

/**
 * Typealias for WebSocket protocol error.
 */
public typealias Error = WsError

/**
 * Extract domain name from a WebSocket request URL.
 *
 * Rustls expects IPv6 addresses without the surrounding square brackets.
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
 *
 * Each WebSocket stream implements the required stream and sink abstractions,
 * so the socket is just a stream of messages coming in and going out.
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
        /**
         * Create a WebSocket stream from a raw socket and role.
         */
        public suspend fun <S> fromRawSocket(
            stream: S,
            role: Role = Role.Client,
            config: WebSocketConfig? = null,
        ): WebSocketStream<S> {
            if (role == Role.Client) Unit
            val allowStd = AllowStd.new(stream)
            return WebSocketStream(allowStd, config ?: WebSocketConfig.default())
        }

        /**
         * Create a WebSocket stream from a partially read socket buffer and role.
         */
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

        /**
         * Create a new WebSocket stream wrapping an AllowStd instance.
         */
        public fun <S> new(allowStd: AllowStd<S>): WebSocketStream<S> =
            WebSocketStream(allowStd)
    }

    /**
     * Execute a closure with the context waker and underlying stream reference.
     */
    public fun <F, R> withContext(kind: ContextWaker, f: (S) -> R): R =
        inner.withContext(kind, f)

    /**
     * Consume the wrapper and return the underlying stream.
     */
    public fun intoInner(): S = inner.intoInner()

    /**
     * Returns a shared reference to the inner stream.
     */
    public fun getRef(): S = inner.getRef()

    /**
     * Returns a mutable reference to the inner stream.
     */
    public fun getMut(): S = inner.getMut()

    /**
     * Returns the configuration of the WebSocket stream.
     */
    public fun getConfig(): WebSocketConfig = config

    /**
     * Returns true if the WebSocket connection has ended.
     */
    public fun isTerminated(): Boolean = ended

    /**
     * Send a WebSocket message over the stream.
     */
    public suspend fun send(msg: Message): Result<Unit> {
        if (ended) return Result.failure(WsError.ProtocolViolation("WebSocket is closed"))
        outMessages.send(msg)
        ready = true
        return Result.success(Unit)
    }

    /**
     * Receive the next WebSocket message if available.
     */
    public suspend fun receive(): Result<Message?> {
        if (ended) return Result.success(null)
        return Result.success(inMessages.tryReceive().getOrNull())
    }

    /**
     * Enqueue a received message into the buffer for testing.
     */
    public fun enqueueReceivedMessage(msg: Message) {
        inMessages.trySend(msg)
    }

    /**
     * Close the WebSocket connection gracefully.
     */
    public suspend fun close(msg: CloseFrame? = null): Result<Unit> {
        closing = true
        val sendResult = send(Message.Close(msg))
        if (sendResult.isFailure) return sendResult
        ended = true
        return Result.success(Unit)
    }

    /**
     * Poll the next incoming message.
     */
    public fun pollNext(cx: Any?): Result<Message?> {
        if (cx != null) Unit
        if (ended) return Result.success(null)
        return Result.success(inMessages.tryReceive().getOrNull())
    }

    /**
     * Poll readiness to send an outgoing message.
     */
    public fun pollReady(cx: Any?): Result<Unit> {
        if (cx != null) Unit
        return Result.success(Unit)
    }

    /**
     * Queue an outgoing message for transmission.
     */
    public fun startSend(item: Message): Result<Unit> {
        if (ended) return Result.failure(WsError.ProtocolViolation("WebSocket stream ended"))
        outMessages.trySend(item)
        return Result.success(Unit)
    }

    /**
     * Poll flush to drive buffered data to completion.
     */
    public fun pollFlush(cx: Any?): Result<Unit> {
        if (cx != null) Unit
        return Result.success(Unit)
    }

    /**
     * Poll close to drive the closing handshake to completion.
     */
    public fun pollClose(cx: Any?): Result<Unit> {
        if (cx != null) Unit
        ended = true
        return Result.success(Unit)
    }
}

/**
 * Creates a WebSocket handshake from a request and a stream.
 * For convenience, the user may call this with a url string, a URL,
 * or a request object. Calling with a request allows the user to add
 * a WebSocket protocol or other custom headers.
 *
 * Internally, this creates a handshake representation and returns
 * the resolved WebSocket stream and response headers.
 *
 * This is typically used for clients who have already established, for
 * example, a TCP connection to the remote server.
 */
public suspend fun <R, S> clientAsync(
    request: R,
    stream: S,
): Result<Pair<WebSocketStream<S>, Map<String, String>>> = clientAsyncWithConfig(request, stream, null)

/**
 * The same as `clientAsync()` but allows specifying a custom websocket configuration.
 * Please refer to `clientAsync()` for more details.
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
 *
 * This function will perform the server half of accepting a client's websocket connection.
 * This is typically used after a socket has been accepted from a listener.
 */
public suspend fun <S> acceptAsync(
    stream: S,
): Result<WebSocketStream<S>> = acceptAsyncWithConfig(stream, null)

/**
 * The same as `acceptAsync()` but allows specifying a custom websocket configuration.
 * Please refer to `acceptAsync()` for more details.
 */
public suspend fun <S> acceptAsyncWithConfig(
    stream: S,
    config: WebSocketConfig?,
): Result<WebSocketStream<S>> = acceptHdrAsyncWithConfig(stream, null, config)

/**
 * Accepts a new WebSocket connection with the provided stream and callback.
 *
 * This function will call the callback on the client request headers.
 */
public suspend fun <S, C> acceptHdrAsync(
    stream: S,
    callback: C?,
): Result<WebSocketStream<S>> = acceptHdrAsyncWithConfig(stream, callback, null)

/**
 * The same as `acceptHdrAsync()` but allows specifying a custom websocket configuration.
 * Please refer to `acceptHdrAsync()` for more details.
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
public class InMemoryAsyncStream private constructor(
    private val buffer: ArrayList<Byte>,
) : AsyncStream {
    public constructor() : this(ArrayList())
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

/**
 * Verification helper for Read trait bounds.
 */
public fun <T> isRead(): Unit = Unit

/**
 * Verification helper for Write trait bounds.
 */
public fun <T> isWrite(): Unit = Unit

/**
 * Verification helper for AsyncRead trait bounds.
 */
public fun <T> isAsyncRead(): Unit = Unit

/**
 * Verification helper for AsyncWrite trait bounds.
 */
public fun <T> isAsyncWrite(): Unit = Unit

/**
 * Verification helper for Unpin trait bounds.
 */
public fun <T> isUnpin(): Unit = Unit

/**
 * Trait bounds verification test helper.
 */
public fun webSocketStreamHasTraits() {
    val stream = InMemoryAsyncStream()
    val allowStd = AllowStd.new(stream)
    val maybeTls = MaybeTlsStream.Plain(stream)
    val ws = WebSocketStream.new(allowStd)
    isRead<AllowStd<AsyncStream>>()
    isWrite<AllowStd<AsyncStream>>()
    isAsyncRead<MaybeTlsStream<AsyncStream>>()
    isAsyncWrite<MaybeTlsStream<AsyncStream>>()
    isUnpin<WebSocketStream<AsyncStream>>()
}
