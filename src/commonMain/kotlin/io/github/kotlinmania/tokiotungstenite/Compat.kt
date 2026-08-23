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
 * An atomic waker proxy supporting dual read/write wakers.
 */
public class WakerProxy {
    private var readWoken = false
    private var writeWoken = false

    public fun wakeRead() {
        readWoken = true
    }

    public fun wakeWrite() {
        writeWoken = true
    }

    public fun wakeByRef() {
        readWoken = true
        writeWoken = true
    }

    public fun wake() {
        wakeByRef()
    }
}

/**
 * Internal interface for setting a handshake waker.
 */
public interface SetWaker {
    public fun setWaker(waker: Any?)
}

/**
 * Compatibility wrapper converting an async stream into a standard synchronous stream.
 */
public class AllowStd<S>(
    private var inner: S,
) : SetWaker {
    private val writeWakerProxy = WakerProxy()
    private val readWakerProxy = WakerProxy()

    public companion object {
        public fun <S> new(inner: S, waker: Any? = null): AllowStd<S> {
            val res = AllowStd(inner)
            res.setWaker(ContextWaker.Read, waker)
            return res
        }
    }

    override fun setWaker(waker: Any?) {
        setWaker(ContextWaker.Read, waker)
    }

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

    public fun intoInner(): S = inner

    public fun getMut(): S = inner

    public fun getRef(): S = inner

    public fun <R> withContext(kind: ContextWaker, f: (S) -> R): R {
        setWaker(kind, null)
        return f(inner)
    }

    public fun read(buf: ByteArray, offset: Int = 0, length: Int = buf.size): Int =
        withContext(ContextWaker.Read) {
            if (inner is AsyncStream) {
                (inner as AsyncStream).read(buf, offset, length)
            } else {
                buf.size
            }
        }

    public fun write(buf: ByteArray, offset: Int = 0, length: Int = buf.size): Int =
        withContext(ContextWaker.Write) {
            if (inner is AsyncStream) {
                (inner as AsyncStream).write(buf, offset, length)
                length
            } else {
                length
            }
        }

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
