//
//  EventsMapper.swift
//  iosApp
//
//  Created by Slava Kukhto on 20/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import MultiPlatformLibrary

public enum MainEventKs {

  case navigationToCatalog
  case navigationToDescription
  case navigationToSearch

  public var sealed: MainEvent {
    switch self {
    case .navigationToCatalog:
      return MultiPlatformLibrary.MainEventNavigationToCatalog() as MultiPlatformLibrary.MainEvent
    case .navigationToDescription:
      return MultiPlatformLibrary.MainEventNavigationToDescription() as MultiPlatformLibrary.MainEvent
    case .navigationToSearch:
      return MultiPlatformLibrary.MainEventNavigationToSearch() as MultiPlatformLibrary.MainEvent
    }
  }

  public init(_ obj: MainEvent) {
    if obj is MultiPlatformLibrary.MainEventNavigationToCatalog {
      self = .navigationToCatalog
    } else if obj is MultiPlatformLibrary.MainEventNavigationToDescription {
      self = .navigationToDescription
    } else if obj is MultiPlatformLibrary.MainEventNavigationToSearch {
      self = .navigationToSearch
    } else {
      fatalError("MainEventKs not synchronized with MainEvent class")
    }
  }
}

public enum SearchEventKs {

  case close

  public var sealed: SearchEvent {
      switch self {
      case .close:
          return MultiPlatformLibrary.SearchEventClose() as MultiPlatformLibrary.SearchEvent
      }
  }

  public init(_ obj: SearchEvent) {
    if obj is MultiPlatformLibrary.SearchEventClose {
      self = .close
    } else {
      fatalError("SearchEventKs not synchronized with SearchEvent class")
    }
  }
}

public enum DescriptionEventKs {

  case close

  public var sealed: DescriptionEvent {
    switch self {
    case .close:
      return MultiPlatformLibrary.DescriptionEventClose() as MultiPlatformLibrary.DescriptionEvent
    }
  }

  public init(_ obj: DescriptionEvent) {
    if obj is MultiPlatformLibrary.DescriptionEventClose {
      self = .close
    } else {
      fatalError("DescriptionEventKs not synchronized with DescriptionEvent class")
    }
  }
}

public enum CatalogEventKs {

  case close

  public var sealed: CatalogEvent {
    switch self {
    case .close:
      return MultiPlatformLibrary.CatalogEventClose() as MultiPlatformLibrary.CatalogEvent
    }
  }

  public init(_ obj: CatalogEvent) {
    if obj is MultiPlatformLibrary.CatalogEventClose {
      self = .close
    } else {
      fatalError("CatalogEventKs not synchronized with CatalogEvent class")
    }
  }
}
