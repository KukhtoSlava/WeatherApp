package com.kukhtoslava.weatherapp.android.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Description : Screen("description")
    object Search : Screen("search")
    object Catalog : Screen("catalog")
}
