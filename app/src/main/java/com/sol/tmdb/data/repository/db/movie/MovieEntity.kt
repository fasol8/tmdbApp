package com.sol.tmdb.data.repository.db.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String,
    val isFavorite: Boolean = false,
    val isInWatchlist: Boolean = false,
)
