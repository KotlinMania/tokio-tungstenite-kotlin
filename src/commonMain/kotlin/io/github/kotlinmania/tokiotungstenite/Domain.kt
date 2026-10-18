package io.github.kotlinmania.tokiotungstenite

/**
 * Extract domain name from a WebSocket request URL.
 *
 * Rustls expects IPv6 addresses without the surrounding square brackets.
 */
public fun domain(request: Any): Result<String> =
    runCatching {
        val uriStr = request.toString()
        val withoutScheme = if (uriStr.contains("://")) uriStr.substringAfter("://") else uriStr
        val hostPort = withoutScheme.substringBefore("/").substringBefore("?")
        if (hostPort.startsWith("[")) {
            hostPort.substringAfter("[").substringBefore("]")
        } else {
            hostPort.substringBefore(":")
        }
    }
