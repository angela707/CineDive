package com.adimovska.cinedive.presentation.models

import com.adimovska.cinedive.domain.models.MediaType
import com.adimovska.cinedive.domain.util.MoviesError

data class HomeUiState(
    val isSearching: Boolean = false,
    val isChoosingMedia: Boolean = false,
    val searchQuery: String = "",
    val error: MoviesError? = null,
    val selectedMedia: MediaType = MediaType.MOVIE,
)