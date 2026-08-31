# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 7/19 (36.8%)
- **Function parity:** 64/101 matched (target 132) — 63.4%
- **Class/type parity:** 16/21 matched (target 57) — 76.2%
- **Combined symbol parity:** 80/122 matched (target 189) — 65.6%
- **Average inline-code cosine:** 0.58 (function body across 6 matched files)
- **Average documentation cosine:** 0.51 (doc text across 6 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 5 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. tokio-tungstenite.lib

- **Target:** `tokiotungstenite.Lib [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 3110.0
- **Functions:** 28/28 matched (target 55)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 25)
- **Missing types:** _none_
- **Tests:** 6/6 matched

### 2. tokio-tungstenite.compat

- **Target:** `tokiotungstenite.Compat`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 1504.1
- **Functions:** 11/11 matched (target 15)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 5)
- **Missing types:** _none_

### 3. tokio-tungstenite.handshake

- **Target:** `tokiotungstenite.Handshake`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 1206.0
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 7/7 matched (target 9)
- **Missing types:** _none_

### 4. tokio-tungstenite.proxy

- **Target:** `tokiotungstenite.Proxy`
- **Similarity:** 0.54
- **Dependents:** 0
- **Priority Score:** 704.6
- **Functions:** 7/7 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 4)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 5. tokio-tungstenite.stream

- **Target:** `tokiotungstenite.Stream`
- **Similarity:** 0.46
- **Dependents:** 0
- **Priority Score:** 605.4
- **Functions:** 5/5 matched (target 25)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 6. tokio-tungstenite.connect

- **Target:** `tokiotungstenite.Connect`
- **Similarity:** 0.75
- **Dependents:** 0
- **Priority Score:** 502.5
- **Functions:** 5/5 matched (target 12)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 7. tokio-tungstenite.tls

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

