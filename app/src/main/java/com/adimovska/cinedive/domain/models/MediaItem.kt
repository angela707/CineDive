package com.adimovska.cinedive.domain.models

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class MediaItem(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val overview: String,
    val backdropPath: String?,
    val posterPath: String?,
    val genreIds: List<String>,
    val adult: Boolean,
    val voteAverage: Double,
    val type: MediaType
)

enum class MediaType {
    MOVIE, TV_SHOW
}