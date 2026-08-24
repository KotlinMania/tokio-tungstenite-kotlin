import Testing
import TokioTungstenite

// Smoke test for the Kotlin → Swift Export → SPM → swift test pipeline.
@Suite
struct TokioTungsteniteExportTests {
    @Test
    func swiftModuleLoads() {
        let result = TokioTungstenite.domain(request: "ws://localhost:8080/ws")
        #expect(result.isSuccess, "domain extraction should succeed")
    }
}
