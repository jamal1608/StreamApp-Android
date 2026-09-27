package com.streamapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

interface FreeApiService {

    @GET("trending/movie/week")
    suspend fun getPopularMovies(@Query("api_key") key: String = "232b810ad3c2b5d7b447046ab19f2029"): Response<TMDBMovieResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("api_key") key: String = "232b810ad3c2b5d7b447046ab19f2029"): Response<TMDBMovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") key: String = "232b810ad3c2b5d7b447046ab19f2029"): Response<TMDBMovieResponse>

    @GET("genre/movie/list")
    suspend fun getMovieGenres(@Query("api_key") key: String = "232b810ad3c2b5d7b447046ab19f2029"): Response<GenreResponse>

    @GET("trending/tv/week")
    suspend fun getPopularSeries(@Query("api_key") key: String = "232b810ad3c2b5d7b447046ab19f2029"): Response<TMDBSeriesResponse>

    @GET("tv/airing_today")
    suspend fun getAiringTodaySeries(@Query("api_key") key: String = "232b810ad3c2b5d7b447046ab19f2029"): Response<TMDBSeriesResponse>

    @GET("tv/top_rated")
    suspend fun getTopRatedSeries(@Query("api_key") key: String = "232b810ad3c2b5d7b447046ab19f2029"): Response<TMDBSeriesResponse>

    @GET("genre/tv/list")
    suspend fun getSeriesGenres(@Query("api_key") key: String = "232b810ad3c2b5d7b447046ab19f2029"): Response<GenreResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: String, @Query("api_key") key: String = "232b810ad3c2b5d7b447046ab19f2029"): Response<TMDBMovieDetail>

    @GET("tv/{id}")
    suspend fun getSeriesDetails(@Path("id") id: String, @Query("api_key") key: String = "232b810ad3c2b5d7b447046ab19f2029"): Response<TMDBSeriesDetail>

    @GET("tv/{id}/season/{season}")
    suspend fun getSeasonDetails(@Path("id") seriesId: String, @Path("season") seasonNumber: Int, @Query("api_key") key: String = "232b810ad3c2b5d7b447046ab19f2029"): Response<TMDBSeasonDetail>

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String, @Query("api_key") key: String = "232b810ad3c2b5d7b447046ab19f2029"): Response<TMDBMovieResponse>

    @GET("search/tv")
    suspend fun searchSeries(@Query("query") query: String, @Query("api_key") key: String = "232b810ad3c2b5d7b447046ab19f2029"): Response<TMDBSeriesResponse>
}

object FreeRetrofitClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: FreeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FreeApiService::class.java)
    }
}
