// port-lint: source connect.rs
package io.github.kotlinmania.tokiotungstenite

/**
 * Connect to a given WebSocket URL asynchronously.
 */
public suspend fun <R> connectAsync(
    request: R,
): Result<Pair<WebSocketStream<MaybeTlsStream<AsyncStream>>, Map<String, String>>> = connectAsyncWithConfig(request, null, false)

/**
 * Connect to a given WebSocket URL with custom configuration.
 */
public suspend fun <R> connectAsyncWithConfig(
    request: R,
    config: WebSocketConfig?,
    disableNagle: Boolean,
): Result<Pair<WebSocketStream<MaybeTlsStream<AsyncStream>>, Map<String, String>>> = connect(request.toString(), config, disableNagle, null)

/**
 * Connect to a given WebSocket URL with TLS configuration and custom connector.
 */
public suspend fun <R> connectAsyncTlsWithConfig(
    request: R,
    config: WebSocketConfig?,
    disableNagle: Boolean,
    connector: Connector?,
): Result<Pair<WebSocketStream<MaybeTlsStream<AsyncStream>>, Map<String, String>>> = connect(request.toString(), config, disableNagle, connector)

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
