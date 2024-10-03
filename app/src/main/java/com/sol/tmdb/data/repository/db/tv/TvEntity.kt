package com.sol.tmdb.data.repository.db.tv

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tvs")
data class TvEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String,
    val isFavorite: Boolean = false,
    val isInWatchlist: Boolean = false,
)