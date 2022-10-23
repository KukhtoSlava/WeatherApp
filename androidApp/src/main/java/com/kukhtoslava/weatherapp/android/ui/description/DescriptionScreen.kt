package com.kukhtoslava.weatherapp.android.ui.description

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kukhtoslava.weatherapp.android.R
import com.kukhtoslava.weatherapp.android.coreui.*
import com.kukhtoslava.weatherapp.android.uihelpers.WeatherPartItem
import com.kukhtoslava.weatherapp.android.utils.convertIconsToResources
import com.kukhtoslava.weatherapp.android.utils.convertSecondsToDaysString
import com.kukhtoslava.weatherapp.android.utils.convertSecondsToHoursMinutesString
import com.kukhtoslava.weatherapp.android.utils.rememberFlowWithLifecycle
import com.kukhtoslava.weatherapp.presentation.description.DescriptionAction
import com.kukhtoslava.weatherapp.presentation.description.DescriptionEvent
import com.kukhtoslava.weatherapp.presentation.description.DescriptionState
import com.kukhtoslava.weatherapp.presentation.description.DescriptionViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun DescriptionScreen(
    navHostController: NavHostController,
    viewModel: DescriptionViewModel
) {

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val eventFlow = rememberFlowWithLifecycle(flow = viewModel.eventFlow)

    BackHandler(enabled = true) {
        navHostController.popBackStack()
    }
    LaunchedEffect(Unit) {
        viewModel.dispatch(DescriptionAction.Load)
    }
    LaunchedEffect(eventFlow) {
        eventFlow.collect { event ->
            when (event) {
                DescriptionEvent.Close -> navHostController.popBackStack()
            }
        }
    }

    DescriptionUI(
        state = state,
        closeClicked = { viewModel.dispatch(action = DescriptionAction.Close) }
    )
}

@Composable
fun DescriptionUI(
    state: DescriptionState,
    closeClicked: () -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = mainBrush
            )
            .statusBarsPadding()
    ) {

        val place = state.placeName
        val temp = state.fullWeather?.current?.temp?.toInt() ?: "--"
        val description = state.fullWeather?.current?.weather?.get(0)?.description ?: ""
        val fullDescription = context.getString(
            R.string.description_title,
            temp,
            description
        )
        val sunrise = state.fullWeather?.current?.sunrise?.let { dt ->
            convertSecondsToHoursMinutesString(dt)
        } ?: "--"
        val sunset = state.fullWeather?.current?.sunset?.let { dt ->
            convertSecondsToHoursMinutesString(dt)
        } ?: "--"
        val feelLikes = state.fullWeather?.current?.feelsLike?.toInt()?.let { temperature ->
            context.getString(
                R.string.degree,
                temperature.toString()
            )
        } ?: "--"
        val uviIndex = state.fullWeather?.current?.uvi?.toString() ?: "--"
        val humidity = state.fullWeather?.current?.humidity?.let { hum ->
            context.getString(
                R.string.humidity_percent,
                hum.toString()
            )
        } ?: "--"
        val wind = state.fullWeather?.current?.windSpeed?.let { wind ->
            context.getString(
                R.string.speed,
                wind.toString()
            )
        } ?: "--"
        val visibility = state.fullWeather?.current?.visibility?.let { vis ->
            val visInKm = vis / 1000
            context.getString(
                R.string.visibility_meters,
                visInKm.toString()
            )
        } ?: "--"
        val pressure = state.fullWeather?.current?.pressure?.let { press ->
            context.getString(
                R.string.pressure_metric,
                press.toString()
            )
        } ?: "--"


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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp),
                text = place,
                style = Type.header2,
                maxLines = 1
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                text = fullDescription,
                style = Type.header4,
                maxLines = 1
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .border(2.dp, color = Gigas, shape = WeatherShapes.extraLarge)
                    .clip(WeatherShapes.extraLarge)
                    .background(color = PortGore)
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    text = stringResource(id = R.string.weekly_forecast),
                    style = Type.header5,
                    maxLines = 1
                )

                Row(
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .horizontalScroll(rememberScrollState())
                        .fillMaxWidth()
                ) {
                    state.fullWeather?.daily?.forEach { item ->
                        val temperature = context.getString(
                            R.string.degree,
                            (item.temp.day.toInt()).toString()
                        )
                        WeatherPartItem(
                            header = convertSecondsToDaysString(item.dt).uppercase(),
                            id = convertIconsToResources(item.weather[0].icon),
                            temperature = temperature
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 4.dp)
                        .weight(1f)
                        .aspectRatio(1f)
                        .border(2.dp, color = Gigas, shape = WeatherShapes.extraLarge)
                        .clip(WeatherShapes.extraLarge)
                        .background(color = PortGore)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        text = stringResource(id = R.string.sunrise),
                        style = Type.header5,
                        maxLines = 1
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 22.dp),
                        text = sunrise,
                        style = Type.body7
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 8.dp)
                        .weight(1f)
                        .aspectRatio(1f)
                        .border(2.dp, color = Gigas, shape = WeatherShapes.extraLarge)
                        .clip(WeatherShapes.extraLarge)
                        .background(color = PortGore)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        text = stringResource(id = R.string.sunset),
                        style = Type.header5,
                        maxLines = 1
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 22.dp),
                        style = Type.body7,
                        text = sunset
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 4.dp)
                        .weight(1f)
                        .aspectRatio(1f)
                        .border(2.dp, color = Gigas, shape = WeatherShapes.extraLarge)
                        .clip(WeatherShapes.extraLarge)
                        .background(color = PortGore)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        text = stringResource(id = R.string.feels_like),
                        style = Type.header5,
                        maxLines = 1
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 22.dp),
                        text = feelLikes,
                        style = Type.body7
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 8.dp)
                        .weight(1f)
                        .aspectRatio(1f)
                        .border(2.dp, color = Gigas, shape = WeatherShapes.extraLarge)
                        .clip(WeatherShapes.extraLarge)
                        .background(color = PortGore)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        text = stringResource(id = R.string.uvi),
                        style = Type.header5,
                        maxLines = 1
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 22.dp),
                        text = uviIndex,
                        style = Type.body7
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 4.dp)
                        .weight(1f)
                        .aspectRatio(1f)
                        .border(2.dp, color = Gigas, shape = WeatherShapes.extraLarge)
                        .clip(WeatherShapes.extraLarge)
                        .background(color = PortGore)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        text = stringResource(id = R.string.wind),
                        style = Type.header5,
                        maxLines = 1
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 22.dp),
                        text = wind,
                        style = Type.body7
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 8.dp)
                        .weight(1f)
                        .aspectRatio(1f)
                        .border(2.dp, color = Gigas, shape = WeatherShapes.extraLarge)
                        .clip(WeatherShapes.extraLarge)
                        .background(color = PortGore)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        text = stringResource(id = R.string.humidity),
                        style = Type.header5,
                        maxLines = 1
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 22.dp),
                        text = humidity,
                        style = Type.body7
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 4.dp)
                        .weight(1f)
                        .aspectRatio(1f)
                        .border(2.dp, color = Gigas, shape = WeatherShapes.extraLarge)
                        .clip(WeatherShapes.extraLarge)
                        .background(color = PortGore)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        text = stringResource(id = R.string.visibility),
                        color = Whisper50,
                        style = Type.header5,
                        maxLines = 1
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 22.dp),
                        text = visibility,
                        color = Color.White,
                        style = Type.body7
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 8.dp)
                        .weight(1f)
                        .aspectRatio(1f)
                        .border(2.dp, color = Gigas, shape = WeatherShapes.extraLarge)
                        .clip(WeatherShapes.extraLarge)
                        .background(color = PortGore)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        text = stringResource(id = R.string.pressure),
                        style = Type.header5,
                        maxLines = 1
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 22.dp),
                        text = pressure,
                        style = Type.body7
                    )
                }
            }
        }
    }
}
