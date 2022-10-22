//
//  WeatherMapper.swift
//  iosApp
//
//  Created by Slava Kukhto on 20/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation

func convertSecondsToHourString(dt: Int) -> String {
    let date = Date(timeIntervalSince1970: TimeInterval(dt))
    let dateFormatterGet = DateFormatter()
    dateFormatterGet.locale = Locale(identifier: "en_us")
    dateFormatterGet.dateFormat = "h a"
    return dateFormatterGet.string(from: date)
}

func convertSecondsToDaysString(dt: Int) -> String {
    let date = Date(timeIntervalSince1970: TimeInterval(dt))
    let dateFormatterGet = DateFormatter()
    dateFormatterGet.locale = Locale(identifier: "en_us")
    dateFormatterGet.dateFormat = "EEE"
    return dateFormatterGet.string(from: date)
}

func convertSecondsToHoursMinutesString(dt: Int) -> String {
    let date = Date(timeIntervalSince1970: TimeInterval(dt))
    let dateFormatterGet = DateFormatter()
    dateFormatterGet.locale = Locale(identifier: "en_us")
    dateFormatterGet.dateFormat = "h:mm a"
    return dateFormatterGet.string(from: date)
}

func convertIconsToResources(icon: String) -> String {
    switch(icon) {
    case "01d":
        return "clear-day"
    case "01n":
        return "clear-night"
    case "02d":
        return "partly-cloudy-day"
    case "02n":
        return "partly-cloudy-night"
    case "03d":
        return "cloudy"
    case "03n":
        return "cloudy"
    case "04d":
        return "overcast"
    case "04n":
        return "overcast"
    case "09d":
        return "heavy-showers"
    case "09n":
        return "heavy-showers"
    case "10d":
        return "showers"
    case "10n":
        return "showers"
    case "11d":
        return "thunderstorm-showers"
    case "11n":
        return "thunderstorm-showers"
    case "13d":
        return "snow"
    case "13n":
        return "snow"
    case "50d":
        return "windy"
    case "50n":
        return "windy"
    default:
        return "clear-day"
    }
}
