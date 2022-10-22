//
//  UIHelper.swift
//  iosApp
//
//  Created by Slava Kukhto on 19/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

func WeatherPartItem(
    header: String,
    resourceName: String,
    temperature: String
) -> some View {
    return VStack {
        Text(header)
            .fontWeight(.semibold)
            .foregroundColor(.white)
            .font(.system(size: 15))
            .padding(.bottom, 8)
        Image(resourceName)
            .renderingMode(.original)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(width: 44, height: 44, alignment: .center)
        Text(temperature)
            .fontWeight(.regular)
            .foregroundColor(.white)
            .font(.system(size: 20))
    }
    .frame(width: 60, height: 150)
    .background(Color(ColorUtils.Gigas20))
    .cornerRadius(50)
    .overlay(RoundedRectangle(cornerRadius: 50)
                .stroke(Color(ColorUtils.Whisper20), lineWidth: 1))
}

func prepareProgress() -> some View {
    VStack {
        Spacer()
        ProgressView()
            .frame(width: 150, height: 150)
        Spacer()
    }
}
