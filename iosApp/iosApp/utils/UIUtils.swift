//
//  UIUtils.swift
//  iosApp
//
//  Created by Slava Kukhto on 20/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import UIKit
import SwiftUI

struct UIUtils {
    
    static func getSafeareaTop() -> CGFloat {
        if let keyWindow = UIApplication.shared.windows.filter({$0.isKeyWindow}).first {
            return keyWindow.windowScene?.statusBarManager?.statusBarFrame.height ?? 20
        }
        return 20
    }
    
    static var hasSafeArea: Bool {
        guard let topPadding = UIApplication.shared.windows.first?.safeAreaInsets.top, topPadding > 24 else {
            return false
        }
        return true
    }
    
    static func isIphoneSE3orLower() -> Bool {
        return UIScreen.main.bounds.height < 668
    }
}
