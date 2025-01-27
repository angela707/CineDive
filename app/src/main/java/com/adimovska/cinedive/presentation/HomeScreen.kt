package com.adimovska.cinedive.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.adimovska.cinedive.R
import com.adimovska.cinedive.domain.models.Movie
import com.adimovska.cinedive.presentation.components.MovieCard
import com.adimovska.cinedive.presentation.components.SearchBar
import com.adimovska.cinedive.presentation.destinations.MovieDetailsScreenDestination
import com.adimovska.cinedive.presentation.navigation.HomeGraph
import com.adimovska.cinedive.presentation.preview_providers.MoviesPreviewProvider
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList


@HomeGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: DestinationsNavigator
) {

    //todo remove once the api is implemented
    val movies = listOf(
        Movie(
            id = 1,
            title = "Sonic the Hedgehog 3 and some extra to make a long title",
            releaseDate = "Dec 19 2024",
            overview = "Sonic, Knuckles, and Tails reunite against a powerful new adversary...",
            backdropPath = "https://image.tmdb.org/t/p/original/b85bJfrTOSJ7M5Ox0yp4lxIxdG1.jpg",
            posterPath = "https://image.tmdb.org/t/p/original/d8Ryb8AunYAuycVKDp5HpdWPKgC.jpg",
            adult = false,
            genreIds = listOf(3, 4, 6),
            voteAverage = 5.4
        ),
        Movie(
            id = 539972,
            title = "Kraven the Hunter",
            releaseDate = "Dec 11 2024",
            overview = "Kraven Kravinoff's complex relationship with his ruthless gangster father, Nikolai, starts him down a path of vengeance with brutal consequences, motivating him to become not only the greatest hunter in the world, but also one of its most feared.",
            backdropPath = "https://image.tmdb.org/t/p/original/v9Du2HC3hlknAvGlWhquRbeifwW.jpg",
            posterPath = "https://image.tmdb.org/t/p/original/i47IUSsN126K11JUzqQIOi1Mg1M.jpg",
            genreIds = listOf(28, 878, 12, 14, 53),
            adult = false,
            voteAverage = 3.2
        ),
        Movie(
            id = 993710,
            title = "Back in Action",
            releaseDate = "Jan 15 2025",
            overview = "Fifteen years after vanishing from the CIA to start a family, elite spies Matt and Emily jump back into the world of espionage when their cover is blown.",
            backdropPath = "https://image.tmdb.org/t/p/original/xZm5YUNY3PlYD1Q4k7X8zd2V4AK.jpg",
            posterPath = "https://image.tmdb.org/t/p/original/3L3l6LsiLGHkTG4RFB2aBA6BttB.jpg",
            genreIds = listOf(28, 35),
            adult = true,
            voteAverage = 8.7
        )
    )

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background

    ) { paddingValues ->
        HomeScreenContent(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            movieList = movies.toPersistentList(),
            onSearch = {},
            onCardSelected = { movie ->
                navController.navigate(
                    MovieDetailsScreenDestination(
                        movie = movie
                    )
                )
            }
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    movieList: ImmutableList<Movie>,
    onSearch: (String) -> Unit,
    onCardSelected: (Movie) -> Unit
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        SearchBar(
            hint = stringResource(R.string.search_bar_hint),
            onSearch = onSearch
        )

        MovieList(
            movieList = movieList,
            onCardSelected = onCardSelected
        )
    }
}

@Composable
fun MovieList(
    modifier: Modifier = Modifier,
    movieList: ImmutableList<Movie>,
    onCardSelected: (Movie) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = movieList) { movie ->
            MovieCard(
                title = movie.title,
                releaseDate = movie.releaseDate,
                overview = movie.overview,
                backdropPath = movie.backdropPath,
                onClick = { onCardSelected(movie) }
            )
        }
    }
}


@Preview
@Composable
private fun HomeScreenContentPreview(
    @PreviewParameter(MoviesPreviewProvider::class) movieList: ImmutableList<Movie>
) {
    HomeScreenContent(
        movieList = movieList,
        onSearch = { },
        onCardSelected = { }
    )
}

@Preview
@Composable
private fun MovieListPreview(
    @PreviewParameter(MoviesPreviewProvider::class) movieList: ImmutableList<Movie>
) {
    MovieList(
        movieList = movieList,
        onCardSelected = {}
    )
}