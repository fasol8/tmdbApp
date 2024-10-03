package com.sol.tmdb.data.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sol.tmdb.data.repository.db.movie.MovieDao
import com.sol.tmdb.data.repository.db.movie.MovieEntity
import com.sol.tmdb.data.repository.db.tv.TvDao
import com.sol.tmdb.data.repository.db.tv.TvEntity

//@Database(entities = [MovieEntity::class, TvEntity::class], version = 1)
@Database(entities = [MovieEntity::class, TvEntity::class], version = 3, exportSchema = false)
abstract class TmdbDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvDao(): TvDao
}
