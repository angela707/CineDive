package com.adimovska.cinedive.domain

import com.adimovska.cinedive.data.remote.dto.MovieDTO

interface MoviesService {
    suspend fun getMovies(page: Int = 1): List<MovieDTO>
    suspend fun searchMovies(page: Int = 1, searchQuery: String): List<MovieDTO>
}