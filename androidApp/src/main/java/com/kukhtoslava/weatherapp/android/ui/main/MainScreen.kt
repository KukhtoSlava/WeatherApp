package com.kukhtoslava.weatherapp.android.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kukhtoslava.weatherapp.android.R
import com.kukhtoslava.weatherapp.android.coreui.Type
import com.kukhtoslava.weatherapp.android.coreui.WeatherShapes
import com.kukhtoslava.weatherapp.android.navigation.Screen
import com.kukhtoslava.weatherapp.android.uihelpers.WeatherPartItem
import com.kukhtoslava.weatherapp.android.utils.convertIconsToResources
import com.kukhtoslava.weatherapp.android.utils.convertSecondsToHourString
import com.kukhtoslava.weatherapp.android.utils.rememberFlowWithLifecycle
import com.kukhtoslava.weatherapp.presentation.main.MainAction
import com.kukhtoslava.weatherapp.presentation.main.MainEvent
import com.kukhtoslava.weatherapp.presentation.main.MainState
import com.kukhtoslava.weatherapp.presentation.main.MainViewModel
import kotlinx.coroutines.flow.Flow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MainScreen(
    navHostController: NavHostController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val eventFlow = rememberFlowWithLifecycle(flow = viewModel.eventFlow)

    BackHandler(enabled = true) {
        (context as? Activity)?.finish()
    }
    LaunchedEffect(Unit) {
        viewModel.dispatch(action = MainAction.LoadData)
    }

    MainUI(
        state = state,
        eventFlow = eventFlow,
        navHostController = navHostController,
        onAddPlaceClicked = { viewModel.dispatch(action = MainAction.AddCityClicked) },
        onDescriptionClicked = { viewModel.dispatch(action = MainAction.DescriptionClicked) },
        onCatalogClicked = { viewModel.dispatch(action = MainAction.CatalogClicked) },
        onRetryClicked = { viewModel.dispatch(action = MainAction.LoadData) }
    )
}

@Composable
fun MainUI(
    state: MainState,
    eventFlow: Flow<MainEvent>,
    navHostController: NavHostController,
    onAddPlaceClicked: () -> Unit,
    onDescriptionClicked: () -> Unit,
    onCatalogClicked: () -> Unit,
    onRetryClicked: () -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(eventFlow) {
        eventFlow.collect { event ->
            when (event) {
                MainEvent.NavigationToCatalog -> navHostController.navigate(Screen.Catalog.route)
                MainEvent.NavigationToDescription -> navHostController.navigate(Screen.Description.route)
                MainEvent.NavigationToSearch -> navHostController.navigate(Screen.Search.route)
            }
        }
    }

    Surface {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.main_background),
                    contentScale = ContentScale.FillWidth
                )
        ) {

            val place = state.placeName
            val temperature = context.getString(
                R.string.degree,
                (state.fullWeather?.current?.temp?.toInt() ?: "--").toString()
            )
            val description = state.fullWeather?.current?.weather?.get(0)?.description ?: "--"
            var enableDescription = true

            val (
                tabBar, modal, home, weather, subtrack, searchBtn,
                descriptionBtn, catalogBtn
            ) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.home), contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .width(250.dp)
                    .constrainAs(home) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(weather.bottom)
                    }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(modal) {
                        bottom.linkTo(parent.bottom)
                    }
                    .paint(
                        painterResource(id = R.drawable.modal_background),
                        contentScale = ContentScale.FillWidth
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    style = Type.header,
                    text = stringResource(id = R.string.hourly_forecast),
                    maxLines = 1
                )

                if (state.isLoading) {
                    enableDescription = false
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                            .size(100.dp),
                        color = Color.White,
                        strokeWidth = 5.dp
                    )
                } else if (state.error != null) {
                    enableDescription = false
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, top = 32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth(),
                            text = state.error?.message
                                ?: stringResource(id = R.string.unknown_error),
                            style = Type.body,
                            maxLines = 1
                        )

                        Button(onClick = {
                            onRetryClicked()
                        }) {
                            Text(
                                modifier = Modifier
                                    .wrapContentWidth(),
                                text = stringResource(id = R.string.retry),
                                style = Type.button,
                                maxLines = 1
                            )
                        }
                    }
                } else if (state.fullWeather != null) {
                    enableDescription = true
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .horizontalScroll(rememberScrollState())
                            .fillMaxWidth()
                    ) {
                        state.fullWeather?.hourly?.forEach { item ->
                            val temp = context.getString(
                                R.string.degree,
                                (item.temp.toInt()).toString()
                            )
                            WeatherPartItem(
                                header = convertSecondsToHourString(item.dt).uppercase(),
                                id = convertIconsToResources(item.weather[0].icon),
                                temperature = temp
                            )
                        }
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.tab_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .constrainAs(tabBar) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.subtrack),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .constrainAs(subtrack) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                    }
            )

            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(searchBtn) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    }
                    .clip(WeatherShapes.small)
                    .clickable {
                        onAddPlaceClicked()
                    },
                painter = painterResource(id = R.drawable.icon_plus),
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .padding(bottom = 24.dp, start = 36.dp)
                    .constrainAs(descriptionBtn) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    }
                    .clickable(
                        enabled = enableDescription
                    ) {
                        onDescriptionClicked()
                    },
                painter = painterResource(id = R.drawable.icon_menu),
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .padding(bottom = 24.dp, end = 36.dp)
                    .constrainAs(catalogBtn) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    }
                    .clickable {
                        onCatalogClicked()
                    },
                painter = painterResource(id = R.drawable.icon_catalog),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, top = 64.dp)
                    .constrainAs(weather) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = place,
                    style = Type.header2,
                    maxLines = 1
                )

                Text(
                    text = temperature,
                    style = Type.header3,
                    maxLines = 1
                )

                Text(
                    text = description,
                    style = Type.body2,
                    maxLines = 1,
                )
            }
        }
    }
}
