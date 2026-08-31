// port-lint: tests tokio-tungstenite/src/lib.rs
package io.github.kotlinmania.tokiotungstenite

import kotlin.test.Test

class LibTest {
    @Test
    fun isRead() {
        io.github.kotlinmania.tokiotungstenite
            .isRead<AllowStd<AsyncStream>>()
    }

    @Test
    fun isWrite() {
        io.github.kotlinmania.tokiotungstenite
            .isWrite<AllowStd<AsyncStream>>()
    }

    @Test
    fun isAsyncRead() {
        io.github.kotlinmania.tokiotungstenite
            .isAsyncRead<MaybeTlsStream<AsyncStream>>()
    }

    @Test
    fun isAsyncWrite() {
        io.github.kotlinmania.tokiotungstenite
            .isAsyncWrite<MaybeTlsStream<AsyncStream>>()
    }

    @Test
    fun isUnpin() {
        io.github.kotlinmania.tokiotungstenite
            .isUnpin<WebSocketStream<AsyncStream>>()
    }

    @Test
    fun webSocketStreamHasTraits() {
        io.github.kotlinmania.tokiotungstenite
            .webSocketStreamHasTraits()
    }
}
