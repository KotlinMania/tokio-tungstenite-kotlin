// port-lint: tests stream.rs
package io.github.kotlinmania.tokiotungstenite

import kotlin.test.Test
import kotlin.test.assertEquals

class StreamTest {
    @Test
    fun testPlainStream() {
        val stream = InMemoryAsyncStream()
        val maybeTls: MaybeTlsStream<InMemoryAsyncStream> = MaybeTlsStream.Plain(stream)
        assertEquals(stream, maybeTls.getRef())
        assertEquals(stream, maybeTls.getMut())

        val data = "Hello Plain Stream".encodeToByteArray()
        val written = maybeTls.pollWrite(null, data)
        assertEquals(data.size, written)
        maybeTls.pollFlush(null)

        val readBuf = ByteArray(data.size)
        val readCount = maybeTls.pollRead(null, readBuf)
        assertEquals(data.size, readCount)
        assertEquals("Hello Plain Stream", readBuf.decodeToString())

        maybeTls.pollShutdown(null)
    }

    @Test
    fun testNativeTlsStream() {
        val stream = InMemoryAsyncStream()
        val maybeTls: MaybeTlsStream<InMemoryAsyncStream> = MaybeTlsStream.NativeTls(stream)
        assertEquals(stream, maybeTls.getRef())
        assertEquals(stream, maybeTls.getMut())

        val data = "Hello Native TLS".encodeToByteArray()
        val written = maybeTls.pollWrite(null, data)
        assertEquals(data.size, written)
        maybeTls.pollFlush(null)

        val readBuf = ByteArray(data.size)
        val readCount = maybeTls.pollRead(null, readBuf)
        assertEquals(data.size, readCount)
        assertEquals("Hello Native TLS", readBuf.decodeToString())

        maybeTls.pollShutdown(null)
    }

    @Test
    fun testRustlsStream() {
        val stream = InMemoryAsyncStream()
        val maybeTls: MaybeTlsStream<InMemoryAsyncStream> = MaybeTlsStream.Rustls(stream)
        assertEquals(stream, maybeTls.getRef())
        assertEquals(stream, maybeTls.getMut())

        val data = "Hello Rustls Stream".encodeToByteArray()
        val written = maybeTls.pollWrite(null, data)
        assertEquals(data.size, written)
        maybeTls.pollFlush(null)

        val readBuf = ByteArray(data.size)
        val readCount = maybeTls.pollRead(null, readBuf)
        assertEquals(data.size, readCount)
        assertEquals("Hello Rustls Stream", readBuf.decodeToString())

        maybeTls.pollShutdown(null)
    }
}
