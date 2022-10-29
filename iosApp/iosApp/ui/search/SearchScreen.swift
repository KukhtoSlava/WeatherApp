//
//  SearchScreen.swift
//  iosApp
//
//  Created by Slava Kukhto on 05/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import mokoMvvmFlowSwiftUI
import MultiPlatformLibrary

struct SearchScreen: View {
    
    private enum AlertType: String, Identifiable {
        
        var id: String { rawValue }
        
        case deniedAlwaysLocationDialog = "deniedAlwaysLocationDialog"
        case locationDisabledDialog = "locationDisabledDialog"
        case unknownErrorLocationDialog = "unknownErrorLocationDialog"
    }
    
    @EnvironmentObject var globalDataHolder: GlobalDataHolder
    @ObservedObject var viewModel: SearchViewModel = SearchViewModel()
    
    @State private var searchText = ""
    @State private var alertType: AlertType? = nil
    @State private var state: SearchState = SearchState(
        isExistCurrentPlace: false,
        term: "",
        predictions: [],
        isLoading: false,
        error: nil
    )
    
    var body: some View {
        VStack {
            HStack {
                Spacer()
                Button(action: {
                    if (state.isExistCurrentPlace){
                        viewModel.dispatch(action: SearchActionCloseScreen())
                    }
                }) {
                    let text = state.isExistCurrentPlace == true ? "close" : ""
                    Text(text)
                        .font(.system(size: 15))
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                        .padding(16)
                }
            }
            HStack {
                HStack {
                    Image(systemName: "magnifyingglass")
                    TextField("city", text: $searchText)
                        .foregroundColor(.primary)
                    Button(action: {
                        self.searchText = ""
                        viewModel.dispatch(action: SearchActionClear())
                    }) {
                        Image(systemName: "xmark.circle.fill").opacity(searchText == "" ? 0 : 1)
                    }
                }
                .padding(EdgeInsets(top: 8, leading: 6, bottom: 8, trailing: 6))
                .frame(height: 60)
                .foregroundColor(.secondary)
                .background(Color(ColorUtils.Rhino))
                .cornerRadius(16.0)
            }
            .padding(.horizontal)
            Spacer()
            VStack {
                if (state.isLoading) {
                    prepareProgress()
                } else if (state.error != nil) {
                    prepareErrorMessage(errorMessage: state.error?.message ?? "unknown_error")
                } else if (state.predictions.isEmpty && !state.term.isEmpty) {
                    prepareEmptyList()
                } else if (state.predictions.isEmpty && state.term.isEmpty) {
                    prepareStartSearchDescription()
                } else {
                    preparePredictionsListView(predictions: state.predictions)
                        .padding(.leading, 12)
                }
            }
        }
        .background(
            LinearGradient(gradient: Gradient(colors: ColorUtils.MainBrushColors), startPoint: .top, endPoint: .bottom)
        )
        .onReceive(createPublisher(viewModel.eventFlow)) { event in
            let eventKt = SearchEventKs(event)
            print(eventKt)
            switch(eventKt) {
            case .close:
                withAnimation(.easeInOut(duration: 0.4)) {
                    globalDataHolder.currentAppScreen = .main
                }
                break
            case .deniedAlwaysMessage:
                self.alertType = .deniedAlwaysLocationDialog
                break
            case .deniedMessage:
                // not impossible in ios
                break
            case .errorMessage:
                self.alertType = .unknownErrorLocationDialog
                break
            case .disabledMessage:
                self.alertType = .locationDisabledDialog
                break
            }}
        .onReceive(createPublisher(viewModel.stateFlow)) { state in
            self.state = state
        }
        .alert(item: $alertType) { type in
            switch(type) {
            case .deniedAlwaysLocationDialog:
                return Alert(
                    title: Text("location_permission_title"),
                    message: Text("location_permission_description"),
                    primaryButton: .default(Text("close")) {
                        self.alertType = nil
                    },
                    secondaryButton: .default(Text("open_settings")) {
                        self.alertType = nil
                        if let url = URL(string: UIApplication.openSettingsURLString) {
                            UIApplication.shared.open(url)
                        }
                    }
                )
                break
            case .unknownErrorLocationDialog:
                return Alert(
                    title: Text("unknown_error"),
                    message: Text("something_went_wrong"),
                    dismissButton: .default(Text("close")) {
                        self.alertType = nil
                    }
                )
                break
            case .locationDisabledDialog:
                return Alert(
                    title: Text("location_disabled"),
                    message: Text("location_disabled_description"),
                    primaryButton: .default(Text("close")) {
                        self.alertType = nil
                    },
                    secondaryButton: .default(Text("open_settings")) {
                        self.alertType = nil
                        if let url = URL(string: "\(UIApplication.openSettingsURLString)&path=LOCATION_SERVICES") {
                            UIApplication.shared.open(url)
                        }
                    }
                )
                break
            }
        }
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.3, execute: {
                viewModel.dispatch(action: SearchActionCheckCurrentPlace())
            })
        }
        .onChange(of: searchText, perform: { text in
            viewModel.dispatch(action: SearchActionSearch(term: text))
        })
    }
}

fileprivate extension SearchScreen {
    
    func prepareErrorMessage(errorMessage: String) -> some View {
        VStack {
            Spacer()
            Text(errorMessage)
                .fontWeight(.semibold)
                .multilineTextAlignment(.center)
                .font(.system(size: 22))
                .foregroundColor(.white)
                .lineLimit(3)
                .padding(16)
            Text("dont_know")
                .font(.system(size: 72))
                .fontWeight(.semibold)
                .foregroundColor(.white)
                .padding(16)
            Spacer()
        }
    }
    
    func preparePredictionsListView(predictions: Array<Prediction>) -> some View {
        VStack {
            ScrollView(.vertical) {
                ForEach(predictions, id:\.self) { prediction in
                    Button {
                        viewModel.dispatch(action: SearchActionClickCity(placeName: prediction.description_, placeId: prediction.placeId))
                    } label: {
                        HStack {
                            Text(prediction.description_)
                                .fontWeight(.semibold)
                                .foregroundColor(.white)
                                .font(.system(size: 18))
                                .lineLimit(1)
                            Spacer()
                        }
                        
                    }
                    .padding(.all, 10)
                }
                .resignKeyboardOnDragGesture()
            }
        }
    }
    
    func prepareEmptyList() -> some View {
        VStack {
            Spacer()
            Text("empty_result_search")
                .font(.system(size: 22))
                .fontWeight(.semibold)
                .foregroundColor(.white)
                .lineLimit(3)
                .padding(16)
            Text("dont_know")
                .font(.system(size: 72))
                .fontWeight(.semibold)
                .foregroundColor(.white)
                .padding(16)
            Spacer()
        }
    }
    
    func prepareStartSearchDescription() -> some View {
        VStack {
            Spacer()
            Button(action: {
                viewModel.dispatch(action: SearchActionClickLocation())
            }, label: {
                (Text("description_search_start")
                 + Text("description_location_end")
                    .foregroundColor(Color(ColorUtils.Whisper50))
                    .underline()
                )
                .fontWeight(.semibold)
                .font(.system(size: 22))
                .foregroundColor(.white)
                .padding(16)
                .fixedSize(horizontal: false, vertical: true)
            })
            .padding([.horizontal], 20)
            Spacer()
        }
    }
}

// Update for iOS 15
// MARK: - UIApplication extension for resgning keyboard on pressing the cancel buttion of the search bar
extension UIApplication {
    /// Resigns the keyboard.
    ///
    /// Used for resigning the keyboard when pressing the cancel button in a searchbar based on [this](https://stackoverflow.com/a/58473985/3687284) solution.
    /// - Parameter force: set true to resign the keyboard.
    func endEditing(_ force: Bool) {
        let scenes = UIApplication.shared.connectedScenes
        let windowScene = scenes.first as? UIWindowScene
        let window = windowScene?.windows.first
        window?.endEditing(force)
    }
}

struct ResignKeyboardOnDragGesture: ViewModifier {
    var gesture = DragGesture().onChanged{_ in
        UIApplication.shared.endEditing(true)
    }
    func body(content: Content) -> some View {
        content.gesture(gesture)
    }
}

extension View {
    func resignKeyboardOnDragGesture() -> some View {
        return modifier(ResignKeyboardOnDragGesture())
    }
}
