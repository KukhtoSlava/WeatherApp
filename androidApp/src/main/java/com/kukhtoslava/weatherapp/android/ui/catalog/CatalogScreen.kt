package com.kukhtoslava.weatherapp.android.ui.catalog

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kukhtoslava.weatherapp.android.R
import com.kukhtoslava.weatherapp.android.coreui.PortGore
import com.kukhtoslava.weatherapp.android.coreui.Type
import com.kukhtoslava.weatherapp.android.utils.convertIconsToResources
import com.kukhtoslava.weatherapp.android.utils.rememberFlowWithLifecycle
import com.kukhtoslava.weatherapp.domain.models.DBPlace
import com.kukhtoslava.weatherapp.domain.models.weather.CurrentWeather
import com.kukhtoslava.weatherapp.presentation.catalog.CatalogAction
import com.kukhtoslava.weatherapp.presentation.catalog.CatalogEvent
import com.kukhtoslava.weatherapp.presentation.catalog.CatalogState
import com.kukhtoslava.weatherapp.presentation.catalog.CatalogViewModel
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun CatalogScreen(
    navHostController: NavHostController,
    viewModel: CatalogViewModel
) {

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val eventFlow = rememberFlowWithLifecycle(flow = viewModel.eventFlow)

    BackHandler(enabled = true) {
        navHostController.popBackStack()
    }
    LaunchedEffect(Unit) {
        viewModel.dispatch(CatalogAction.Load)
    }

    CatalogUI(
        state = state,
        eventFlow = eventFlow,
        navHostController = navHostController,
        itemClicked = { placeId, placeName ->
            viewModel.dispatch(
                action = CatalogAction.ClickCity(
                    placeId = placeId,
                    placeName = placeName
                )
            )
        },
        closeClicked = { viewModel.dispatch(action = CatalogAction.Close) },
        onRetryClicked = { viewModel.dispatch(action = CatalogAction.Load) }
    )
}

@Composable
fun CatalogUI(
    state: CatalogState,
    eventFlow: Flow<CatalogEvent>,
    navHostController: NavHostController,
    itemClicked: (placeId: String, placeName: String) -> Unit,
    closeClicked: () -> Unit,
    onRetryClicked: () -> Unit
) {

    LaunchedEffect(eventFlow) {
        eventFlow.collect { event ->
            when (event) {
                CatalogEvent.Close -> navHostController.popBackStack()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = PortGore
            )
            .statusBarsPadding()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                modifier = Modifier
                    .clickable { closeClicked() }
                    .padding(16.dp),
                text = stringResource(id = R.string.close),
                style = Type.body3,
                maxLines = 1
            )
        }

        if (state.isLoading) {
            LoadingIndicator()
        } else if (state.error != null) {
            ErrorMessage(
                errorMessage = state.error?.message ?: stringResource(id = R.string.unknown_error),
                onRetryClicked = onRetryClicked
            )
        } else {
            WeatherColumn(
                placesWeatherMap = state.placesWeatherMap,
                itemClicked = itemClicked
            )
        }
    }
}

@Composable
private fun WeatherColumn(
    placesWeatherMap: Map<DBPlace, CurrentWeather>,
    itemClicked: (placeId: String, placeName: String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for ((dbPlace, weather) in placesWeatherMap) {
            item(dbPlace.placeId) {
                PlaceItem(
                    currentWeather = weather,
                    dbPlace = dbPlace,
                    itemClicked = itemClicked
                )
            }
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(top = 64.dp)
                .size(100.dp),
            color = Color.White,
            strokeWidth = 5.dp
        )
    }
}

@Composable
private fun ErrorMessage(
    errorMessage: String,
    onRetryClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = errorMessage,
            style = Type.body4,
            maxLines = 3
        )
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = stringResource(id = R.string.dont_know),
            style = Type.body5,
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
}

@Composable
fun PlaceItem(
    currentWeather: CurrentWeather,
    dbPlace: DBPlace,
    itemClicked: (placeId: String, placeName: String) -> Unit
) {

    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .clickable {
                itemClicked(dbPlace.placeId, dbPlace.name)
            }
            .wrapContentSize()
    ) {

        val (
            temperatureRef, tempDistinctionRef, cityNameRef, imageRef, backgroundImageRef, weatherDescriptionRef
        ) = createRefs()

        val temperature = context.getString(
            R.string.degree,
            (currentWeather.main.temp.toInt()).toString()
        )
        val iconId = convertIconsToResources(currentWeather.weather[0].icon)
        val minAndMaxTemperature = context.getString(
            R.string.min_max,
            (currentWeather.main.tempMax.toInt()).toString(),
            (currentWeather.main.tempMin.toInt()).toString()
        )
        val place = dbPlace.name
        val weatherDescription = currentWeather.weather[0].description

        Image(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(backgroundImageRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(20.dp),
            contentDescription = null,
            painter = painterResource(id = R.drawable.place_background),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
        )

        Text(
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(temperatureRef) {
                    start.linkTo(backgroundImageRef.start)
                    top.linkTo(backgroundImageRef.top)
                }
                .padding(top = 36.dp, start = 36.dp),
            text = temperature,
            style = Type.body8,
            maxLines = 1
        )

        Text(
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(cityNameRef) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 36.dp, start = 36.dp),
            text = place,
            textAlign = TextAlign.Start,
            style = Type.body10,
            maxLines = 1
        )

        Text(
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(tempDistinctionRef) {
                    start.linkTo(parent.start)
                    bottom.linkTo(cityNameRef.top)
                }
                .padding(start = 36.dp),
            text = minAndMaxTemperature,
            style = Type.body9,
            maxLines = 1
        )

        Text(
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(weatherDescriptionRef) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 36.dp, end = 36.dp),
            text = weatherDescription,
            textAlign = TextAlign.End,
            style = Type.body11,
            maxLines = 1
        )

        Image(
            modifier = Modifier
                .size(160.dp)
                .constrainAs(imageRef) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
            painter = painterResource(id = iconId),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}
