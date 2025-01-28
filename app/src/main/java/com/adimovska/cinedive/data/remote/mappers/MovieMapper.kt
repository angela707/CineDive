package com.adimovska.cinedive.data.remote.mappers

import com.adimovska.cinedive.data.local.MovieEntity
import com.adimovska.cinedive.data.remote.AppConfig.IMAGE_BASE_URL
import com.adimovska.cinedive.data.remote.dto.MovieDTO
import com.adimovska.cinedive.data.remote.util.convertDateFormat
import com.adimovska.cinedive.data.remote.util.roundDownTo
import com.adimovska.cinedive.domain.models.MediaItem

fun MovieDTO.toMediaItem(): MediaItem {
    return MediaItem(
        id = this.id,
        title = this.title,
        releaseDate = this.releaseDate?.let { convertDateFormat(it)} ?:  "",
        overview = this.overview,
        backdropPath = "$IMAGE_BASE_URL${this.backdropPath}",
        posterPath = "$IMAGE_BASE_URL${this.posterPath}",
        adult = this.adult,
        voteAverage = this.voteAverage.roundDownTo(1),
    )
}


fun MovieDTO.toMediaItemEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        releaseDate = this.releaseDate?.let { convertDateFormat(it)} ?:  "",
        overview = overview,
        backdropPath = "$IMAGE_BASE_URL${this.backdropPath}",
        posterPath = "$IMAGE_BASE_URL${this.posterPath}",
        adult = adult,
        voteAverage = this.voteAverage.roundDownTo(1),
        voteCount = voteCount,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        popularity = popularity,
        video = video,
        timestamp = System.currentTimeMillis()
    )
}

fun MovieEntity.toMediaItem(): MediaItem {
    return MediaItem(
        id = id,
        title = title,
        releaseDate = releaseDate,
        overview = overview,
        backdropPath = backdropPath,
        posterPath = posterPath,
        adult = adult,
        voteAverage = voteAverage,
    )
}
