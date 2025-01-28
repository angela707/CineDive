package com.adimovska.cinedive.presentation.models

import androidx.paging.PagingData
import com.adimovska.cinedive.domain.models.MediaItem
import com.adimovska.cinedive.domain.models.MediaType
import com.adimovska.cinedive.domain.util.MoviesError

data class HomeUiState(
    val isSearching: Boolean = false,
    val isChoosingMedia: Boolean = false,
    val searchQuery: String = "",
    val error: MoviesError? = null,
    val selectedMedia: MediaType = MediaType.MOVIE,
    val mediaItems: PagingData<MediaItem> = PagingData.empty()
)