//
//  SplashScreen.swift
//  iosApp
//
//  Created by Slava Kukhto on 05/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import mokoMvvmFlowSwiftUI
import MultiPlatformLibrary

struct CatalogScreen: View {
    
    @EnvironmentObject var globalDataHolder: GlobalDataHolder
    
    @ObservedObject var viewModel: CatalogViewModel = CatalogViewModel()
    @State private var state: CatalogState = CatalogState(placesWeatherMap: [:], isLoading: false, error: nil)
    
    var body: some View {
        VStack {
            HStack {
                Spacer()
                Button(action: {
                    viewModel.dispatch(action: CatalogActionClose())
                }) {
                    Text("close")
                        .font(.system(size: 15))
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                        .padding(16)
                }
            }
            VStack {
                if (state.isLoading) {
                    prepareProgress()
                } else if (state.error != nil) {
                    prepareError(errorMessage: state.error?.message ?? "unckniwn_error")
                } else {
                    prepareWatherColumn(placesWeatherMap: state.placesWeatherMap)
                }
            }
            Spacer()
        }
        .edgesIgnoringSafeArea(.bottom)
        .background(Color(ColorUtils.PortGore))
        .onReceive(createPublisher(viewModel.stateFlow)) { state in
            self.state = state
        }
        .onReceive(createPublisher(viewModel.eventFlow)) { event in
            let eventKt = CatalogEventKs(event)
            switch(eventKt) {
            case .close:
                withAnimation {
                    globalDataHolder.currentAppScreen = .main
                }
                break
            }
        }
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.3, execute: {
                viewModel.dispatch(action: CatalogActionLoad())
            })
        }
    }
}

extension CatalogScreen {
    
    func prepareWatherColumn(placesWeatherMap: Dictionary<DBPlace, CurrentWeather>) -> some View {
        
        let placesWeatherMapSorted = placesWeatherMap.sorted(by: { $0.key.name < $1.key.name })
        
        return ScrollView {
            LazyVStack {
                ForEach(placesWeatherMapSorted, id: \.key) { (place, weather) in
                    preparePlaceItem(currentWeather: weather, dbPlace: place)
                }
            }
        }
    }
    
    func preparePlaceItem(currentWeather: CurrentWeather,
                          dbPlace: DBPlace) -> some View {
        
        let temperature = String(format: NSLocalizedString("degree", comment: "Degree"), currentWeather.main.temp.toInt()!)
        let iconName = convertIconsToResources(icon: currentWeather.weather[0].icon)
        let minAndMaxTemperature = String(format: NSLocalizedString("min_max", comment: "Min & Max"), currentWeather.main.tempMax.toInt()!, currentWeather.main.tempMin.toInt()!)
        let place = dbPlace.name
        let weatherDescription = currentWeather.weather[0].description_
        
        return Button(
            action: {
                viewModel.dispatch(action: CatalogActionClickCity(
                    placeName: dbPlace.name, placeId: dbPlace.placeId)
                )
            }
        ) {
            VStack {
                HStack {
                    Text(temperature)
                        .font(.system(size: 64))
                        .fontWeight(.medium)
                        .foregroundColor(.white)
                    Spacer()
                    Image(iconName)
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(width: 140, height: 120)
                }
                .padding(.horizontal, 16)
                HStack {
                    Text(minAndMaxTemperature)
                        .font(.system(size: 14))
                        .fontWeight(.medium)
                        .foregroundColor(Color(ColorUtils.Whisper50))
                    Spacer()
                }
                .padding(.horizontal, 24)
                HStack {
                    Text(place)
                        .font(.system(size: 18))
                        .fontWeight(.medium)
                        .foregroundColor(.white)
                        .lineLimit(1)
                    Spacer()
                    Text(weatherDescription)
                        .font(.system(size: 16))
                        .fontWeight(.medium)
                        .foregroundColor(.white)
                }
                .padding(.horizontal, 24)
                Spacer()
            }
        }
        .padding(.horizontal, 24)
        .padding(.bottom, 8)
        .background(Image("place-background")
            .renderingMode(.original)
            .resizable()
            .aspectRatio(contentMode: .fit)
        )
    }
    
    private func prepareError(errorMessage: String) -> some View {
        VStack {
            Text(errorMessage)
                .fontWeight(.medium)
                .foregroundColor(Color(ColorUtils.Whisper))
                .font(.system(size: 16))
                .padding(.bottom, 16)
            Text("dont_know")
                .font(.system(size: 72))
                .fontWeight(.semibold)
                .foregroundColor(.white)
                .padding(16)
            Button(action:{
                viewModel.dispatch(action: CatalogActionLoad())
            }){
                Text("retry")
                    .fontWeight(.semibold)
                    .foregroundColor(Color(ColorUtils.Whisper))
                    .font(.system(size: 16))
            }
            .buttonStyle(.bordered)
        }
    }
}
