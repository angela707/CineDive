package com.adimovska.cinedive.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.adimovska.cinedive.R
import com.adimovska.cinedive.domain.models.MediaItem
import com.adimovska.cinedive.domain.models.MediaType
import com.adimovska.cinedive.domain.util.MoviesError
import com.adimovska.cinedive.domain.util.MoviesException
import com.adimovska.cinedive.presentation.components.MediaDropDown
import com.adimovska.cinedive.presentation.components.MovieCard
import com.adimovska.cinedive.presentation.components.SearchBar
import com.adimovska.cinedive.presentation.destinations.MovieDetailsScreenDestination
import com.adimovska.cinedive.presentation.models.MovieEvent
import com.adimovska.cinedive.presentation.navigation.HomeGraph
import com.adimovska.cinedive.presentation.preview_providers.MoviesPreviewProvider
import com.adimovska.cinedive.presentation.preview_providers.createMockLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.ImmutableList


@HomeGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: DestinationsNavigator,
    viewModel: MovieViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val movies = viewModel.uiMediaState.collectAsLazyPagingItems()

    val loadState = movies.loadState
    LaunchedEffect(loadState) {
        loadState.handleErrors { error ->
            viewModel.onEvent(MovieEvent.OnError(error))
        }
    }

    LaunchedEffect(key1 = state.error) {
        val message = when (state.error) {
            MoviesError.SERVICE_UNAVAILABLE -> context.getString(R.string.error_service_unavailable)
            MoviesError.CLIENT_ERROR -> context.getString(R.string.client_error)
            MoviesError.SERVER_ERROR -> context.getString(R.string.server_error)
            MoviesError.UNKNOWN_ERROR -> context.getString(R.string.unknown_error)
            else -> null
        }

        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.onEvent(MovieEvent.OnErrorSeen)
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background

    ) { paddingValues ->
        HomeScreenContent(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            mediaItemList = movies,
            onEvent = viewModel::onEvent,
            isSearching = state.isSearching,
            selectedMediaType = state.selectedMedia,
            isChoosingMedia = state.isChoosingMedia,
            searchQuery = state.searchQuery,
            onCardSelected = { movie ->
                navController.navigate(
                    MovieDetailsScreenDestination(
                        mediaItem = movie
                    )
                )
            }
        )
    }
}

private fun CombinedLoadStates.handleErrors(onError: (MoviesError) -> Unit) {
    listOf(refresh, append).forEach { state ->
        if (state is LoadState.Error) {
            val exception = state.error as? MoviesException
            val error = exception?.error ?: MoviesError.UNKNOWN_ERROR
            onError(error)
        }
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    mediaItemList: LazyPagingItems<MediaItem>,
    isChoosingMedia: Boolean,
    isSearching: Boolean,
    selectedMediaType: MediaType,
    searchQuery: String,
    onCardSelected: (MediaItem) -> Unit,
    onEvent: (MovieEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        SearchBar(
            input = searchQuery,
            hint = stringResource(R.string.search_bar_hint),
            trailingContent = {
                MediaDropDown(
                    selectedType = selectedMediaType,
                    isOpen = isChoosingMedia,
                    onClick = {
                        onEvent(MovieEvent.OpenMediaTypeDropDown)
                    },
                    onDismiss = {
                        onEvent(MovieEvent.StopChoosingMediaType)
                    },
                    onSelectType = {
                        onEvent(MovieEvent.ChooseMediaType(it))
                    }
                )
            },
            onSearch = {
                onEvent(MovieEvent.Search(it))
            }
        )

        if (isSearching) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            MovieList(
                mediaItemList = mediaItemList,
                onCardSelected = onCardSelected
            )
        }
    }
}

@Composable
fun MovieList(
    modifier: Modifier = Modifier,
    mediaItemList: LazyPagingItems<MediaItem>,
    onCardSelected: (MediaItem) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(mediaItemList.itemCount) { index ->
            val movie = mediaItemList[index]
            if (movie != null) {
                MovieCard(
                    title = movie.title,
                    releaseDate = movie.releaseDate ?: "",
                    overview = movie.overview,
                    backdropPath = movie.backdropPath,
                    onClick = { onCardSelected(movie) }
                )
            }
        }
    }
}


@Preview
@Composable
private fun HomeScreenContentPreview(
    @PreviewParameter(MoviesPreviewProvider::class) mediaItemList: ImmutableList<MediaItem>
) {
    val lazyPagingItems = createMockLazyPagingItems(mediaItemList)

    HomeScreenContent(
        mediaItemList = lazyPagingItems,
        isChoosingMedia = false,
        isSearching = false,
        selectedMediaType = MediaType.MOVIE,
        onCardSelected = { },
        onEvent = { },
        searchQuery = ""
    )
}

@Preview
@Composable
private fun MovieListPreview(
    @PreviewParameter(MoviesPreviewProvider::class) mediaItemList: ImmutableList<MediaItem>
) {
    val lazyPagingItems = createMockLazyPagingItems(mediaItemList)
    MovieList(
        mediaItemList = lazyPagingItems,
        onCardSelected = {}
    )
}