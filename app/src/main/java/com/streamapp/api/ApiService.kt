package com.streamapp.api

import com.streamapp.models.ContentResponse
import com.streamapp.models.Movie
import com.streamapp.models.Series
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
        const val TMDB_IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        const val TMDB_BACKDROP_URL = "https://image.tmtm.org/t/p/original"
        const val TMDB_API_KEY = "YOUR_TMDB_API_KEY"
        const val STREAM_API_BASE = "https://raw.githubusercontent.com/your-repo/stream-api/main/"
    }

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = TMDB_API_KEY,
        @Query("page") page: Int = 1
    ): Response<TMDBMovieResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = TMDB_API_KEY,
        @Query("page") page: Int = 1
    ): Response<TMDBMovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = TMDB_API_KEY,
        @Query("page") page: Int = 1
    ): Response<TMDBMovieResponse>

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("api_key") apiKey: String = TMDB_API_KEY
    ): Response<GenreResponse>

    @GET("tv/popular")
    suspend fun getPopularSeries(
        @Query("api_key") apiKey: String = TMDB_API_KEY,
        @Query("page") page: Int = 1
    ): Response<TMDBSeriesResponse>

    @GET("tv/airing_today")
    suspend fun getAiringTodaySeries(
        @Query("api_key") apiKey: String = TMDB_API_KEY,
        @Query("page") page: Int = 1
    ): Response<TMDBSeriesResponse>

    @GET("tv/top_rated")
    suspend fun getTopRatedSeries(
        @Query("api_key") apiKey: String = TMDB_API_KEY,
        @Query("page") page: Int = 1
    ): Response<TMDBSeriesResponse>

    @GET("genre/tv/list")
    suspend fun getSeriesGenres(
        @Query("api_key") apiKey: String = TMDB_API_KEY
    ): Response<GenreResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: String,
        @Query("api_key") apiKey: String = TMDB_API_KEY
    ): Response<TMDBMovieDetail>

    @GET("tv/{id}")
    suspend fun getSeriesDetails(
        @Path("id") id: String,
        @Query("api_key") apiKey: String = TMDB_API_KEY
    ): Response<TMDBSeriesDetail>

    @GET("tv/{id}/season/{season}")
    suspend fun getSeasonDetails(
        @Path("id") seriesId: String,
        @Path("season") seasonNumber: Int,
        @Query("api_key") apiKey: String = TMDB_API_KEY
    ): Response<TMDBSeasonDetail>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = TMDB_API_KEY,
        @Query("page") page: Int = 1
    ): Response<TMDBMovieResponse>

    @GET("search/tv")
    suspend fun searchSeries(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = TMDB_API_KEY,
        @Query("page") page: Int = 1
    ): Response<TMDBSeriesResponse>
}
