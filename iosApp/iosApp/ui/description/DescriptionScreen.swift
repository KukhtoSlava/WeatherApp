//
//  DescriptionScreen.swift
//  iosApp
//
//  Created by Slava Kukhto on 05/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import mokoMvvmFlowSwiftUI
import MultiPlatformLibrary

struct DescriptionScreen: View {
    
    @EnvironmentObject var globalDataHolder: GlobalDataHolder
    
    @ObservedObject var viewModel: DescriptionViewModel = DescriptionViewModel()
    @State private var state: DescriptionState = DescriptionState(placeName: "", fullWeather: nil)
    
    var body: some View {
        VStack {
            HStack {
                Spacer()
                Button(action: {
                    viewModel.dispatch(action: DescriptionActionClose())
                }) {
                    Text("close")
                        .font(.system(size: 15))
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                        .padding(16)
                }
            }
            prepareDescriptionContent(state: self.state)
        }
        .edgesIgnoringSafeArea(.bottom)
        .background(
            LinearGradient(gradient: Gradient(colors: ColorUtils.MainBrushColors), startPoint: .top, endPoint: .bottom)
        )
        .onReceive(createPublisher(viewModel.stateFlow)) { state in
            self.state = state
        }
        .onReceive(createPublisher(viewModel.eventFlow)) { event in
            let eventKt = DescriptionEventKs(event)
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
                viewModel.dispatch(action: DescriptionActionLoad())
            })
        }
    }
}

extension DescriptionScreen {
    
    func prepareDescriptionContent(state: DescriptionState) -> some View {
        
        let place = state.placeName
        let fullDescription: String
        let sunrise: String
        let sunset: String
        let feelLikes: String
        let uviIndex: String
        let humidity: String
        let wind: String
        let visibility: String
        let pressure: String
        let daily: [Daily]
        
        if (state.fullWeather != nil) {
            let temp = (state.fullWeather?.current.temp.toInt())
            let description = state.fullWeather?.current.weather[0].description_
            fullDescription = String(format: NSLocalizedString("description_title", comment: "Full description"), temp!, description!)
            
            sunrise = convertSecondsToHoursMinutesString(dt: Int(state.fullWeather!.current.sunrise))
            sunset = convertSecondsToHoursMinutesString(dt: Int(state.fullWeather!.current.sunset))
            
            let feelLikesTemperature = state.fullWeather?.current.feelsLike.toInt()
            feelLikes = String(format: NSLocalizedString("degree", comment: "Degree"), feelLikesTemperature!)
            
            uviIndex = String("\(state.fullWeather!.current.uvi)")
            
            humidity = String(format: NSLocalizedString("humidity_percent", comment: "Humidity percent"), state.fullWeather!.current.humidity)
            
            wind = String(format: NSLocalizedString("speed", comment: "Wind speed"), String("\(state.fullWeather!.current.windSpeed)"))
            
            visibility = String(format: NSLocalizedString("visibility_meters", comment: "Visibility"), String("\(((state.fullWeather!.current.visibility)) / 1000)"))
            
            pressure = String(format: NSLocalizedString("pressure_metric", comment: "Pressure"), String("\(state.fullWeather!.current.pressure)"))
            
            daily = state.fullWeather?.daily ?? []
            
        } else {
            fullDescription = ""
            sunrise = "--"
            sunset = "--"
            feelLikes = "--"
            uviIndex = "--"
            humidity = "--"
            wind = "--"
            visibility = "--"
            pressure = "--"
            daily = []
        }
        
        return ScrollView {
            VStack {
                Text(place)
                    .font(.system(size: 34))
                    .fontWeight(.medium)
                    .foregroundColor(.white)
                    .padding([.leading, .top, .trailing], 8)
                Text(fullDescription)
                    .font(.system(size: 18))
                    .fontWeight(.semibold)
                    .foregroundColor(Color(ColorUtils.Whisper50))
                    .padding([.leading, .trailing], 8)
                VStack {
                    HStack {
                        Text("weekly_forecast")
                            .font(.system(size: 15))
                            .fontWeight(.semibold)
                            .foregroundColor(Color(ColorUtils.Whisper50))
                            .padding([.leading, .top, .trailing], 20)
                        Spacer()
                    }
                    HStack {
                        ScrollView(.horizontal) {
                            LazyHStack {
                                ForEach(daily, id: \.self) { day in
                                    let header = convertSecondsToDaysString(dt: Int(day.dt)).uppercased()
                                    let resource = convertIconsToResources(icon: day.weather[0].icon)
                                    let temp = String(format: NSLocalizedString("degree", comment: "Degree"), day.temp.day.toInt() ?? "--")
                                    WeatherPartItem(header: header, resourceName: resource, temperature: temp)
                                        .padding(.leading, 4)
                                        .padding(.trailing, 4)
                                }
                            }
                        }
                    }
                    .padding(20)
                }
                .frame(height: 250)
                .background(Color(ColorUtils.PortGore))
                .cornerRadius(28)
                .overlay(RoundedRectangle(cornerRadius: 28)
                    .stroke(Color(ColorUtils.Gigas), lineWidth: 2.0))
                
                HStack {
                    prepareItemView(title: "sunrise", value: sunrise)
                    prepareItemView(title: "sunset", value: sunset)
                }
                
                HStack {
                    prepareItemView(title: "feels_like", value: feelLikes)
                    prepareItemView(title: "uvi", value: uviIndex)
                }
                
                HStack {
                    prepareItemView(title: "wind", value: wind)
                    prepareItemView(title: "humidity", value: humidity)
                }
                
                HStack {
                    prepareItemView(title: "visibility", value: visibility)
                    prepareItemView(title: "pressure", value: pressure)
                }
            }
            .padding(16)
        }
    }
    
    func prepareItemView(title: String, value: String) -> some View {
        
        let width = (UIScreen.main.bounds.width - 32) / 2
        
        return VStack {
            HStack {
                Text(title.localized)
                    .font(.system(size: 15))
                    .fontWeight(.semibold)
                    .foregroundColor(Color(ColorUtils.Whisper50))
                    .padding([.leading, .top, .trailing], 20)
                Spacer()
            }
            HStack {
                Text(value)
                    .font(.system(size: 32))
                    .fontWeight(.semibold)
                    .foregroundColor(.white)
                    .padding([.leading, .top, .trailing], 16)
                Spacer()
            }
            Spacer()
        }
        .frame(width: width, height: width)
        .background(Color(ColorUtils.PortGore))
        .cornerRadius(28)
        .overlay(RoundedRectangle(cornerRadius: 28)
            .stroke(Color(ColorUtils.Gigas), lineWidth: 2.0))
    }
}
