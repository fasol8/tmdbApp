package com.sol.tmdb.data.repository.db.tv

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sol.tmdb.data.repository.db.movie.MovieEntity

@Dao
interface TvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTv(tv: TvEntity)

    @Query("SELECT * FROM tvs WHERE id = :tvId")
    suspend fun getTvById(tvId: Int): TvEntity?

    @Query("SELECT * FROM tvs WHERE isFavorite = 1")
    suspend fun getFavoriteTvs(): List<TvEntity>

    @Query("SELECT * FROM tvs WHERE isInWatchlist = 1")
    suspend fun getWatchListTvs(): List<TvEntity>

    @Update
    suspend fun updateTv(tv: TvEntity)

    @Delete
    suspend fun deleteTv(tv: TvEntity)
}