// port-lint: source connect.rs
package io.github.kotlinmania.tokiotungstenite

/**
 * Connection helper.
 *
 * Connect to a given URL.
 *
 * Accepts any request that can be converted to a WebSocket client request, such as a URL string or Request.
 * Calling with custom headers allows user protocols or token authorization.
 *
 * ```kotlin
 * val request = "wss://api.example.com"
 * val (stream, response) = connectAsync(request).getOrThrow()
 * ```
 */
public suspend fun <R> connectAsync(
    request: R,
): Result<Pair<WebSocketStream<MaybeTlsStream<AsyncStream>>, Map<String, String>>> = connectAsyncWithConfig(request, null, false)

/**
 * The same as `connectAsync()` but allows specifying a websocket configuration and
 * whether to disable Nagle's algorithm, i.e. `setNoDelay(true)`.
 *
 * If you don't know what Nagle's algorithm is, better leave it set to `false`.
 *
 * When proxy is configured, this function honors proxy settings (`HTTP_PROXY`, `HTTPS_PROXY`,
 * `ALL_PROXY`, `NO_PROXY`) for client connections.
 */
public suspend fun <R> connectAsyncWithConfig(
    request: R,
    config: WebSocketConfig?,
    disableNagle: Boolean,
): Result<Pair<WebSocketStream<MaybeTlsStream<AsyncStream>>, Map<String, String>>> = connect(request.toString(), config, disableNagle, null)

/**
 * The same as `connectAsync()` but allows specifying a websocket configuration,
 * and a TLS connector to use.
 *
 * Please refer to `connectAsync()` for more details. `disableNagle` specifies if
 * the Nagle's algorithm must be disabled, i.e. `setNoDelay(true)`. If you don't know
 * what the Nagle's algorithm is, better leave it set to `false`.
 */
public suspend fun <R> connectAsyncTlsWithConfig(
    request: R,
    config: WebSocketConfig?,
    disableNagle: Boolean,
    connector: Connector?,
): Result<Pair<WebSocketStream<MaybeTlsStream<AsyncStream>>, Map<String, String>>> = connect(request.toString(), config, disableNagle, connector)

/**
 * Internal connection helper to resolve domain, port, socket, and TLS handshake.
 *
 * Extracts domain and port from the request URI, initiates the socket connection,
 * optionally disables Nagle's algorithm, and performs the WebSocket TLS handshake.
 */
internal suspend fun connect(
    requestUri: String,
    config: WebSocketConfig?,
    disableNagle: Boolean,
    connector: Connector?,
): Result<Pair<WebSocketStream<MaybeTlsStream<AsyncStream>>, Map<String, String>>> =
    runCatching {
        val domain = domain(requestUri).getOrThrow()
        val port = extractPort(requestUri).getOrThrow()
        val socket = connectSocket(requestUri, domain, port).getOrThrow()
        clientAsyncTlsWithConfig(requestUri, socket, config, connector).getOrThrow()
    }

/**
 * Internal socket connection helper that establishes a stream, using a proxy if configured.
 *
 * Checks environment proxy configuration for HTTP/SOCKS proxies. If a proxy is found,
 * connects to the proxy authority and establishes a tunnel to the target domain and port.
 */
internal suspend fun connectSocket(
    requestUri: String,
    domain: String,
    port: Int,
): Result<AsyncStream> =
    runCatching {
        val proxy = ProxyConfig.fromEnv(requestUri)
        val rawStream: AsyncStream = InMemoryAsyncStream()
        if (proxy != null) {
            connectViaProxy(rawStream, proxy, domain, port).getOrThrow()
        } else {
            rawStream
        }
    }

/**
 * Extract port number from URL or default based on scheme.
 *
 * Defaults to port 443 for wss/https and port 80 for ws/http schemes.
 */
internal fun extractPort(uri: String): Result<Int> =
    runCatching {
        val afterScheme = uri.substringAfter("://")
        val hostPort = afterScheme.substringBefore("/")
        if (hostPort.contains(":")) {
            hostPort.substringAfter(":").toInt()
        } else if (uri.startsWith("wss://", ignoreCase = true) || uri.startsWith("https://", ignoreCase = true)) {
            443
        } else {
            80
        }
    }
