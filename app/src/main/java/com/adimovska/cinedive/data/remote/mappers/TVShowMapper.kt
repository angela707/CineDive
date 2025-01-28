package com.adimovska.cinedive.data.remote.mappers

import com.adimovska.cinedive.data.remote.AppConfig.IMAGE_BASE_URL
import com.adimovska.cinedive.data.remote.dto.TVShowDTO
import com.adimovska.cinedive.data.remote.util.convertDateFormat
import com.adimovska.cinedive.data.remote.util.roundDownTo
import com.adimovska.cinedive.domain.models.MediaItem

fun TVShowDTO.toMediaItem(): MediaItem {
    return MediaItem(
        id = this.id,
        title = this.name,
        releaseDate = convertDateFormat(this.firstAirDate),
        overview = this.overview,
        backdropPath = "$IMAGE_BASE_URL${this.backdropPath}",
        posterPath = "$IMAGE_BASE_URL${this.posterPath}",
        adult = this.adult,
        voteAverage = this.voteAverage.roundDownTo(1),
    )
}