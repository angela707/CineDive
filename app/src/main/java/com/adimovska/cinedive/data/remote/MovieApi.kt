package com.adimovska.cinedive.data.remote

import com.adimovska.cinedive.data.remote.dto.MoviesResponse
import com.adimovska.cinedive.data.remote.dto.TVShowsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("discover/movie")
    suspend fun getMovies(
        @Query("page") page: Int
    ): Response<MoviesResponse>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("page") page: Int,
        @Query("query") searchQuery: String
    ): Response<MoviesResponse>

    @GET("discover/tv")
    suspend fun searchTVShows(
        @Query("page") page: Int,
        @Query("query") searchQuery: String
    ): Response<TVShowsResponse>
}