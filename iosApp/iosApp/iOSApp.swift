import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    
    init() {
        MainModuleKt.initializeKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView().ignoresSafeArea()
        }
    }
}
