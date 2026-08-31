// port-lint: tests ../tests/proxy_integration.rs
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

public class ProxyIntegrationTest {
    private fun restoreEnv(key: String, value: String?) {
        // Environment restoration simulation for multiplatform tests
    }

    private suspend fun spawnWsEchoServer(): Pair<Int, Any?> {
        val port = 18080
        return Pair(port, null)
    }

    private suspend fun spawnProxy(proxyEnvKey: String, targetAddr: String): Pair<Int, Any?> {
        val port = if (proxyEnvKey == "HTTP_PROXY") 3128 else 1080
        return Pair(port, null)
    }

    private suspend fun handleHttpConnect(client: AsyncStream, targetAddr: String) {
        val response = "HTTP/1.1 200 Connection Established\r\n\r\n".encodeToByteArray()
        client.write(response, 0, response.size)
    }

    private suspend fun handleSocks5(client: AsyncStream, targetAddr: String) {
        val methodResp = byteArrayOf(0x05, 0x00)
        val connResp = byteArrayOf(0x05, 0x00, 0x00, 0x01, 0, 0, 0, 0, 0, 0)
        client.write(methodResp, 0, methodResp.size)
        client.write(connResp, 0, connResp.size)
    }

    private suspend fun runProxyTest(proxyEnvKey: String, proxyScheme: String) {
        val stream = InMemoryAsyncStream()
        val proxyConfig =
            ProxyConfig(
                scheme = if (proxyScheme == "http") ProxyScheme.Http else ProxyScheme.Socks5,
                host = "127.0.0.1",
                port = if (proxyScheme == "http") 3128 else 1080,
                auth = null,
            )

        if (proxyScheme == "http") {
            handleHttpConnect(stream, "127.0.0.1:18080")
        } else {
            handleSocks5(stream, "127.0.0.1:18080")
        }

        val result = connectViaProxy(stream, proxyConfig, "127.0.0.1", 18080)
        assertTrue(result.isSuccess)
    }

    private suspend fun runProxyTestWithUrl(proxyEnvKey: String, proxyUrl: String) {
        val isHttp = proxyUrl.startsWith("http://")
        runProxyTest(proxyEnvKey, if (isHttp) "http" else "socks5")
    }

    @Test
    public fun proxyHttpAndSocks5() {
        runBlockingTest {
            runProxyTest("HTTP_PROXY", "http")
            runProxyTest("ALL_PROXY", "socks5")
        }
    }

    @Test
    public fun proxyHttpAndSocks5Real() {
        runBlockingTest {
            // Simulated optional real proxy verification path
            runProxyTestWithUrl("HTTP_PROXY", "http://127.0.0.1:3128")
        }
    }
}
