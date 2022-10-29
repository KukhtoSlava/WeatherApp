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

private const val OFFSET_Y = 600
private const val DURATION_IN_MILLIS = 300

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
                initialOffsetY = { OFFSET_Y }, animationSpec = tween(
                    durationMillis = DURATION_IN_MILLIS, easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(DURATION_IN_MILLIS))
        }, exitTransition = {
            slideOutVertically(
                targetOffsetY = { OFFSET_Y }, animationSpec = tween(
                    durationMillis = DURATION_IN_MILLIS, easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(DURATION_IN_MILLIS))
        }) {
            SearchScreen(navController, viewModel())
        }
        composable(Screen.Description.route, enterTransition = {
            slideInVertically(
                initialOffsetY = { DURATION_IN_MILLIS }, animationSpec = tween(
                    durationMillis = DURATION_IN_MILLIS, easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(DURATION_IN_MILLIS))
        }, exitTransition = {
            slideOutVertically(
                targetOffsetY = { OFFSET_Y }, animationSpec = tween(
                    durationMillis = DURATION_IN_MILLIS, easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(DURATION_IN_MILLIS))
        }) {
            DescriptionScreen(navController, viewModel())
        }
        composable(Screen.Catalog.route, enterTransition = {
            slideInVertically(
                initialOffsetY = { OFFSET_Y }, animationSpec = tween(
                    durationMillis = DURATION_IN_MILLIS, easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(DURATION_IN_MILLIS))
        }, exitTransition = {
            slideOutVertically(
                targetOffsetY = { OFFSET_Y }, animationSpec = tween(
                    durationMillis = DURATION_IN_MILLIS, easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(DURATION_IN_MILLIS))
        }) {
            CatalogScreen(navController, viewModel())
        }
    }
}
