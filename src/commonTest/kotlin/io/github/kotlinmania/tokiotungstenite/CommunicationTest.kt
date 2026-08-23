// port-lint: tests tests/communication.rs
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

class CommunicationTest {
    @Test
    fun communication() {
        runBlockingTest {
            val stream = InMemoryAsyncStream()
            val allowStd = AllowStd.new(stream)
            val ws = WebSocketStream.new(allowStd)

            assertFalse(ws.isTerminated())

            for (i in 1..9) {
                val sendResult = ws.send(Message.text("$i"))
                assertTrue(sendResult.isSuccess)
            }

            ws.close(null)
            assertTrue(ws.isTerminated())
        }
    }

    @Test
    fun splitCommunication() {
        runBlockingTest {
            val stream = InMemoryAsyncStream()
            val allowStd = AllowStd.new(stream)
            val ws = WebSocketStream.new(allowStd)

            for (i in 1..9) {
                ws.enqueueReceivedMessage(Message.text("$i"))
                val received = ws.receive().getOrNull()
                assertNotNull(received)
                assertTrue(received is Message.Text)
                assertEquals("$i", received.string)
            }
        }
    }
}
