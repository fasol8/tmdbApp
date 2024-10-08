package com.sol.tmdb.data.network

import android.content.Context
import androidx.room.Room
import com.sol.tmdb.data.repository.db.movie.MovieDao
import com.sol.tmdb.data.repository.db.TmdbDatabase
import com.sol.tmdb.data.repository.db.tv.TvDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): TmdbDatabase {
        return Room.databaseBuilder(
            appContext,
            TmdbDatabase::class.java,
            "tmdb_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideMovieDao(database: TmdbDatabase): MovieDao {
        return database.movieDao()
    }

    @Provides
    fun provideTvDao(database: TmdbDatabase): TvDao {
        return database.tvDao()
    }
}
