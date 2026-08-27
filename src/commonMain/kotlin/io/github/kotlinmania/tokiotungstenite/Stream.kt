// port-lint: source tokio-tungstenite/src/stream.rs
package io.github.kotlinmania.tokiotungstenite

/**
 * Convenience wrapper for streams to switch between plain TCP and TLS at runtime.
 *
 * There is no dependency on actual TLS implementations. Everything like
 * `NativeTls` or `OpenSsl` will work as long as there is a TLS stream supporting standard
 * Read and Write operations.
 *
 * A stream that might be protected with TLS.
 */
public sealed class MaybeTlsStream<S> : AsyncStream {
    /**
     * Unencrypted socket stream.
     */
    public class Plain<S>(
        public var stream: S,
    ) : MaybeTlsStream<S>() {
        override fun getRef(): S = stream

        override fun getMut(): S = stream

        override fun read(buf: ByteArray, offset: Int, length: Int): Int =
            if (stream is AsyncStream) (stream as AsyncStream).read(buf, offset, length) else length

        override fun write(buf: ByteArray, offset: Int, length: Int) {
            if (stream is AsyncStream) (stream as AsyncStream).write(buf, offset, length)
        }

        override fun flush() {
            if (stream is AsyncStream) (stream as AsyncStream).flush()
        }

        override fun close() {
            if (stream is AsyncStream) (stream as AsyncStream).close()
        }
    }

    /**
     * Encrypted socket stream using platform native-tls.
     */
    public class NativeTls<S>(
        public var stream: S,
    ) : MaybeTlsStream<S>() {
        override fun getRef(): S = stream

        override fun getMut(): S = stream

        override fun read(buf: ByteArray, offset: Int, length: Int): Int =
            if (stream is AsyncStream) (stream as AsyncStream).read(buf, offset, length) else length

        override fun write(buf: ByteArray, offset: Int, length: Int) {
            if (stream is AsyncStream) (stream as AsyncStream).write(buf, offset, length)
        }

        override fun flush() {
            if (stream is AsyncStream) (stream as AsyncStream).flush()
        }

        override fun close() {
            if (stream is AsyncStream) (stream as AsyncStream).close()
        }
    }

    /**
     * Encrypted socket stream using rustls / pure TLS engine.
     */
    public class Rustls<S>(
        public var stream: S,
    ) : MaybeTlsStream<S>() {
        override fun getRef(): S = stream

        override fun getMut(): S = stream

        override fun read(buf: ByteArray, offset: Int, length: Int): Int =
            if (stream is AsyncStream) (stream as AsyncStream).read(buf, offset, length) else length

        override fun write(buf: ByteArray, offset: Int, length: Int) {
            if (stream is AsyncStream) (stream as AsyncStream).write(buf, offset, length)
        }

        override fun flush() {
            if (stream is AsyncStream) (stream as AsyncStream).flush()
        }

        override fun close() {
            if (stream is AsyncStream) (stream as AsyncStream).close()
        }
    }

    /**
     * Returns a shared reference to the inner stream.
     */
    public abstract fun getRef(): S

    /**
     * Returns a mutable reference to the inner stream.
     */
    public abstract fun getMut(): S

    /**
     * Poll read bytes from the underlying stream into buffer.
     */
    public fun pollRead(cx: Any?, buf: ByteArray): Int {
        if (cx != null) Unit
        return read(buf, 0, buf.size)
    }

    /**
     * Poll write bytes from buffer to the underlying stream.
     */
    public fun pollWrite(cx: Any?, buf: ByteArray): Int {
        if (cx != null) Unit
        write(buf, 0, buf.size)
        return buf.size
    }

    /**
     * Poll flush pending writes to the stream.
     */
    public fun pollFlush(cx: Any?) {
        if (cx != null) Unit
        flush()
    }

    /**
     * Poll shutdown / close the stream.
     */
    public fun pollShutdown(cx: Any?) {
        if (cx != null) Unit
        close()
    }
}
