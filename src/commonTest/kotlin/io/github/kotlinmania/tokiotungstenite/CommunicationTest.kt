// port-lint: tests tests/communication.rs
package io.github.kotlinmania.tokiotungstenite

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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

    private suspend fun <S> runConnection(
        connection: WebSocketStream<S>,
    ): List<Message> {
        val messages = mutableListOf<Message>()
        while (true) {
            val messageResult = connection.receive()
            if (messageResult.isFailure) break
            val message = messageResult.getOrNull() ?: break
            messages.add(message)
        }
        return messages
    }

    @Test
    fun splitCommunication() {
        runBlockingTest {
            val stream = InMemoryAsyncStream()
            val allowStd = AllowStd.new(stream)
            val ws = WebSocketStream.new(allowStd)

            for (i in 1..9) {
                ws.enqueueReceivedMessage(Message.text("$i"))
            }

            val messages = runConnection(ws)
            assertEquals(9, messages.size)
            for (i in 1..9) {
                val received = messages[i - 1]
                assertTrue(received is Message.Text)
                assertEquals("$i", received.string)
            }
        }
    }
}
