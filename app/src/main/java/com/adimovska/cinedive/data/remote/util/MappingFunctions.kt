package com.adimovska.cinedive.data.remote.util

import com.adimovska.cinedive.domain.util.MoviesError
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.pow

fun Double.roundDownTo(decimals: Int): Double {
    val factor = 10.0.pow(decimals)
    return kotlin.math.floor(this * factor) / factor
}

fun mapHttpErrorToMoviesError(code: Int): MoviesError {
    return when (code) {
        in 500..599 -> MoviesError.SERVER_ERROR
        in 400..499 -> MoviesError.CLIENT_ERROR
        else -> MoviesError.UNKNOWN_ERROR
    }
}

fun convertDateFormat(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val date: Date? = inputFormat.parse(inputDate)
    return if (date != null) {
        outputFormat.format(date)
    } else {
        inputDate
    }
}