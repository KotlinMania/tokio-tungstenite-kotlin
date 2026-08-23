package io.github.kotlinmania.tokiotungstenite

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private fun runBlockingTest(block: suspend () -> Unit) {
    var capturedException: Throwable? = null
    block.startCoroutine(
        object : Continuation<Unit> {
            override val context: CoroutineContext = EmptyCoroutineContext

            override fun resumeWith(result: Result<Unit>) {
                capturedException = result.exceptionOrNull()
            }
        },
    )
    capturedException?.let { throw it }
}

class TokioTungsteniteTest {
    @Test
    fun testDomainExtraction() {
        assertEquals("localhost", domain("ws://localhost:8080/ws").getOrNull())
        assertEquals("example.com", domain("wss://example.com/socket").getOrNull())
        assertEquals("127.0.0.1", domain("ws://127.0.0.1:9000").getOrNull())
        assertEquals("::1", domain("ws://[::1]:8080/chat").getOrNull())
    }

    @Test
    fun testAllowStdReadWrite() {
        val stream = InMemoryAsyncStream()
        val allowStd = AllowStd.new(stream)
        val testData = "Hello WebSocket".encodeToByteArray()

        val written = allowStd.write(testData, 0, testData.size)
        assertEquals(testData.size, written)
        allowStd.flush()

        val readBuf = ByteArray(testData.size)
        val readCount = allowStd.read(readBuf, 0, readBuf.size)
        assertEquals(testData.size, readCount)
        assertEquals("Hello WebSocket", readBuf.decodeToString())
    }

    @Test
    fun testWebSocketStreamCommunication() {
        runBlockingTest {
            val stream = InMemoryAsyncStream()
            val allowStd = AllowStd.new(stream)
            val ws = WebSocketStream.new(allowStd)

            assertFalse(ws.isTerminated())

            val sendResult = ws.send(Message.text("ping message"))
            assertTrue(sendResult.isSuccess)

            ws.enqueueReceivedMessage(Message.text("pong message"))
            val received = ws.receive().getOrNull()
            assertNotNull(received)
            assertTrue(received is Message.Text)
            assertEquals("pong message", received.string)

            val closeResult = ws.close(CloseFrame(CloseCode.Normal, "bye"))
            assertTrue(closeResult.isSuccess)
            assertTrue(ws.isTerminated())
        }
    }

    @Test
    fun testClientAndServerAsyncHandshake() {
        runBlockingTest {
            val clientStream = InMemoryAsyncStream()
            val clientResult = clientAsync("ws://localhost:8080/ws", clientStream)
            assertTrue(clientResult.isSuccess)
            val (clientWs, clientHeaders) = clientResult.getOrThrow()
            assertEquals("websocket", clientHeaders["Upgrade"])
            assertNotNull(clientWs)

            val serverStream = InMemoryAsyncStream()
            val serverResult = acceptAsync(serverStream)
            assertTrue(serverResult.isSuccess)
            val serverWs = serverResult.getOrThrow()
            assertNotNull(serverWs)
        }
    }

    @Test
    fun testMaybeTlsStream() {
        val stream = InMemoryAsyncStream()
        val plain = MaybeTlsStream.Plain(stream)
        assertEquals(stream, plain.getRef())
        assertEquals(stream, plain.getMut())

        val testData = "secure data".encodeToByteArray()
        plain.write(testData, 0, testData.size)
        plain.flush()

        val readBuf = ByteArray(testData.size)
        val count = plain.read(readBuf, 0, readBuf.size)
        assertEquals(testData.size, count)
        assertEquals("secure data", readBuf.decodeToString())
    }

    @Test
    fun testHttpConnectProxyHandshake() {
        runBlockingTest {
            val stream = InMemoryAsyncStream()
            val response = "HTTP/1.1 200 Connection Established\r\n\r\n".encodeToByteArray()
            stream.write(response, 0, response.size)

            val proxy =
                ProxyConfig(
                    scheme = ProxyScheme.Http,
                    host = "proxy.local",
                    port = 3128,
                    auth = ProxyAuth("user", "pass"),
                )
            val connectResult = connectViaProxy(stream, proxy, "example.com", 443)
            assertTrue(connectResult.isSuccess)
        }
    }

    @Test
    fun testSocks5ProxyHandshake() {
        runBlockingTest {
            val stream = InMemoryAsyncStream()
            val response = byteArrayOf(0x05, 0x00, 0x05, 0x00, 0x00, 0x01, 0, 0, 0, 0, 0, 0)
            stream.write(response, 0, response.size)

            val proxy =
                ProxyConfig(
                    scheme = ProxyScheme.Socks5,
                    host = "proxy.local",
                    port = 1080,
                    auth = null,
                )
            val connectResult = connectViaProxy(stream, proxy, "example.com", 443)
            assertTrue(connectResult.isSuccess)
        }
    }

    @Test
    fun testTraitsVerification() {
        webSocketStreamHasTraits()
    }
}
