package com.adimovska.cinedive.data.remote.paging_sources

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.adimovska.cinedive.data.local.MovieDatabase
import com.adimovska.cinedive.data.local.MovieEntity
import com.adimovska.cinedive.data.remote.AppConfig.FIST_PAGE
import com.adimovska.cinedive.data.remote.AppConfig.PAGE_SIZE
import com.adimovska.cinedive.data.remote.MovieApi
import com.adimovska.cinedive.data.remote.mappers.toMediaItemEntity
import com.adimovska.cinedive.data.remote.util.mapHttpErrorToMoviesError
import com.adimovska.cinedive.domain.util.MoviesError
import com.adimovska.cinedive.domain.util.MoviesException
import timber.log.Timber
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        //Add a timeout of 6 hours.
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(6, TimeUnit.HOURS)
        return if (System.currentTimeMillis() - movieDatabase.movieDao.lastUpdated() >= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            // Determine the page to load
            val page = when (loadType) {
                LoadType.REFRESH -> FIST_PAGE
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val moviesCount = movieDatabase.withTransaction {
                        movieDatabase.movieDao.countAllMovies()
                    }
                    // Calculate the current page
                    val currentPage = (moviesCount / PAGE_SIZE) + 1

                    // Check if there are remaining movies to fetch
                    val isEndOfPagination = moviesCount % PAGE_SIZE != 0

                    if (isEndOfPagination) {
                        Timber.d("=+= End of pagination reached.")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        currentPage
                    }
                }
            }

            // Fetch data from the API
            val response = movieApi.getMovies(page)

            if (response.isSuccessful) {
                val movies = response.body()?.results?.map { it.toMediaItemEntity() } ?: emptyList()
                // Save data to the database
                movieDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        movieDatabase.movieDao.clearAll()
                    }
                    movieDatabase.movieDao.insertAll(movies)
                }

                MediatorResult.Success(endOfPaginationReached = movies.isEmpty())
            } else {
                MediatorResult.Error(MoviesException(mapHttpErrorToMoviesError(response.code())))
            }
        } catch (e: Exception) {
            MediatorResult.Error(MoviesException(MoviesError.SERVICE_UNAVAILABLE))
        }
    }
}
