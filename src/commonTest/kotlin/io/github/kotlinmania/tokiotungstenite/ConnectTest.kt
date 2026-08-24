// port-lint: tests connect.rs
package io.github.kotlinmania.tokiotungstenite

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine
import kotlin.test.Test
import kotlin.test.assertEquals
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

class ConnectTest {
    @Test
    fun testExtractPort() {
        assertEquals(80, extractPort("ws://example.com/socket").getOrNull())
        assertEquals(443, extractPort("wss://example.com/socket").getOrNull())
        assertEquals(8080, extractPort("ws://example.com:8080/").getOrNull())
        assertEquals(9443, extractPort("wss://example.com:9443/").getOrNull())
        assertEquals(80, extractPort("http://localhost/path").getOrNull())
        assertEquals(443, extractPort("https://localhost/path").getOrNull())
    }

    @Test
    fun testConnectAsyncPlain() {
        runBlockingTest {
            val res = connectAsync("ws://localhost:8080/ws")
            assertTrue(res.isSuccess)
            val (wsStream, headers) = res.getOrThrow()
            assertEquals("websocket", headers["Upgrade"])
            assertTrue(wsStream.getRef() is MaybeTlsStream.Plain)
        }
    }

    @Test
    fun testConnectAsyncWithConfig() {
        runBlockingTest {
            val config = WebSocketConfig(maxMessageSize = 65536L)
            val res = connectAsyncWithConfig("ws://localhost:8080/ws", config, disableNagle = true)
            assertTrue(res.isSuccess)
        }
    }

    @Test
    fun testConnectAsyncTlsWithConfig() {
        runBlockingTest {
            val config = WebSocketConfig(maxMessageSize = 32768L)
            val res = connectAsyncTlsWithConfig("wss://localhost:443/secure", config, disableNagle = false, connector = null)
            assertTrue(res.isSuccess)
            val (wsStream, _) = res.getOrThrow()
            assertTrue(wsStream.getRef() is MaybeTlsStream.Plain)
        }
    }
}
