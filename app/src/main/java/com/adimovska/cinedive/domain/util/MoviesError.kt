package com.adimovska.cinedive.domain.util

enum class MoviesError {
    SERVICE_UNAVAILABLE,
    CLIENT_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR
}

class MoviesException(val error: MoviesError) : Exception(
    "An error occurred when translating: $error"
)