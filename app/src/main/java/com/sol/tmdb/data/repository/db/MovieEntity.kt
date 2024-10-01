package com.sol.tmdb.data.repository.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String,
    val voteAverage: Double,
    val isFavorite: Boolean = false,
    val isInWatchlist: Boolean = false,
    val isWatched: Boolean = false
)
