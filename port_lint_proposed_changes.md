# port-lint Proposed Changes

**Generated:** 2026-08-23
**Source:** tmp/tokio-tungstenite
**Target:** src/commonMain/kotlin

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonMain/kotlin/io/github/kotlinmania/tokiotungstenite/Lib.kt` | `// port-lint: source lib.rs` | `// port-lint: source lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'lib.rs' vs expected 'lib.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/tokiotungstenite/LibTest.kt` | `// port-lint: tests lib.rs` | `// port-lint: tests lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:lib.rs' vs expected 'lib.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/tokiotungstenite/Compat.kt` | `// port-lint: source compat.rs` | `// port-lint: source compat.rs` | `compat.rs` | `port-lint provenance header matched only after fallback normalization: 'compat.rs' vs expected 'compat.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/tokiotungstenite/Handshake.kt` | `// port-lint: source handshake.rs` | `// port-lint: source handshake.rs` | `handshake.rs` | `port-lint provenance header matched only after fallback normalization: 'handshake.rs' vs expected 'handshake.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/tokiotungstenite/Proxy.kt` | `// port-lint: source proxy.rs` | `// port-lint: source proxy.rs` | `proxy.rs` | `port-lint provenance header matched only after fallback normalization: 'proxy.rs' vs expected 'proxy.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/tokiotungstenite/ProxyTest.kt` | `// port-lint: tests proxy.rs` | `// port-lint: tests proxy.rs` | `proxy.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:proxy.rs' vs expected 'proxy.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/tokiotungstenite/Stream.kt` | `// port-lint: source stream.rs` | `// port-lint: source stream.rs` | `stream.rs` | `port-lint provenance header matched only after fallback normalization: 'stream.rs' vs expected 'stream.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/tokiotungstenite/Connect.kt` | `// port-lint: source connect.rs` | `// port-lint: source connect.rs` | `connect.rs` | `port-lint provenance header matched only after fallback normalization: 'connect.rs' vs expected 'connect.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/tokiotungstenite/Tls.kt` | `// port-lint: source tls.rs` | `// port-lint: source tls.rs` | `tls.rs` | `port-lint provenance header matched only after fallback normalization: 'tls.rs' vs expected 'tls.rs'` |
