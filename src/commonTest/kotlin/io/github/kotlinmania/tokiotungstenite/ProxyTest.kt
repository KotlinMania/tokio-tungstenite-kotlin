// port-lint: tests proxy.rs
package io.github.kotlinmania.tokiotungstenite

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine
import kotlin.test.Test
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

class ProxyTest {
    @Test
    fun httpConnectHandshake() {
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
    fun socks5Handshake() {
        runBlockingTest {
            val stream = InMemoryAsyncStream()
            // SOCKS5 response payload for method selection, auth success, and connect reply
            val methodResp = byteArrayOf(0x05, 0x02)
            val authResp = byteArrayOf(0x01, 0x00)
            val connResp = byteArrayOf(0x05, 0x00, 0x00, 0x01, 0, 0, 0, 0, 0, 0)
            stream.write(methodResp, 0, methodResp.size)
            stream.write(authResp, 0, authResp.size)
            stream.write(connResp, 0, connResp.size)

            val proxy =
                ProxyConfig(
                    scheme = ProxyScheme.Socks5,
                    host = "proxy.local",
                    port = 1080,
                    auth = ProxyAuth("user", "pass"),
                )
            val connectResult = connectViaProxy(stream, proxy, "example.com", 443)
            assertTrue(connectResult.isSuccess)
        }
    }
}
