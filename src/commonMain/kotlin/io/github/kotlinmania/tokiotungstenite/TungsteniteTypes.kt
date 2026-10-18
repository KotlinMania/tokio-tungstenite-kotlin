// port-lint: source lib.rs
package io.github.kotlinmania.tokiotungstenite

/**
 * WebSocket error hierarchy.
 */
public sealed class WsError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    public class ConnectionClosed : WsError("Connection closed normally")

    public class AlreadyClosed : WsError("WebSocket already closed")

    public class Io(
        message: String,
    ) : WsError("I/O error: $message")

    public class ProtocolViolation(
        message: String,
    ) : WsError("Protocol violation: $message")

    public class Url(
        message: String,
    ) : WsError("URL error: $message")

    public class Http(
        public val statusCode: Int,
    ) : WsError("HTTP error status $statusCode")

    public class Capacity(
        message: String,
    ) : WsError("Capacity exceeded: $message")

    public class Tls(
        message: String,
    ) : WsError("TLS error: $message")
}

/**
 * WebSocket role (client or server).
 */
public enum class Role {
    Client,
    Server,
}

/**
 * Close frame status code.
 */
public enum class CloseCode(
    public val code: Short,
) {
    Normal(1000),
    Away(1001),
    ProtocolError(1002),
    Unsupported(1003),
    Status(1005),
    Abnormal(1006),
    Invalid(1007),
    Policy(1008),
    Size(1009),
    Extension(1010),
    Error(1011),
    Restart(1012),
    Again(1013),
    Tls(1015),
}

/**
 * WebSocket close frame payload.
 */
public data class CloseFrame(
    public val code: CloseCode = CloseCode.Normal,
    public val reason: String = "",
)

/**
 * WebSocket message envelope.
 */
public sealed class Message {
    public data class Text(
        public val string: String,
    ) : Message()

    public data class Binary(
        public val data: ByteArray,
    ) : Message() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Binary) return false
            return data.contentEquals(other.data)
        }

        override fun hashCode(): Int = data.contentHashCode()
    }

    public data class Ping(
        public val data: ByteArray,
    ) : Message() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Ping) return false
            return data.contentEquals(other.data)
        }

        override fun hashCode(): Int = data.contentHashCode()
    }

    public data class Pong(
        public val data: ByteArray,
    ) : Message() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Pong) return false
            return data.contentEquals(other.data)
        }

        override fun hashCode(): Int = data.contentHashCode()
    }

    public data class Close(
        public val frame: CloseFrame? = null,
    ) : Message()

    public data class Frame(
        public val payload: ByteArray,
    ) : Message() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Frame) return false
            return payload.contentEquals(other.payload)
        }

        override fun hashCode(): Int = payload.contentHashCode()
    }

    public companion object {
        public fun text(str: String): Message = Text(str)

        public fun binary(data: ByteArray): Message = Binary(data)

        public fun ping(data: ByteArray): Message = Ping(data)

        public fun pong(data: ByteArray): Message = Pong(data)

        public fun close(frame: CloseFrame? = null): Message = Close(frame)
    }
}

/**
 * WebSocket configuration settings.
 */
public data class WebSocketConfig(
    public val writeBufferSize: Int = 128 * 1024,
    public val maxWriteBufferSize: Int = 10 * 1024 * 1024,
    public val maxMessageSize: Long? = 64 * 1024 * 1024L,
    public val maxFrameSize: Long? = 16 * 1024 * 1024L,
    public val acceptUnmaskedFrames: Boolean = false,
) {
    public companion object {
        public fun default(): WebSocketConfig = WebSocketConfig()
    }
}
