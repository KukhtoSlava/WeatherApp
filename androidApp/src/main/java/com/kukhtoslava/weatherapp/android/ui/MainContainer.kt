package com.kukhtoslava.weatherapp.android.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.kukhtoslava.weatherapp.android.navigation.Screen
import com.kukhtoslava.weatherapp.android.ui.catalog.CatalogScreen
import com.kukhtoslava.weatherapp.android.ui.description.DescriptionScreen
import com.kukhtoslava.weatherapp.android.ui.main.MainScreen
import com.kukhtoslava.weatherapp.android.ui.search.SearchScreen

@ExperimentalAnimationApi
@Composable
fun MainContainer() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreen(navController, viewModel())
        }
        composable(Screen.Search.route, enterTransition = {
            slideInVertically(
                initialOffsetY = { 600 }, animationSpec = tween(
                    durationMillis = 300, easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        }, exitTransition = {
            slideOutVertically(
                targetOffsetY = { 600 }, animationSpec = tween(
                    durationMillis = 300, easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(300))
        }) {
            SearchScreen(navController, viewModel())
        }
        composable(Screen.Description.route, enterTransition = {
            slideInVertically(
                initialOffsetY = { 600 }, animationSpec = tween(
                    durationMillis = 300, easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        }, exitTransition = {
            slideOutVertically(
                targetOffsetY = { 600 }, animationSpec = tween(
                    durationMillis = 300, easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(300))
        }) {
            DescriptionScreen(navController, viewModel())
        }
        composable(Screen.Catalog.route, enterTransition = {
            slideInVertically(
                initialOffsetY = { 600 }, animationSpec = tween(
                    durationMillis = 300, easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        }, exitTransition = {
            slideOutVertically(
                targetOffsetY = { 600 }, animationSpec = tween(
                    durationMillis = 300, easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(300))
        }) {
            CatalogScreen(navController, viewModel())
        }
    }
}
