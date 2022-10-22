//
//  Double+.swift
//  iosApp
//
//  Created by Slava Kukhto on 20/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation

extension Double {
    func toInt() -> Int? {
        if self >= Double(Int.min) && self < Double(Int.max) {
            return Int(self)
        } else {
            return nil
        }
    }
}
