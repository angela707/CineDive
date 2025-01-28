package com.adimovska.cinedive.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.adimovska.cinedive.data.local.MovieDatabase
import com.adimovska.cinedive.data.remote.AppConfig.PAGE_SIZE
import com.adimovska.cinedive.data.remote.MovieApi
import com.adimovska.cinedive.data.remote.mappers.toMediaItem
import com.adimovska.cinedive.data.remote.paging_sources.MoviesRemoteMediator
import com.adimovska.cinedive.data.remote.paging_sources.SearchMoviesPagingSource
import com.adimovska.cinedive.data.remote.paging_sources.SearchTvShowsPagingSource
import com.adimovska.cinedive.domain.models.MediaItem
import com.adimovska.cinedive.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaRepositoryImpl(
    private val api: MovieApi,
    private val movieDatabase: MovieDatabase
) : MediaRepository {

    //region Get Movies
    //Fetch the movies from the database, 20 at a time.
    //The mediator will handle fetching from the API and storing in the database
    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(): Flow<PagingData<MediaItem>> {
        val pagingSourceFactory = { movieDatabase.movieDao.getAllMovies() }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = MoviesRemoteMediator(api, movieDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map {
                it.toMediaItem()
            }
        }
    }
    //endregion

    //region Search Movies
    override fun searchMovies(searchQuery: String): Flow<PagingData<MediaItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchMoviesPagingSource(api, searchQuery) }
        ).flow
    }

    //endregion

    //region Search TvShows
    override fun searchTvShows(searchQuery: String): Flow<PagingData<MediaItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchTvShowsPagingSource(api, searchQuery) }
        ).flow
    }
    //endregion
}