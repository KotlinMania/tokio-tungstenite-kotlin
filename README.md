# tokio-tungstenite-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Ftokio--tungstenite--kotlin-blue.svg)](https://github.com/KotlinMania/tokio-tungstenite-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/tokio-tungstenite-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/tokio-tungstenite-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/tokio-tungstenite-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/tokio-tungstenite-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`snapview/tokio-tungstenite`](https://github.com/snapview/tokio-tungstenite).

**Original Project:** This port is based on [`snapview/tokio-tungstenite`](https://github.com/snapview/tokio-tungstenite). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `snapview/tokio-tungstenite`

> The text below is reproduced and lightly edited from [`https://github.com/snapview/tokio-tungstenite`](https://github.com/snapview/tokio-tungstenite). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs so they continue to resolve from this repository.

## tokio-tungstenite

Asynchronous WebSockets for Tokio stack.

[![MIT Licensed](https://img.shields.io/badge/license-MIT-blue.svg)](./LICENSE)
[![crates.io](https://img.shields.io/crates/v/tokio-tungstenite.svg?maxAge=2592000)](https://crates.io/crates/tokio-tungstenite)
[![Build Status](https://github.com/snapview/tokio-tungstenite/actions/workflows/ci.yml/badge.svg)](https://github.com/snapview/tokio-tungstenite/actions)

[Documentation](https://docs.rs/tokio-tungstenite)

## Usage

Add this in your `Cargo.toml`:

```toml
[dependencies]
tokio-tungstenite = "0.29"
```

Take a look at the `examples/` directory for client and server examples. You may also want to get familiar with
[Tokio](https://github.com/tokio-rs/tokio) if you don't have any experience with it.

## What is tokio-tungstenite?

This crate is based on [`tungstenite-rs`](https://github.com/snapview/tungstenite-rs) Rust WebSocket library and provides `Tokio` bindings and wrappers for it, so you
can use it with non-blocking/asynchronous `TcpStream`s from and couple it together with other crates from `Tokio` stack.

## Features

As with [`tungstenite-rs`](https://github.com/snapview/tungstenite-rs) TLS is supported on all platforms using [`native-tls`](https://github.com/sfackler/rust-native-tls) or [`rustls`](https://github.com/rustls/rustls) through feature flags: `native-tls`, `rustls-tls-native-roots` or `rustls-tls-webpki-roots` feature flags. Neither is enabled by default. See the `Cargo.toml` for more information. If you require support for secure WebSockets (`wss://`) enable one of them.

Note, that if you're using `rustls` features with `tokio-tungstenite` [version `0.23.0`](https://github.com/snapview/tokio-tungstenite/blob/master/CHANGELOG.md#0230) or higher,
you [might observe a panic](https://github.com/snapview/tokio-tungstenite/issues/336) that is easy to fix (see the issue). Check [the discussion](https://github.com/snapview/tokio-tungstenite/issues/353#issuecomment-2455100010)
for more details.

## Is it performant?

In essence, `tokio-tungstenite` is a wrapper for `tungstenite`, so the performance is capped by the performance of `tungstenite`. `tungstenite`
has a decent performance (it has been used in production for real-time communication software, video conferencing, etc), but it's definitely
not the fastest WebSocket library in the world at the moment of writing this note.

We are aware of changes that both `tungstenite` and `tokio-tungstenite` require in order to make it on-par with slightly more performant libraries like `fastwebsockets`. In the course of past years we have merged several performance improvements submitted by the awesome community of Rust users who helped to improve the library! The more recent versions of `tokio-tungstenite` (`> 0.26.2`) are more performant and should be more on-par with `fastwebsockets`.

For a quick summary of the pending performance problems/improvements, see [the comment](https://github.com/snapview/tungstenite-rs/issues/352#issuecomment-1537488614).

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:tokio-tungstenite-kotlin:0.1.0-SNAPSHOT")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same MIT license as the upstream [`snapview/tokio-tungstenite`](https://github.com/snapview/tokio-tungstenite). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the tokio-tungstenite authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`snapview/tokio-tungstenite`](https://github.com/snapview/tokio-tungstenite) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
