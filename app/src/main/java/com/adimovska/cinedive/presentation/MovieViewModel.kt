package com.adimovska.cinedive.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.adimovska.cinedive.domain.models.MediaItem
import com.adimovska.cinedive.domain.models.MediaType
import com.adimovska.cinedive.domain.repository.MediaRepository
import com.adimovska.cinedive.presentation.models.HomeUiState
import com.adimovska.cinedive.presentation.models.MovieEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MediaRepository,
) : ViewModel() {

    private val _uiMediaState = MutableStateFlow<PagingData<MediaItem>>(PagingData.empty())
    val uiMediaState: StateFlow<PagingData<MediaItem>> = _uiMediaState.asStateFlow()

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    // Cache the fetched movies to avoid re-fetching
    private var cachedMovies: PagingData<MediaItem> = PagingData.empty()

    init {
        fetchMovies()
    }

    private fun fetchMovies() = viewModelScope.launch {
        repository.getMovies()
            .cachedIn(viewModelScope)
            .collect { pagingData ->
                _uiMediaState.value = pagingData
                _state.update {
                    it.copy(
                        isSearching = false,
                        error = null // Clear any previous error when data is fetched successfully
                    )
                }
            }
    }


    @OptIn(FlowPreview::class)
    private fun searchMedia(type: MediaType, query: String) = viewModelScope.launch {
        if (query.isBlank() || query.isEmpty()) {
            // If the search query is cleared, reset to cached movies
            _uiMediaState.value = cachedMovies
            _state.update {
                it.copy(
                    isSearching = false,
                    error = null
                )
            }
            return@launch
        }

        when (type) {
            MediaType.MOVIE -> repository.searchMovies(query)
            MediaType.TV_SHOW -> repository.searchTvShows(query)
        }.cachedIn(viewModelScope)
            .debounce(500).collect { pagingData ->
                _uiMediaState.value = pagingData
                _state.update {
                    it.copy(
                        isSearching = false,
                        error = null
                    )
                }
            }
    }

    private fun triggerSearch() {
        val type = _state.value.selectedMedia
        val query = _state.value.searchQuery
        searchMedia(type, query)
    }

    fun onEvent(event: MovieEvent) {
        when (event) {
            is MovieEvent.ChooseMediaType -> {
                _state.update {
                    it.copy(
                        selectedMedia = event.type,
                        isChoosingMedia = false,
                        isSearching = true
                    )
                }
                // Trigger search on media type selection, close dropdown menu, show progress indicator
                triggerSearch()
            }

            is MovieEvent.Search -> {
                _state.update {
                    it.copy(
                        searchQuery = event.query,
                        isSearching = true
                    )
                }
                // Trigger search on search query input, show progress indicator
                triggerSearch()
            }

            MovieEvent.ClearSearch -> {
                _state.update {
                    it.copy(searchQuery = "")
                }
                // Reset UI state to cached movies
                _uiMediaState.value = cachedMovies
            }

            MovieEvent.OnErrorSeen -> {
                // Clear the errors
                _state.update {
                    it.copy(
                        error = null
                    )
                }
            }

            MovieEvent.OpenMediaTypeDropDown -> {
                _state.update {
                    it.copy(
                        isChoosingMedia = true
                    )
                }
            }

            MovieEvent.StopChoosingMediaType -> {
                _state.update {
                    it.copy(
                        isChoosingMedia = false
                    )
                }
            }

            is MovieEvent.OnError ->  {
                _state.update {
                    it.copy(
                        error = event.error
                    )
                }
            }
        }
    }
}
