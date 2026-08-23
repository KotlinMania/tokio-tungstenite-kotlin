// port-lint: source stream.rs
package io.github.kotlinmania.tokiotungstenite

/**
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

    public abstract fun getRef(): S

    public abstract fun getMut(): S

    public fun pollRead(cx: Any?, buf: ByteArray): Int {
        if (cx != null) Unit
        return read(buf, 0, buf.size)
    }

    public fun pollWrite(cx: Any?, buf: ByteArray): Int {
        if (cx != null) Unit
        write(buf, 0, buf.size)
        return buf.size
    }

    public fun pollFlush(cx: Any?) {
        if (cx != null) Unit
        flush()
    }

    public fun pollShutdown(cx: Any?) {
        if (cx != null) Unit
        close()
    }
}
