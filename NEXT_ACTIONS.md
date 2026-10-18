# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 6/7 (85.7%)
- **Function parity:** 36/36 matched (target 74) — 100.0%
- **Class/type parity:** 13/13 matched (target 29) — 100.0%
- **Combined symbol parity:** 49/49 matched (target 103) — 100.0%
- **Average inline-code cosine:** 0.59 (function body across 6 matched files)
- **Average documentation cosine:** 0.50 (doc text across 6 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 3 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. compat

- **Target:** `tokiotungstenite.Compat`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 1504.1
- **Functions:** 11/11 matched (target 15)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 5)
- **Missing types:** _none_

### 2. handshake

- **Target:** `tokiotungstenite.Handshake`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 1206.0
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 7/7 matched (target 9)
- **Missing types:** _none_

### 3. proxy

- **Target:** `tokiotungstenite.Proxy`
- **Similarity:** 0.61
- **Dependents:** 0
- **Priority Score:** 703.9
- **Functions:** 7/7 matched (target 11)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 4. stream

- **Target:** `tokiotungstenite.Stream`
- **Similarity:** 0.46
- **Dependents:** 0
- **Priority Score:** 605.4
- **Functions:** 5/5 matched (target 25)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 5)
- **Missing types:** _none_

### 5. connect

- **Target:** `tokiotungstenite.Connect`
- **Similarity:** 0.75
- **Dependents:** 0
- **Priority Score:** 502.5
- **Functions:** 5/5 matched (target 12)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 6. tls

- **Target:** `tokiotungstenite.Tls`
- **Similarity:** 0.76
- **Dependents:** 0
- **Priority Score:** 402.4
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

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |

