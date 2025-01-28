package com.adimovska.cinedive.data.remote.mappers

import com.adimovska.cinedive.data.remote.AppConfig.IMAGE_BASE_URL
import com.adimovska.cinedive.data.remote.dto.MovieDTO
import com.adimovska.cinedive.data.remote.util.roundDownTo
import com.adimovska.cinedive.domain.mappers.toMovieGenres
import com.adimovska.cinedive.domain.models.MediaItem
import com.adimovska.cinedive.domain.models.MediaType

fun MovieDTO.toMediaItem(): MediaItem {
    return MediaItem(
        id = this.id,
        title = this.title,
        releaseDate = this.releaseDate ?: "",
        overview = this.overview,
        backdropPath = "$IMAGE_BASE_URL${this.backdropPath}",
        posterPath = "$IMAGE_BASE_URL${this.posterPath}",
        genreIds = this.genreIds?.toMovieGenres() ?: emptyList(),
        adult = this.adult,
        voteAverage = this.voteAverage.roundDownTo(1),
        type = MediaType.MOVIE
    )
}

