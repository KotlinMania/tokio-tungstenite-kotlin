// port-lint: source tokio-tungstenite/src/handshake.rs
package io.github.kotlinmania.tokiotungstenite

public typealias Output = WebSocketStream<*>

/**
 * Container for skipped handshake execution.
 */
public class SkippedHandshakeFuture<F, S>(
    private val inner: SkippedHandshakeFutureInner<F, S>?,
) {
    public fun poll(ctx: Any?): Any? = inner
}

/**
 * Inner storage for skipped handshake closure and stream.
 */
public class SkippedHandshakeFutureInner<F, S>(
    public val f: F,
    public val stream: S,
)

/**
 * Mid-handshake state representation.
 */
public class MidHandshake<Role>(
    public val handshake: Role?,
)

/**
 * Started handshake state.
 */
public sealed class StartedHandshake<Role> {
    public class Done<Role>(
        public val result: Role,
    ) : StartedHandshake<Role>()

    public class Mid<Role>(
        public val handshake: Role,
    ) : StartedHandshake<Role>()
}

/**
 * Future representing a starting handshake operation.
 */
public class StartedHandshakeFuture<F, S>(
    private val inner: StartedHandshakeFutureInner<F, S>?,
) {
    public fun poll(ctx: Any?): Any? = inner
}

/**
 * Inner storage for starting handshake future.
 */
public class StartedHandshakeFutureInner<F, S>(
    public val f: F,
    public val stream: S,
)

/**
 * Convert a stream without performing a WebSocket handshake.
 */
public suspend fun <F, S> withoutHandshake(stream: S, f: (AllowStd<S>) -> WebSocketStream<S>): WebSocketStream<S> {
    val allowStd = AllowStd.new(stream)
    return f(allowStd)
}

/**
 * Execute a generic WebSocket handshake.
 */
public suspend fun <Role, F, S> handshake(
    stream: S,
    f: (AllowStd<S>) -> Result<Role>,
): Result<Role> {
    val allowStd = AllowStd.new(stream)
    return f(allowStd)
}

/**
 * Perform an asynchronous client WebSocket handshake.
 */
public suspend fun <F, S> clientHandshake(
    stream: S,
    f: (AllowStd<S>) -> Result<Pair<WebSocketStream<S>, Map<String, String>>>,
): Result<Pair<WebSocketStream<S>, Map<String, String>>> {
    val allowStd = AllowStd.new(stream)
    return f(allowStd)
}

/**
 * Perform an asynchronous server WebSocket handshake.
 */
public suspend fun <C, F, S> serverHandshake(
    stream: S,
    f: (AllowStd<S>) -> Result<WebSocketStream<S>>,
): Result<WebSocketStream<S>> {
    val allowStd = AllowStd.new(stream)
    return f(allowStd)
}
