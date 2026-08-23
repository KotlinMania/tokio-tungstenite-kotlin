# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 7/7 (100.0%)
- **Function parity:** 57/64 matched (target 99) — 89.1%
- **Class/type parity:** 16/16 matched (target 33) — 100.0%
- **Combined symbol parity:** 73/80 matched (target 132) — 91.2%
- **Average inline-code cosine:** 0.57 (function body across 7 matched files)
- **Average documentation cosine:** 0.44 (doc text across 7 matched files)
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

- **Target:** `tokiotungstenite.Lib`
- **Similarity:** 0.56
- **Dependents:** 0
- **Priority Score:** 63104.4
- **Functions:** 22/28 matched (target 35)
- **Missing functions:** `is_read`, `is_write`, `is_async_read`, `is_async_write`, `is_unpin`, `web_socket_stream_has_traits`
- **Types:** 3/3 matched (target 4)
- **Missing types:** _none_
- **Tests:** 0/6 matched

### 2. proxy

- **Target:** `tokiotungstenite.Proxy`
- **Similarity:** 0.49
- **Dependents:** 0
- **Priority Score:** 10705.1
- **Functions:** 6/7 matched (target 10)
- **Missing functions:** `http_connect_handshake`
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 3. compat

- **Target:** `tokiotungstenite.Compat`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 1504.1
- **Functions:** 11/11 matched (target 15)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 5)
- **Missing types:** _none_

### 4. handshake

- **Target:** `tokiotungstenite.Handshake`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 1206.0
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 7/7 matched (target 9)
- **Missing types:** _none_

### 5. stream

- **Target:** `tokiotungstenite.Stream`
- **Similarity:** 0.46
- **Dependents:** 0
- **Priority Score:** 605.4
- **Functions:** 5/5 matched (target 22)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 4)
- **Missing types:** _none_

### 6. connect

- **Target:** `tokiotungstenite.Connect`
- **Similarity:** 0.75
- **Dependents:** 0
- **Priority Score:** 502.5
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched
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

