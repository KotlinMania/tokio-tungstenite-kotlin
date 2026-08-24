=== Deep Analysis: tmp/tokio-tungstenite (rust) -> src/commonMain/kotlin (kotlin) ===

Scanning source codebase (rust)...
Codebase: tmp/tokio-tungstenite (rust)
  Files: 19
  Total imports: 107

Scanning target codebase (kotlin)...
Codebase: src/commonMain/kotlin (kotlin)
  Files: 13
  Total imports: 34

Comparing codebases...
Computing AST similarities...

=== Codebase Comparison Report ===

Source: tmp/tokio-tungstenite (19 files)
Target: src/commonMain/kotlin (13 files)
Scoring invariant: FnSim is required function body/parameter similarity. Class/type and symbol parity are reported beside it; whole-file shape is diagnostic only.

Matched:   7 files
Unmatched: 12 source, 4 target

=== Matched Files (by porting priority) ===

Source                        Target                        FnSim     Dependents FunctionParityTypeParity  Priority
 --------------------------------------------------------------------------------------------------------------------------
lib                           tokiotungstenite.Lib [PROVENANCE-FALLBACK]0.69      0          28/28         3/3         3103.1    
compat                        tokiotungstenite.Compat [PROVENANCE-FALLBACK]0.59      0          11/11         4/4         1504.1    
handshake                     tokiotungstenite.Handshake [PROVENANCE-FALLBACK]0.40      0          5/5           7/7         1206.0    
proxy                         tokiotungstenite.Proxy [PROVENANCE-FALLBACK]0.54      0          7/7           0/0         704.6     
stream                        tokiotungstenite.Stream [PROVENANCE-FALLBACK]0.46      0          5/5           1/1         605.4     
connect                       tokiotungstenite.Connect [PROVENANCE-FALLBACK]0.75      0          5/5           0/0         502.5     
tls                           tokiotungstenite.Tls [PROVENANCE-FALLBACK]0.75      0          3/3           1/1         402.5     

=== Function and Symbol Details ===

lib -> tokiotungstenite.Lib [PROVENANCE-FALLBACK]
  similarity: 0.69, priority: 3103.1, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:lib.rs` vs expected `lib.rs`
  functions: 28/28 matched (target total: 41, required body score: 0.69)
  missing functions: none
  types: 3/3 matched (target total: 5)
  missing types: none
  tests: 6/6 matched

compat -> tokiotungstenite.Compat [PROVENANCE-FALLBACK]
  similarity: 0.59, priority: 1504.1, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `compat.rs` vs expected `compat.rs`
  functions: 11/11 matched (target total: 15, required body score: 0.59)
  missing functions: none
  types: 4/4 matched (target total: 5)
  missing types: none

handshake -> tokiotungstenite.Handshake [PROVENANCE-FALLBACK]
  similarity: 0.40, priority: 1206.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `handshake.rs` vs expected `handshake.rs`
  functions: 5/5 matched (target total: 6, required body score: 0.40)
  missing functions: none
  types: 7/7 matched (target total: 9)
  missing types: none

proxy -> tokiotungstenite.Proxy [PROVENANCE-FALLBACK]
  similarity: 0.54, priority: 704.6, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `proxy.rs` vs expected `proxy.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:proxy.rs` vs expected `proxy.rs`
  functions: 7/7 matched (target total: 14, required body score: 0.54)
  missing functions: none
  types: 0/0 matched (target total: 4)
  missing types: none
  tests: 1/1 matched

stream -> tokiotungstenite.Stream [PROVENANCE-FALLBACK]
  similarity: 0.46, priority: 605.4, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `stream.rs` vs expected `stream.rs`
  functions: 5/5 matched (target total: 22, required body score: 0.46)
  missing functions: none
  types: 1/1 matched (target total: 4)
  missing types: none

connect -> tokiotungstenite.Connect [PROVENANCE-FALLBACK]
  similarity: 0.75, priority: 502.5, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `connect.rs` vs expected `connect.rs`
  functions: 5/5 matched (target total: 6, required body score: 0.75)
  missing functions: none
  types: 0/0 matched (target total: 0)
  missing types: none

tls -> tokiotungstenite.Tls [PROVENANCE-FALLBACK]
  similarity: 0.75, priority: 402.5, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `tls.rs` vs expected `tls.rs`
  functions: 3/3 matched (target total: 5, required body score: 0.75)
  missing functions: none
  types: 1/1 matched (target total: 8)
  missing types: none


=== Provenance Header Fallbacks ===

These files were paired only after normalization; fix the port-lint source header.
  - lib -> tokiotungstenite.Lib: port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
    proposed: // port-lint: source lib.rs
  - lib -> tokiotungstenite.Lib: port-lint provenance header matched only after fallback normalization: `tests:lib.rs` vs expected `lib.rs`
    proposed: // port-lint: tests lib.rs
  - compat -> tokiotungstenite.Compat: port-lint provenance header matched only after fallback normalization: `compat.rs` vs expected `compat.rs`
    proposed: // port-lint: source compat.rs
  - handshake -> tokiotungstenite.Handshake: port-lint provenance header matched only after fallback normalization: `handshake.rs` vs expected `handshake.rs`
    proposed: // port-lint: source handshake.rs
  - proxy -> tokiotungstenite.Proxy: port-lint provenance header matched only after fallback normalization: `proxy.rs` vs expected `proxy.rs`
    proposed: // port-lint: source proxy.rs
  - proxy -> tokiotungstenite.Proxy: port-lint provenance header matched only after fallback normalization: `tests:proxy.rs` vs expected `proxy.rs`
    proposed: // port-lint: tests proxy.rs
  - stream -> tokiotungstenite.Stream: port-lint provenance header matched only after fallback normalization: `stream.rs` vs expected `stream.rs`
    proposed: // port-lint: source stream.rs
  - connect -> tokiotungstenite.Connect: port-lint provenance header matched only after fallback normalization: `connect.rs` vs expected `connect.rs`
    proposed: // port-lint: source connect.rs
  - tls -> tokiotungstenite.Tls: port-lint provenance header matched only after fallback normalization: `tls.rs` vs expected `tls.rs`
    proposed: // port-lint: source tls.rs

=== Missing from Target (need to port) ===

File                          Deps    Path
------------------------------------------------------------------------------
examples.autobahn-client      0       examples/autobahn-client.rs
examples.autobahn-server      0       examples/autobahn-server.rs
examples.client               0       examples/client.rs
examples.echo-server          0       examples/echo-server.rs
examples.interval-server      0       examples/interval-server.rs
examples.proxy-client         0       examples/proxy-client.rs
examples.server               0       examples/server.rs
examples.server-custom-accep  0       examples/server-custom-accept.rs
examples.server-headers       0       examples/server-headers.rs
tests.communication           0       tests/communication.rs
tests.handshakes              0       tests/handshakes.rs
tests.proxy_integration       0       tests/proxy_integration.rs

=== Porting Quality Summary ===

Matched by exact header:          0 / 7
Matched by provenance fallback:   7 / 7
Matched by name:                  0 / 7
Total TODOs in target: 0
Total lint errors:    9
Stub files:           0

=== Big Picture ===

- Missing files: 12
- Incomplete ports (similarity < 60%): 4
- Stub files: 0
- Files missing functions: 0 (total deficit: 0 functions)
- Type definitions missing: 0
- Files missing tests: 0 (total deficit: 0 unported `#[test]` functions)
- Documentation coverage: 398 / 250 lines (159%)

Primary focus: create missing files (highest deps first)

=== Files with Issues ===

File                          Similarity LineRatio  FunctionParityTests     TODOs Lint  Status
----------------------------------------------------------------------------------------------------
tokiotungstenite.Lib [PROVEN  0.69       0.00       28/28         6/6       0     2     LINT
tokiotungstenite.Compat [PRO  0.59       0.00       11/11         -         0     1     LINT
tokiotungstenite.Handshake [  0.40       0.00       5/5           -         0     1     LINT
tokiotungstenite.Proxy [PROV  0.54       0.00       7/7           1/1       0     2     LINT
tokiotungstenite.Stream [PRO  0.46       0.00       5/5           -         0     1     LINT
tokiotungstenite.Connect [PR  0.75       0.00       5/5           -         0     1     LINT
tokiotungstenite.Tls [PROVEN  0.75       0.00       3/3           -         0     1     LINT

=== Porting Recommendations ===

Incomplete ports (similarity < 60%): 4
Missing files: 12

Incomplete ports to complete:
  compat                         similarity=0.59 function_parity=11/11 dependents=0
  handshake                      similarity=0.40 function_parity=5/5 dependents=0
  proxy                          similarity=0.54 function_parity=7/7 dependents=0
  stream                         similarity=0.46 function_parity=5/5 dependents=0

=== Missing Files (by Dependents) ===

Source File                   Expected Target                       Dependents Path
-----------------------------------------------------------------------------------------------------------------------
examples.autobahn-client      examples.Autobahn-client              0          examples/autobahn-client.rs
examples.autobahn-server      examples.Autobahn-server              0          examples/autobahn-server.rs
examples.client               examples.Client                       0          examples/client.rs
examples.echo-server          examples.Echo-server                  0          examples/echo-server.rs
examples.interval-server      examples.Interval-server              0          examples/interval-server.rs
examples.proxy-client         examples.Proxy-client                 0          examples/proxy-client.rs
examples.server               examples.Server                       0          examples/server.rs
examples.server-custom-accep  examples.Server-custom-accept         0          examples/server-custom-accept.rs
examples.server-headers       examples.Server-headers               0          examples/server-headers.rs
tests.communication           tests.Communication                   0          tests/communication.rs
tests.handshakes              tests.Handshakes                      0          tests/handshakes.rs
tests.proxy_integration       tests.ProxyIntegration                0          tests/proxy_integration.rs

=== Documentation Gaps ===

Documentation coverage: 398 / 250 lines (159%)
Files with >20% doc gap: 1

File                          Src Docs    Tgt Docs    Gap %     DocSim    DocAmt    DocEq     
----------------------------------------------------------------------------------------------
connect                       64          47          26%       0.60      0.73      0.67      

=== Generating Reports ===

✅ Generated: port_status_report.md
✅ Generated: high_priority_ports.md
✅ Generated: NEXT_ACTIONS.md
✅ Generated: port_lint_proposed_changes.md

📁 All reports generated successfully!
