package com.adimovska.cinedive.presentation.models

import com.adimovska.cinedive.domain.models.MediaType
import com.adimovska.cinedive.domain.util.MoviesError

sealed class MovieEvent {
    data class ChooseMediaType(val type: MediaType) : MovieEvent()
    data class Search(val query: String) : MovieEvent()
    data class OnError(val error: MoviesError) : MovieEvent()
    object StopChoosingMediaType : MovieEvent()
    object OpenMediaTypeDropDown : MovieEvent()
    object ClearSearch : MovieEvent()
    object OnErrorSeen : MovieEvent()
}