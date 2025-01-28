package com.adimovska.cinedive.data.remote.paging_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.adimovska.cinedive.data.remote.MovieApi
import com.adimovska.cinedive.data.remote.mappers.toMediaItem
import com.adimovska.cinedive.data.remote.util.mapHttpErrorToMoviesError
import com.adimovska.cinedive.domain.models.MediaItem
import com.adimovska.cinedive.domain.util.MoviesError
import com.adimovska.cinedive.domain.util.MoviesException

class SearchMoviesPagingSource(
    private val api: MovieApi,
    private val searchQuery: String
) : PagingSource<Int, MediaItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
        val page = params.key ?: 1
        return try {
            val response = api.searchMovies(page, searchQuery)
            if (response.isSuccessful) {
                val totalPages = response.body()?.totalPages
                val movies = response.body()?.results?.map { it.toMediaItem() } ?: emptyList()
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (page == totalPages) null else page + 1
                )
            } else {
                LoadResult.Error(MoviesException(mapHttpErrorToMoviesError(response.code())))
            }
        } catch (e: Exception) {
            LoadResult.Error(MoviesException(MoviesError.SERVICE_UNAVAILABLE))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
