package com.adimovska.cinedive.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val tableId : Int = 0,
    @ColumnInfo val id: Int,
    @ColumnInfo val adult: Boolean,
    @ColumnInfo val backdropPath: String?,
    @ColumnInfo val originalLanguage: String,
    @ColumnInfo val originalTitle: String,
    @ColumnInfo val overview: String,
    @ColumnInfo val popularity: Double,
    @ColumnInfo val posterPath: String?,
    @ColumnInfo val releaseDate: String?,
    @ColumnInfo val title: String,
    @ColumnInfo val video: Boolean,
    @ColumnInfo val voteAverage: Double,
    @ColumnInfo val voteCount: Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP") val timestamp: Long
)

