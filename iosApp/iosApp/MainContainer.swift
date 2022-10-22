//
//  MainContainer.swift
//  iosApp
//
//  Created by Slava Kukhto on 05/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import MultiPlatformLibrary

struct MainContainer: View {
    
    @EnvironmentObject var globalDataHolder: GlobalDataHolder
    
    var body: some View {
        ZStack {
            switch globalDataHolder.currentAppScreen {
            case .main:
                MainScreen()
                    .transition(.opacity)
            case .catalog:
                CatalogScreen()
                    .transition(.move(edge: .bottom))
            case .description:
                DescriptionScreen()
                    .transition(.move(edge: .bottom))
            case.search:
                SearchScreen()
                    .transition(.move(edge: .bottom))
            }
        }
        .preferredColorScheme(.dark)
    }
}
