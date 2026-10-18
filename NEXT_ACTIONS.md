# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 7/7 (100.0%)
- **Function parity:** 43/64 matched (target 97) — 67.2%
- **Class/type parity:** 13/16 matched (target 53) — 81.2%
- **Combined symbol parity:** 56/80 matched (target 150) — 70.0%
- **Average inline-code cosine:** 0.51 (function body across 7 matched files)
- **Average documentation cosine:** 0.50 (doc text across 7 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 5 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. lib

- **Target:** `tokiotungstenite.TungsteniteTypes`
- **Similarity:** 0.04
- **Dependents:** 0
- **Priority Score:** 243109.6
- **Functions:** 7/28 matched (target 20)
- **Missing functions:** `client_async`, `client_async_with_config`, `accept_async`, `accept_async_with_config`, `accept_hdr_async`, `accept_hdr_async_with_config`, `from_raw_socket`, `from_partially_read`, `new`, `with_context`, `into_inner`, `get_ref`, `get_mut`, `get_config`, `poll_next`, `is_terminated`, `poll_ready`, `start_send`, `poll_flush`, `poll_close`, `domain`
- **Types:** 0/3 matched (target 21)
- **Missing types:** `WebSocketStream`, `Item`, `Error`
- **Tests:** 6/6 matched

### 2. compat

- **Target:** `tokiotungstenite.Compat`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 1504.1
- **Functions:** 11/11 matched (target 15)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 5)
- **Missing types:** _none_

### 3. handshake

- **Target:** `tokiotungstenite.Handshake`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 1206.0
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 7/7 matched (target 9)
- **Missing types:** _none_

### 4. proxy

- **Target:** `tokiotungstenite.Proxy`
- **Similarity:** 0.54
- **Dependents:** 0
- **Priority Score:** 704.6
- **Functions:** 7/7 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 4)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 5. stream

- **Target:** `tokiotungstenite.Stream`
- **Similarity:** 0.46
- **Dependents:** 0
- **Priority Score:** 605.4
- **Functions:** 5/5 matched (target 25)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 6. connect

- **Target:** `tokiotungstenite.Connect`
- **Similarity:** 0.75
- **Dependents:** 0
- **Priority Score:** 502.5
- **Functions:** 5/5 matched (target 12)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 7. tls

- **Target:** `tokiotungstenite.Tls`
- **Similarity:** 0.75
- **Dependents:** 0
- **Priority Score:** 402.5
- **Functions:** 3/3 matched (target 5)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 8)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

