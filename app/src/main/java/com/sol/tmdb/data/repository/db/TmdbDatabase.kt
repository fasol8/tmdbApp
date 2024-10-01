package com.sol.tmdb.data.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase

//@Database(entities = [MovieEntity::class, TvEntity::class], version = 1)
@Database(entities = [MovieEntity::class], version = 1)
abstract class TmdbDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
//    abstract fun tvDao(): TvDao
}
