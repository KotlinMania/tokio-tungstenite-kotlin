# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 7/19 (36.8%)
- **Function parity:** 64/101 matched (target 109) — 63.4%
- **Class/type parity:** 16/21 matched (target 35) — 76.2%
- **Combined symbol parity:** 80/122 matched (target 144) — 65.6%
- **Average inline-code cosine:** 0.60 (function body across 7 matched files)
- **Average documentation cosine:** 0.54 (doc text across 7 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 4 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. lib

- **Target:** `tokiotungstenite.Lib [PROVENANCE-FALLBACK]`
- **Similarity:** 0.69
- **Dependents:** 0
- **Priority Score:** 3103.1
- **Functions:** 28/28 matched (target 41)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_
- **Tests:** 6/6 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Proposed provenance header:** `// port-lint: tests lib.rs` (current: `// port-lint: tests lib.rs`)
- **Lint issues:** 2

### 2. compat

- **Target:** `tokiotungstenite.Compat [PROVENANCE-FALLBACK]`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 1504.1
- **Functions:** 11/11 matched (target 15)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `compat.rs` vs expected `compat.rs`
- **Proposed provenance header:** `// port-lint: source compat.rs` (current: `// port-lint: source compat.rs`)
- **Lint issues:** 1

### 3. handshake

- **Target:** `tokiotungstenite.Handshake [PROVENANCE-FALLBACK]`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 1206.0
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 7/7 matched (target 9)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `handshake.rs` vs expected `handshake.rs`
- **Proposed provenance header:** `// port-lint: source handshake.rs` (current: `// port-lint: source handshake.rs`)
- **Lint issues:** 1

### 4. proxy

- **Target:** `tokiotungstenite.Proxy [PROVENANCE-FALLBACK]`
- **Similarity:** 0.54
- **Dependents:** 0
- **Priority Score:** 704.6
- **Functions:** 7/7 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 4)
- **Missing types:** _none_
- **Tests:** 1/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `proxy.rs` vs expected `proxy.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:proxy.rs` vs expected `proxy.rs`
- **Proposed provenance header:** `// port-lint: source proxy.rs` (current: `// port-lint: source proxy.rs`)
- **Proposed provenance header:** `// port-lint: tests proxy.rs` (current: `// port-lint: tests proxy.rs`)
- **Lint issues:** 2

### 5. stream

- **Target:** `tokiotungstenite.Stream [PROVENANCE-FALLBACK]`
- **Similarity:** 0.46
- **Dependents:** 0
- **Priority Score:** 605.4
- **Functions:** 5/5 matched (target 22)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 4)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `stream.rs` vs expected `stream.rs`
- **Proposed provenance header:** `// port-lint: source stream.rs` (current: `// port-lint: source stream.rs`)
- **Lint issues:** 1

### 6. connect

- **Target:** `tokiotungstenite.Connect [PROVENANCE-FALLBACK]`
- **Similarity:** 0.75
- **Dependents:** 0
- **Priority Score:** 502.5
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `connect.rs` vs expected `connect.rs`
- **Proposed provenance header:** `// port-lint: source connect.rs` (current: `// port-lint: source connect.rs`)
- **Lint issues:** 1

### 7. tls

- **Target:** `tokiotungstenite.Tls [PROVENANCE-FALLBACK]`
- **Similarity:** 0.75
- **Dependents:** 0
- **Priority Score:** 402.5
- **Functions:** 3/3 matched (target 5)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 8)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tls.rs` vs expected `tls.rs`
- **Proposed provenance header:** `// port-lint: source tls.rs` (current: `// port-lint: source tls.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

