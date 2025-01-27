package com.adimovska.cinedive.domain.models

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val overview: String,
    val backdropPath: String?,
    val posterPath: String?,
    val genreIds: List<Int>,
    val adult: Boolean,
    val voteAverage: Double
) : Parcelable