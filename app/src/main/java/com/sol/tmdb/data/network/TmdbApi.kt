package com.sol.tmdb.data.network

import com.sol.tmdb.BuildConfig
import com.sol.tmdb.domain.model.movie.MovieCredits
import com.sol.tmdb.domain.model.movie.MovieDetail
import com.sol.tmdb.domain.model.movie.MovieProviderResponse
import com.sol.tmdb.domain.model.movie.MovieRecommendationResponse
import com.sol.tmdb.domain.model.movie.MovieRelease
import com.sol.tmdb.domain.model.movie.MovieResponse
import com.sol.tmdb.domain.model.movie.MovieSimilarResponse
import com.sol.tmdb.domain.model.person.ImagesResponse
import com.sol.tmdb.domain.model.person.MovieCreditsResponse
import com.sol.tmdb.domain.model.person.PersonDetail
import com.sol.tmdb.domain.model.person.PersonResponse
import com.sol.tmdb.domain.model.person.TvCreditsResponse
import com.sol.tmdb.domain.model.tv.CreditsResponse
import com.sol.tmdb.domain.model.tv.SimilarResponse
import com.sol.tmdb.domain.model.tv.TvDetail
import com.sol.tmdb.domain.model.tv.TvResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("discover/movie")
    suspend fun getDiscoverMovie(
        @Query("page") page: Int = 1,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): MovieDetail

    @GET("movie/{movie_id}/release_dates")
    suspend fun getMovieRelease(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): MovieRelease

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): MovieCredits

    @GET("movie/{movie_id}/watch/providers")
    suspend fun getMovieProviders(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): MovieProviderResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getMovieSimilar(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): MovieSimilarResponse

    @GET("movie/{movie_id}/recommendations")
    suspend fun getMovieRecommendation(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): MovieRecommendationResponse

    @GET("tv/popular")
    suspend fun getPopularTv(
        @Query("page") page: Int = 1,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): TvResponse

    @GET("tv/{tv_id}")
    suspend fun getTvDetail(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): TvDetail

    @GET("tv/{tv_id}/credits")
    suspend fun getTvCredits(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): CreditsResponse

    @GET("tv/{tv_id}/similar")
    suspend fun getTvSimilar(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): SimilarResponse

    @GET("person/popular")
    suspend fun getPopularPerson(
        @Query("page") page: Int = 1,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): PersonResponse

    @GET("person/{person_id}")
    suspend fun getPersonDetail(
        @Path("person_id") page: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): PersonDetail

    @GET("person/{person_id}/movie_credits")
    suspend fun getCreditsMovies(
        @Path("person_id") page: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): MovieCreditsResponse

    @GET("person/{person_id}/tv_credits")
    suspend fun getCreditsTv(
        @Path("person_id") page: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): TvCreditsResponse

    @GET("person/{person_id}/images")
    suspend fun getImagesProfile(
        @Path("person_id") page: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): ImagesResponse
}