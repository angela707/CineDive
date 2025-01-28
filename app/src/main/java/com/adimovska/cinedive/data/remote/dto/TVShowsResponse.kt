package com.adimovska.cinedive.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TVShowsResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<TVShowDTO>,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int
)