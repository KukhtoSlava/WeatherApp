//
//  GlobalDataHolder.swift
//  iosApp
//
//  Created by Slava Kukhto on 05/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Combine

enum AppScreen {
    case catalog
    case main
    case description
    case search
}

class GlobalDataHolder: ObservableObject {
    
    @Published var currentAppScreen: AppScreen = .main
}
