package com.adimovska.cinedive.domain.repository

import androidx.paging.PagingData
import com.adimovska.cinedive.domain.models.MediaItem
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getMovies(): Flow<PagingData<MediaItem>>
    fun searchMovies(searchQuery: String): Flow<PagingData<MediaItem>>
    fun searchTvShows(searchQuery: String): Flow<PagingData<MediaItem>>
}