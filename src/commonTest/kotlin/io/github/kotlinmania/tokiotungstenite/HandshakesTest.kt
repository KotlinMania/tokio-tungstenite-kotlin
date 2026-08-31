// port-lint: tests tokio-tungstenite/tests/handshakes.rs
package io.github.kotlinmania.tokiotungstenite

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine
import kotlin.test.Test
import kotlin.test.assertEquals
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

class HandshakesTest {
    @Test
    fun handshakes() {
        runBlockingTest {
            val serverStream = InMemoryAsyncStream()
            val serverResult = acceptAsync(serverStream)
            assertTrue(serverResult.isSuccess)
            val serverWs = serverResult.getOrThrow()
            assertNotNull(serverWs)

            val clientStream = InMemoryAsyncStream()
            val clientResult = clientAsync("ws://localhost:12345/", clientStream)
            assertTrue(clientResult.isSuccess)
            val (clientWs, headers) = clientResult.getOrThrow()
            assertNotNull(clientWs)
            assertEquals("websocket", headers["Upgrade"])
        }
    }
}
