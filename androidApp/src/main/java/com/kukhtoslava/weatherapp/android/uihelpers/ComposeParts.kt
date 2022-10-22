package com.kukhtoslava.weatherapp.android.uihelpers

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kukhtoslava.weatherapp.android.coreui.Gigas20
import com.kukhtoslava.weatherapp.android.coreui.Type
import com.kukhtoslava.weatherapp.android.coreui.Whisper20

@Composable
fun WeatherPartItem(
    header: String,
    @DrawableRes id: Int,
    temperature: String
) {

    Box(
        modifier = Modifier
            .height(160.dp)
            .width(80.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Gigas20)
            .border(1.dp, Whisper20, RoundedCornerShape(50.dp))
    ) {
        Column(
            modifier = Modifier
                .align(alignment = Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .wrapContentSize(),
                text = header,
                style = Type.body12,
                maxLines = 1
            )
            Image(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                painter = painterResource(id = id),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )
            Text(
                text = temperature,
                style = Type.body2,
                maxLines = 1
            )
        }
    }
}
