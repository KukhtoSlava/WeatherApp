package com.kukhtoslava.weatherapp.android.utils

import android.annotation.SuppressLint
import com.kukhtoslava.weatherapp.android.R
import java.text.SimpleDateFormat
import java.util.*

private const val MILLIS_IN_SECONDS = 1000

@SuppressLint("SimpleDateFormat")
fun convertSecondsToHourString(dt: Int): String {
    val millis = dt.toLong() * MILLIS_IN_SECONDS
    val date = Date(millis)
    val format = SimpleDateFormat("h a", Locale.ENGLISH)
    return format.format(date)
}

@SuppressLint("SimpleDateFormat")
fun convertSecondsToDaysString(dt: Int): String {
    val millis = dt.toLong() * MILLIS_IN_SECONDS
    val date = Date(millis)
    val format = SimpleDateFormat("EEE", Locale.ENGLISH)
    return format.format(date)
}

@SuppressLint("SimpleDateFormat")
fun convertSecondsToHoursMinutesString(dt: Int): String {
    val millis = dt.toLong() * MILLIS_IN_SECONDS
    val date = Date(millis)
    val format = SimpleDateFormat("h:mm a", Locale.ENGLISH)
    return format.format(date)
}

fun convertIconsToResources(icon: String): Int {
    return when (icon) {
        "01d" -> R.drawable.clear_day
        "01n" -> R.drawable.clear_night
        "02d" -> R.drawable.partly_cloudy_day
        "02n" -> R.drawable.partly_cloudy_night
        "03d" -> R.drawable.cloudy
        "03n" -> R.drawable.cloudy
        "04d" -> R.drawable.overcast
        "04n" -> R.drawable.overcast
        "09d" -> R.drawable.heavy_showers
        "09n" -> R.drawable.heavy_showers
        "10d" -> R.drawable.showers
        "10n" -> R.drawable.showers
        "11d" -> R.drawable.thunderstorm_showers
        "11n" -> R.drawable.thunderstorm_showers
        "13d" -> R.drawable.snow
        "13n" -> R.drawable.snow
        "50d" -> R.drawable.windy
        "50n" -> R.drawable.windy
        else -> R.drawable.clear_day
    }
}