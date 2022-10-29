package com.kukhtoslava.weatherapp.android.ui.search

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kukhtoslava.weatherapp.android.R
import com.kukhtoslava.weatherapp.android.coreui.Rhino
import com.kukhtoslava.weatherapp.android.coreui.Type
import com.kukhtoslava.weatherapp.android.coreui.Whisper50
import com.kukhtoslava.weatherapp.android.coreui.mainBrush
import com.kukhtoslava.weatherapp.android.utils.rememberFlowWithLifecycle
import com.kukhtoslava.weatherapp.domain.models.places.Prediction
import com.kukhtoslava.weatherapp.presentation.search.SearchAction
import com.kukhtoslava.weatherapp.presentation.search.SearchEvent
import com.kukhtoslava.weatherapp.presentation.search.SearchState
import com.kukhtoslava.weatherapp.presentation.search.SearchViewModel
import kotlinx.coroutines.flow.Flow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchScreen(
    navHostController: NavHostController,
    viewModel: SearchViewModel
) {

    val context = LocalContext.current

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val eventFlow = rememberFlowWithLifecycle(flow = viewModel.eventFlow)

    BackHandler(enabled = true) {
        if (state.isExistCurrentPlace) {
            navHostController.popBackStack()
        } else {
            (context as? Activity)?.finish()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.dispatch(action = SearchAction.CheckCurrentPlace)
    }

    SearchUI(
        state = state,
        eventFlow = eventFlow,
        navHostController = navHostController,
        search = { query ->
            viewModel.dispatch(SearchAction.Search(term = query))
        },
        clear = {
            viewModel.dispatch(SearchAction.Clear)
        },
        cityClicked = { placeId, placeName ->
            viewModel.dispatch(
                SearchAction.ClickCity(
                    placeName = placeName,
                    placeId = placeId
                )
            )
        },
        currentLocationClicked = {
            viewModel.dispatch(SearchAction.ClickLocation)
        },
        closeClicked = {
            viewModel.dispatch(SearchAction.CloseScreen)
        },
        retryClicked = {
            viewModel.dispatch(SearchAction.ClickLocation)
        },
        openSettingsClicked = {
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", context.packageName, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        },
        openLocationSettingsClicked = {
            val intent = Intent().apply {
                action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUI(
    state: SearchState,
    eventFlow: Flow<SearchEvent>,
    navHostController: NavHostController,
    search: (String) -> Unit,
    clear: () -> Unit,
    cityClicked: (placeId: String, placeName: String) -> Unit,
    currentLocationClicked: () -> Unit,
    closeClicked: () -> Unit,
    retryClicked: () -> Unit,
    openSettingsClicked: () -> Unit,
    openLocationSettingsClicked: () -> Unit
) {

    val context = LocalContext.current

    var deniedLocationDialog by remember { mutableStateOf(false) }
    var deniedAlwaysLocationDialog by remember { mutableStateOf(false) }
    var locationDisabledDialog by remember { mutableStateOf(false) }
    var unknownErrorLocationDialog: String? by remember { mutableStateOf(null) }

    LaunchedEffect(eventFlow) {
        eventFlow.collect { event ->
            when (event) {
                is SearchEvent.Close -> {
                    navHostController.popBackStack()
                }
                SearchEvent.DeniedAlwaysMessage -> deniedAlwaysLocationDialog = true
                SearchEvent.DeniedMessage -> deniedLocationDialog = true
                is SearchEvent.ErrorMessage -> {
                    unknownErrorLocationDialog =
                        event.message ?: context.getString(R.string.unknown_error)
                }
                SearchEvent.DisabledMessage -> locationDisabledDialog = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = mainBrush
            )
            .statusBarsPadding()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (state.isExistCurrentPlace) {
                Text(
                    modifier = Modifier
                        .clickable { closeClicked() }
                        .padding(16.dp),
                    text = stringResource(id = R.string.close),
                    style = Type.body3
                )
            } else {
                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = "",
                    style = Type.body3
                )
            }
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                SearchAppBar(
                    searchValue = { query ->
                        search(query)
                    },
                    clearValue = {
                        clear()
                    }
                )
            },
            content = {
                Box {
                    if (state.isLoading) {
                        LoadingIndicator()
                    } else if (state.error != null) {
                        ErrorMessage(
                            errorMessage = state.error?.message ?: "Unknown error"
                        )
                    } else if (state.predictions.isEmpty() && state.term.isNotEmpty()) {
                        EmptyList()
                    } else if (state.predictions.isEmpty() && state.term.isEmpty()) {
                        StartSearchDescription(
                            currentLocationClicked = currentLocationClicked
                        )
                    } else {
                        PredictionsList(
                            predictions = state.predictions,
                            cityClicked = { placeId, placeName ->
                                cityClicked(
                                    placeId,
                                    placeName
                                )
                            }
                        )
                    }

                    if (deniedLocationDialog) {
                        DeniedLocationDialog(
                            closeClicked = { deniedLocationDialog = false },
                            retryClicked = {
                                deniedLocationDialog = false
                                retryClicked()
                            }
                        )
                    }

                    if (deniedAlwaysLocationDialog) {
                        DeniedAlwaysLocationDialog(
                            closeClicked = { deniedAlwaysLocationDialog = false },
                            openSettingsClicked = {
                                deniedAlwaysLocationDialog = false
                                openSettingsClicked()
                            }
                        )
                    }

                    if (unknownErrorLocationDialog != null) {
                        UnknownErrorLocationDialog(
                            errorMessage = unknownErrorLocationDialog!!,
                            closeClicked = { unknownErrorLocationDialog = null }
                        )
                    }

                    if (locationDisabledDialog) {
                        LocationDisableDialog(
                            closeClicked = { locationDisabledDialog = false },
                            openLocationSettingsClicked = {
                                locationDisabledDialog = false
                                openLocationSettingsClicked()
                            }
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun LoadingIndicator(
) {
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
private fun EmptyList(
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = stringResource(id = R.string.empty_result_search),
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
    }
}

@Composable
private fun StartSearchDescription(
    currentLocationClicked: () -> Unit
) {

    val mAnnotatedLinkString = buildAnnotatedString {

        val mStr = stringResource(id = R.string.description_search_start)
        val mEnd = stringResource(id = R.string.description_location_end)
        val mFull = "$mStr $mEnd"
        val mStartIndex = mFull.indexOf(mEnd)
        val mLetter = mEnd.length
        val mEndIndex = mStartIndex + mLetter

        append(mFull)
        addStyle(
            style = SpanStyle(
                color = Whisper50,
                textDecoration = TextDecoration.Underline
            ), start = mStartIndex, end = mEndIndex
        )

        addStringAnnotation(
            tag = "Current location",
            annotation = "Current location",
            start = mStartIndex,
            end = mEndIndex
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ClickableText(
            modifier = Modifier
                .padding(32.dp),
            text = mAnnotatedLinkString,
            style = Type.body4,
            onClick = {
                mAnnotatedLinkString
                    .getStringAnnotations("Current location", it, it)
                    .firstOrNull()?.let { _ ->
                        currentLocationClicked()
                    }
            }
        )
    }
}

@Composable
private fun ErrorMessage(
    errorMessage: String
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
            color = Color.White,
            style = Type.body5,
            maxLines = 1
        )
    }
}

@Composable
private fun PredictionsList(
    predictions: List<Prediction>,
    cityClicked: (placeId: String, placeName: String) -> Unit
) {

    LazyColumn(
        modifier = Modifier.padding(top = 64.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        items(predictions) { prediction ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        cityClicked(
                            prediction.placeId,
                            prediction.description
                        )
                    }
            ) {
                Text(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    text = prediction.description,
                    style = Type.body6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchAppBar(
    searchValue: (String) -> Unit,
    clearValue: () -> Unit
) {
    var query: String by rememberSaveable { mutableStateOf("") }
    var showClearIcon by rememberSaveable { mutableStateOf(false) }

    if (query.isEmpty()) {
        showClearIcon = false
    } else if (query.isNotEmpty()) {
        showClearIcon = true
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp),
        value = query,
        onValueChange = { onQueryChanged ->
            query = onQueryChanged
            searchValue(query)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                tint = Color.White,
                contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (showClearIcon) {
                IconButton(onClick = {
                    query = ""
                    clearValue()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        tint = Color.White,
                        contentDescription = "Clear Icon"
                    )
                }
            }
        },
        maxLines = 1,
        placeholder = { Text(text = stringResource(id = R.string.city)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Rhino,
            containerColor = Rhino,
            placeholderColor = Whisper50
        ),
        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
fun DeniedLocationDialog(
    closeClicked: () -> Unit,
    retryClicked: () -> Unit
) {
    Dialog(onDismissRequest = { closeClicked() }) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.location_permission_title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = Type.body4,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(id = R.string.location_permission_description),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 20.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    style = Type.header4
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                TextButton(onClick = {
                    closeClicked()
                }) {

                    Text(
                        stringResource(id = R.string.close),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        style = Type.body
                    )
                }
                TextButton(onClick = {
                    retryClicked()
                }) {
                    Text(
                        stringResource(id = R.string.retry),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        style = Type.body
                    )
                }
            }
        }
    }
}

@Composable
fun DeniedAlwaysLocationDialog(
    closeClicked: () -> Unit,
    openSettingsClicked: () -> Unit
) {
    Dialog(onDismissRequest = { closeClicked() }) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.location_permission_title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = Type.body4,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(id = R.string.location_permission_description),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 20.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    style = Type.header4
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                TextButton(onClick = {
                    closeClicked()
                }) {

                    Text(
                        stringResource(id = R.string.close),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        style = Type.body
                    )
                }
                TextButton(onClick = {
                    openSettingsClicked()
                }) {
                    Text(
                        stringResource(id = R.string.open_settings),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        style = Type.body
                    )
                }
            }
        }
    }
}

@Composable
fun LocationDisableDialog(
    openLocationSettingsClicked: () -> Unit,
    closeClicked: () -> Unit
) {
    Dialog(onDismissRequest = { closeClicked() }) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.location_disabled),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = Type.body4,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(id = R.string.location_disabled_description),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 20.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    style = Type.header4
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                TextButton(onClick = {
                    closeClicked()
                }) {

                    Text(
                        stringResource(id = R.string.close),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        style = Type.body
                    )
                }
                TextButton(onClick = {
                    openLocationSettingsClicked()
                }) {
                    Text(
                        stringResource(id = R.string.open_settings),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        style = Type.body
                    )
                }
            }
        }
    }
}

@Composable
fun UnknownErrorLocationDialog(
    errorMessage: String,
    closeClicked: () -> Unit
) {
    Dialog(onDismissRequest = { closeClicked() }) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.unknown_error),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = Type.body4,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = errorMessage,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 20.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    style = Type.header4
                )
            }
            TextButton(
                onClick = { closeClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {

                Text(
                    stringResource(id = R.string.close),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                    style = Type.body
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SearchScreenPreview() {
    val context = LocalContext.current
    SearchScreen(
        navHostController = NavHostController(context),
        viewModel = viewModel()
    )
}
