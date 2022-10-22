import SwiftUI
import Combine
import mokoMvvmFlowSwiftUI
import MultiPlatformLibrary

struct ContentView: View {
    
    @ObservedObject var viewModel: SplashViewModel = SplashViewModel()
    @State var text: String = Greeting().greeting()
    
	var body: some View {
        VStack(spacing: 8.0) {
            Text(text)
        }.padding()
        .onReceive(createPublisher(viewModel.stateFlow)) { state in
           
        }
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
