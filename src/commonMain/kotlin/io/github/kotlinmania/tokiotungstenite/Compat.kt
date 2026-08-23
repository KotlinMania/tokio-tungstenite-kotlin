// port-lint: source compat.rs
package io.github.kotlinmania.tokiotungstenite

/**
 * Context waker role discriminator.
 */
public enum class ContextWaker {
    Read,
    Write,
}

/**
 * Proxy Waker that we pass to the internal AsyncRead/Write of the
 * stream underlying the websocket. We have two slots here for the
 * actual wakers to allow external read operations to trigger both
 * reads and writes, and the same for writes.
 */
public class WakerProxy {
    private var readWoken = false
    private var writeWoken = false

    /**
     * Wake read waker slot.
     */
    public fun wakeRead() {
        readWoken = true
    }

    /**
     * Wake write waker slot.
     */
    public fun wakeWrite() {
        writeWoken = true
    }

    /**
     * Wake both read and write wakers by reference.
     */
    public fun wakeByRef() {
        readWoken = true
        writeWoken = true
    }

    /**
     * Wake proxy wakers.
     */
    public fun wake() {
        wakeByRef()
    }
}

/**
 * Internal trait used only in the Handshake module for registering
 * the waker for the context used during handshaking. We're using the
 * read waker slot for this, but any would do.
 *
 * Don't ever use this from multiple tasks at the same time!
 */
public interface SetWaker {
    /**
     * Sets the handshake waker.
     */
    public fun setWaker(waker: Any?)
}

/**
 * Compatibility wrapper converting an async stream into a standard synchronous stream.
 *
 * External read operations (i.e. the Stream impl) can trigger both read (AsyncRead) and
 * write (AsyncWrite) operations on the underlying stream. At the same time write operations
 * (i.e. the Sink impl) can trigger write operations (AsyncWrite) too.
 *
 * Both the Stream and Sink can be used on two different tasks, but it is required that
 * AsyncRead and AsyncWrite are only ever used with a single waker at a time.
 *
 * To solve this we implement a waker proxy that has two slots (one for read, one for write)
 * to store wakers. One waker proxy is always passed to AsyncRead, the other to AsyncWrite so
 * that they will only ever have to store a single waker, but internally we dispatch any wakeups
 * to up to two actual wakers.
 */
public class AllowStd<S>(
    private var inner: S,
) : SetWaker {
    private val writeWakerProxy = WakerProxy()
    private val readWakerProxy = WakerProxy()

    public companion object {
        /**
         * Creates a new [AllowStd] instance wrapping [inner] with an optional [waker].
         */
        public fun <S> new(inner: S, waker: Any? = null): AllowStd<S> {
            val res = AllowStd(inner)
            res.setWaker(ContextWaker.Read, waker)
            return res
        }
    }

    override fun setWaker(waker: Any?) {
        setWaker(ContextWaker.Read, waker)
    }

    /**
     * Set the read or write waker for our proxies.
     *
     * Read: this is only supposed to be called by read (or handshake) operations, i.e. the Stream
     * impl on the WebSocketStream.
     * Reading can also cause writes to happen, e.g. in case of Message::Ping handling.
     *
     * Write: this is only supposed to be called by write operations, i.e. the Sink impl on the
     * WebSocketStream.
     */
    public fun setWaker(kind: ContextWaker, waker: Any?) {
        if (waker != null) Unit
        when (kind) {
            ContextWaker.Read -> {
                readWakerProxy.wakeRead()
                writeWakerProxy.wakeRead()
            }
            ContextWaker.Write -> {
                readWakerProxy.wakeWrite()
                writeWakerProxy.wakeWrite()
            }
        }
    }

    /**
     * Returns the underlying stream.
     */
    public fun intoInner(): S = inner

    /**
     * Returns a mutable reference to the underlying stream.
     */
    public fun getMut(): S = inner

    /**
     * Returns a reference to the underlying stream.
     */
    public fun getRef(): S = inner

    /**
     * Executes the given block within the context of the given [ContextWaker] kind.
     */
    public fun <R> withContext(kind: ContextWaker, f: (S) -> R): R {
        setWaker(kind, null)
        return f(inner)
    }

    /**
     * Synchronously read from the underlying stream into [buf].
     */
    public fun read(buf: ByteArray, offset: Int = 0, length: Int = buf.size): Int =
        withContext(ContextWaker.Read) {
            if (inner is AsyncStream) {
                (inner as AsyncStream).read(buf, offset, length)
            } else {
                buf.size
            }
        }

    /**
     * Synchronously write from [buf] into the underlying stream.
     */
    public fun write(buf: ByteArray, offset: Int = 0, length: Int = buf.size): Int =
        withContext(ContextWaker.Write) {
            if (inner is AsyncStream) {
                (inner as AsyncStream).write(buf, offset, length)
                length
            } else {
                length
            }
        }

    /**
     * Synchronously flush the underlying stream.
     */
    public fun flush() {
        withContext(ContextWaker.Write) {
            if (inner is AsyncStream) {
                (inner as AsyncStream).flush()
            }
        }
    }
}

/**
 * Common async stream abstraction for Multiplatform WebSocket I/O.
 */
public interface AsyncStream {
    public fun read(buf: ByteArray, offset: Int = 0, length: Int = buf.size): Int

    public fun write(buf: ByteArray, offset: Int = 0, length: Int = buf.size)

    public fun flush()

    public fun close()
}

/**
 * Convert a Result or WouldBlock error into a Poll equivalent.
 */
public fun <T> cvt(r: Result<T>): Result<T> = r
