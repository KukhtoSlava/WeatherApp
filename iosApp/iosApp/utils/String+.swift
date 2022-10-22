//
//  String+.swift
//  iosApp
//
//  Created by Slava Kukhto on 21/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation

extension String {
    var localized: String {
        return NSLocalizedString(self, comment:"")
    }
}
