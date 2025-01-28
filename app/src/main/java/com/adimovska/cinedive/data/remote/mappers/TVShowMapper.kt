package com.adimovska.cinedive.data.remote.mappers

import com.adimovska.cinedive.data.remote.AppConfig.IMAGE_BASE_URL
import com.adimovska.cinedive.data.remote.dto.TVShowDTO
import com.adimovska.cinedive.data.remote.util.roundDownTo
import com.adimovska.cinedive.domain.mappers.toTVGenres
import com.adimovska.cinedive.domain.models.MediaItem
import com.adimovska.cinedive.domain.models.MediaType

fun TVShowDTO.toMediaItem(): MediaItem {
    return MediaItem(
        id = this.id,
        title = this.name,
        releaseDate = this.firstAirDate,
        overview = this.overview,
        backdropPath = "$IMAGE_BASE_URL${this.backdropPath}",
        posterPath = "$IMAGE_BASE_URL${this.posterPath}",
        genreIds = this.genreIds.toTVGenres(),
        adult = this.adult,
        voteAverage = this.voteAverage.roundDownTo(1),
        type = MediaType.TV_SHOW
    )
}