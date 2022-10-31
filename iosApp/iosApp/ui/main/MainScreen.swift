//
//  MainScreen.swift
//  iosApp
//
//  Created by Slava Kukhto on 05/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import mokoMvvmFlowSwiftUI
import MultiPlatformLibrary

struct MainScreen: View {
    
    @EnvironmentObject var globalDataHolder: GlobalDataHolder
    
    @ObservedObject var viewModel: MainViewModel = MainViewModel()
    
    @State private var state: MainState = MainState(placeName: "--", fullWeather: nil, isLoading: true, error: nil)
    @State private var disabled = false
    @State private var place = "--"
    @State private var temperature = "--"
    @State private var description = "--"
    
    private let multiplyDevider: Double = UIUtils.isIphoneSE3orLower() ? 1.8 : 2.3
    
    var body: some View {
        ZStack {
            VStack() {
                VStack {
                    Text(place)
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                        .font(.system(size: 34))
                        .padding(.horizontal, 20)
                        .lineLimit(1)
                    Text(temperature)
                        .fontWeight(.thin)
                        .foregroundColor(.white)
                        .font(.system(size: 96))
                    Text(description)
                        .fontWeight(.medium)
                        .foregroundColor(.white)
                        .font(.system(size: 20))
                }
                .padding(.top, 20)
                Spacer()
                if !UIUtils.isIphoneSE3orLower() {
                    Image("home")
                        .renderingMode(.original)
                        .resizable()
                        .scaledToFill()
                        .frame(width: UIScreen.main.bounds.width - 40, height: 390)
                }
                Spacer()
                    .frame(height: UIUtils.isIphoneSE3orLower() ? 100 : 170)
            }
            VStack {
                Spacer()
                ZStack {
                    VStack {
                        Spacer()
                        Image("modal-background")
                            .renderingMode(.original)
                            .resizable()
                            .frame(width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.height / multiplyDevider)
                    }
                    VStack {
                        Spacer()
                        Image("tab-background")
                            .renderingMode(.original)
                            .frame(width: UIScreen.main.bounds.width)
                        
                    }
                    VStack {
                        Spacer()
                        
                        VStack {
                            VStack {
                                Spacer()
                                Text("hourly_forecast")
                                    .fontWeight(.semibold)
                                    .foregroundColor(Color(ColorUtils.Whisper))
                                    .font(.system(size: 15))
                                    .padding(.top, 16)
                                ZStack {
                                    if (state.isLoading) {
                                        prepareProgress()
                                    } else if (state.error != nil) {
                                        prepareError(errorMessage: state.error?.message ?? "unckniwn_error")
                                    } else if (state.fullWeather != nil) {
                                        prepareWeatherItems(hourlies: state.fullWeather?.hourly ?? [])
                                    } else {
                                        EmptyView()
                                    }
                                }.frame(width: UIScreen.main.bounds.width-20, height: 160)
                                Spacer()
                            }
                            Spacer()
                            HStack {
                                Spacer()
                                ZStack {
                                    Image("subtrack")
                                        .renderingMode(.original)
                                    Button(action: {
                                        viewModel.dispatch(action: MainActionAddCityClicked())
                                    }) {
                                        Image("icon-plus")
                                            .frame(width: 64, height: 64)
                                            .foregroundColor(Color.black)
                                            .background(Color.white)
                                            .clipShape(Circle())
                                    }
                                }
                                Spacer()
                            }
                            
                        }
                        .frame(width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.height / multiplyDevider)
                    }
                    VStack {
                        Spacer()
                        HStack {
                            Button(action: {
                                viewModel.dispatch(action: MainActionDescriptionClicked())
                            }) {
                                Image("icon-menu")
                                    .frame(width: 44, height: 44)
                                    .padding(.leading, 25)
                                    .padding(.bottom, 40)
                            }
                            .disabled(disabled)
                            Spacer()
                            Button(action: {
                                viewModel.dispatch(action: MainActionCatalogClicked())
                            }) {
                                Image("icon-catalog")
                                    .frame(width: 44, height: 44)
                                    .padding(.trailing, 25)
                                    .padding(.bottom, 40)
                            }
                        }
                    }
                }
                
            }
            .edgesIgnoringSafeArea(.bottom)
        }
        .background(Image("main-background")
            .resizable()
            .edgesIgnoringSafeArea(.all)
            .aspectRatio(contentMode: .fill)
            .frame(width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.height))
        .onReceive(createPublisher(viewModel.stateFlow)) { state in
            self.state = state
            if (state.isLoading) {
                self.disabled = true
            } else if (state.error != nil) {
                self.disabled = true
            } else if (state.fullWeather != nil) {
                self.disabled = false
                self.temperature = String(format: NSLocalizedString("degree", comment: "Degree"), state.fullWeather!.current.temp.toInt()!)
                self.description = (state.fullWeather?.current.weather[0].description_)!
            }
            self.place = self.state.placeName
        }
        .onReceive(createPublisher(viewModel.eventFlow)) { event in
            let eventKt = MainEventKs(event)
            switch(eventKt) {
            case .navigationToCatalog:
                withAnimation {
                    globalDataHolder.currentAppScreen = .catalog
                }
                break
            case .navigationToDescription:
                withAnimation {
                    globalDataHolder.currentAppScreen = .description
                }
                break
            case .navigationToSearch:
                withAnimation {
                    globalDataHolder.currentAppScreen = .search
                }
                break
            }
        }
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.3, execute: {
                viewModel.dispatch(action: MainActionLoadData())
            })
        }
    }
}

extension MainScreen {
    
    private func prepareError(errorMessage: String) -> some View {
        VStack {
            Text(errorMessage)
                .fontWeight(.medium)
                .foregroundColor(Color(ColorUtils.Whisper))
                .font(.system(size: 16))
                .padding(.bottom, 16)
            
            Button(action:{
                viewModel.dispatch(action: MainActionLoadData())
            }){
                Text("retry")
                    .fontWeight(.semibold)
                    .foregroundColor(Color(ColorUtils.Whisper))
                    .font(.system(size: 16))
            }
            .buttonStyle(.bordered)
        }
    }
    
    private func prepareWeatherItems(hourlies: [Hourly]) -> some View {
        HStack {
            ScrollView(.horizontal) {
                LazyHStack {
                    ForEach(hourlies, id: \.self) { hourly in
                        let header = convertSecondsToHourString(dt: Int(hourly.dt)).uppercased()
                        let resource = convertIconsToResources(icon: hourly.weather[0].icon)
                        let temp = String(format: NSLocalizedString("degree", comment: "Degree"), hourly.temp.toInt() ?? "--")
                        WeatherPartItem(header: header, resourceName: resource, temperature: temp)
                            .padding(.leading, 4)
                            .padding(.trailing, 4)
                    }
                }
            }
        }
    }
}
