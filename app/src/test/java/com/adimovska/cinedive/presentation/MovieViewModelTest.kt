package com.adimovska.cinedive.presentation

import androidx.paging.PagingData
import com.adimovska.cinedive.CoroutineProvider
import com.adimovska.cinedive.CoroutineTest
import com.adimovska.cinedive.domain.models.MediaItem
import com.adimovska.cinedive.domain.models.MediaType
import com.adimovska.cinedive.domain.repository.MediaRepository
import com.adimovska.cinedive.domain.util.MoviesError
import com.adimovska.cinedive.presentation.models.MovieEvent
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MovieViewModelTest : CoroutineTest {
    override lateinit var testCoroutineProvider: CoroutineProvider
    override lateinit var testScope: TestScope


    private val pagingDataFlow = flowOf(PagingData.from(movieList))
    private val mockRepository = mockk<MediaRepository>(relaxed = true) {
        every { getMovies() } returns pagingDataFlow
    }

    private lateinit var viewModel: MovieViewModel

    companion object {
        private val movie = MediaItem(
            id = 1,
            title = "Sonic the Hedgehog 3",
            releaseDate = "2024-12-19",
            overview = "Sonic, Knuckles, and Tails reunite against a powerful new adversary...",
            backdropPath = "https://image.tmdb.org/t/p/original/b85bJfrTOSJ7M5Ox0yp4lxIxdG1.jpg",
            posterPath = "https://image.tmdb.org/t/p/original/d8Ryb8AunYAuycVKDp5HpdWPKgC.jpg",
            adult = false,
            voteAverage = 8.9,
        )
        private val movie2 = MediaItem(
            id = 539972,
            title = "Kraven the Hunter",
            releaseDate = "2024-12-11",
            overview = "Kraven Kravinoff's complex relationship with his ruthless gangster father, Nikolai, starts him down a path of vengeance with brutal consequences, motivating him to become not only the greatest hunter in the world, but also one of its most feared.",
            backdropPath = "https://image.tmdb.org/t/p/original/v9Du2HC3hlknAvGlWhquRbeifwW.jpg",
            posterPath = "https://image.tmdb.org/t/p/original/i47IUSsN126K11JUzqQIOi1Mg1M.jpg",
            adult = false,
            voteAverage = 5.5,
        )
        private val movie3 = MediaItem(
            id = 993710,
            title = "Back in Action",
            releaseDate = "2025-01-15",
            overview = "Fifteen years after vanishing from the CIA to start a family, elite spies Matt and Emily jump back into the world of espionage when their cover is blown.",
            backdropPath = "https://image.tmdb.org/t/p/original/xZm5YUNY3PlYD1Q4k7X8zd2V4AK.jpg",
            posterPath = "https://image.tmdb.org/t/p/original/3L3l6LsiLGHkTG4RFB2aBA6BttB.jpg",
            adult = false,
            voteAverage = 3.2,
        )

        private val movieList = listOf(movie, movie2, movie3)

    }

    @BeforeEach
    fun setUp() {
        viewModel = MovieViewModel(
            repository = mockRepository
        )
    }

    //todo find a way to access paging data values
    @Test
    fun `test fetchMovies is called during initialization and populates uiState`() = runTest {
        val uiState = viewModel.uiMediaState.first()
        coVerify(exactly = 1) { mockRepository.getMovies() }
//
//        viewModel.uiMediaState.test {
//            assertEquals(PagingData.from(movieList), awaitItem())
//        }
    }

    @Test
    fun `test initial state`() = runTest {
        val initialState = viewModel.state.first()

        assertFalse(initialState.isSearching)
        assertFalse(initialState.isChoosingMedia)
        assertNull(initialState.error)
        assertEquals("", initialState.searchQuery)
        assertEquals(MediaType.MOVIE, initialState.selectedMedia)
    }

    @Test
    fun `test search event updates state and calls search`() = runTest {
        val query = "Sonic"
        viewModel.onEvent(MovieEvent.Search(query))

        val updatedState = viewModel.state.first()
        assertTrue(updatedState.isSearching)
        assertEquals(query, updatedState.searchQuery)

        coVerify(exactly = 1) { mockRepository.searchMovies(query) }
    }

    @Test
    fun `test choosing media updates state and calls search`() = runTest {
        val searchQuery = "Sonic"

        viewModel.onEvent(MovieEvent.Search(searchQuery))
        viewModel.onEvent(MovieEvent.ChooseMediaType(MediaType.TV_SHOW))

        val updatedState = viewModel.state.first()

        assertTrue(updatedState.isSearching)
        assertEquals(searchQuery, updatedState.searchQuery)
        assertEquals(MediaType.TV_SHOW, updatedState.selectedMedia)


        coVerify(exactly = 1) { mockRepository.searchMovies(searchQuery) }
        coVerify(exactly = 1) { mockRepository.searchTvShows(searchQuery) }
        //one is triggered from changing the query, the other from changing the type
    }

    //todo find a way to access paging data values
    @Disabled
    @Test
    fun `test clear search resets state and shows cached movies`() = runTest {
        // Trigger a search to update the state

        val cachedMovies = viewModel.uiMediaState.first()
        viewModel.onEvent(MovieEvent.Search("Sonic"))

        // Clear the search
        viewModel.onEvent(MovieEvent.Search(""))

        val updatedState = viewModel.state.first()
        val uiMediaState = viewModel.uiMediaState.first()

        assertEquals("", updatedState.searchQuery)
        assertFalse(updatedState.isSearching)
        assertEquals(cachedMovies, uiMediaState)
    }

    @Test
    fun `test error handling updates state`() = runTest {
        val error = MoviesError.SERVICE_UNAVAILABLE
        viewModel.onEvent(MovieEvent.OnError(error))

        val updatedState = viewModel.state.first()
        assertEquals(error, updatedState.error)
    }

    @Test
    fun `test on error seen clears error`() = runTest {
        viewModel.onEvent(MovieEvent.OnError(MoviesError.CLIENT_ERROR))

        viewModel.onEvent(MovieEvent.OnErrorSeen)

        val updatedState = viewModel.state.first()
        assertNull(updatedState.error)
    }
}