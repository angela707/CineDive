package com.adimovska.cinedive.presentation.preview_providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.adimovska.cinedive.domain.models.Movie
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class MoviesPreviewProvider : PreviewParameterProvider<ImmutableList<Movie>> {
    override val values: Sequence<ImmutableList<Movie>>
        get() = sequenceOf(
            listOf(
                Movie(
                    id = 1,
                    title = "Sonic the Hedgehog 3",
                    releaseDate = "2024-12-19",
                    overview = "Sonic, Knuckles, and Tails reunite against a powerful new adversary...",
                    backdropPath = "https://image.tmdb.org/t/p/original/b85bJfrTOSJ7M5Ox0yp4lxIxdG1.jpg",
                    posterPath = "https://image.tmdb.org/t/p/original/d8Ryb8AunYAuycVKDp5HpdWPKgC.jpg",
                    adult = false,
                    genreIds = listOf(3, 4, 6),
                    voteAverage = 8.9
                ),
                Movie(
                    id = 539972,
                    title = "Kraven the Hunter",
                    releaseDate = "2024-12-11",
                    overview = "Kraven Kravinoff's complex relationship with his ruthless gangster father, Nikolai, starts him down a path of vengeance with brutal consequences, motivating him to become not only the greatest hunter in the world, but also one of its most feared.",
                    backdropPath = "https://image.tmdb.org/t/p/original/v9Du2HC3hlknAvGlWhquRbeifwW.jpg",
                    posterPath = "https://image.tmdb.org/t/p/original/i47IUSsN126K11JUzqQIOi1Mg1M.jpg",
                    genreIds = listOf(28, 878, 12, 14, 53),
                    adult = false,
                    voteAverage = 5.5
                ),
                Movie(
                    id = 993710,
                    title = "Back in Action",
                    releaseDate = "2025-01-15",
                    overview = "Fifteen years after vanishing from the CIA to start a family, elite spies Matt and Emily jump back into the world of espionage when their cover is blown.",
                    backdropPath = "https://image.tmdb.org/t/p/original/xZm5YUNY3PlYD1Q4k7X8zd2V4AK.jpg",
                    posterPath = "https://image.tmdb.org/t/p/original/3L3l6LsiLGHkTG4RFB2aBA6BttB.jpg",
                    genreIds = listOf(28, 35),
                    adult = false,
                    voteAverage = 3.2
                )
            ).toImmutableList()
        )
}