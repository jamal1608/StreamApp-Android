package com.streamapp.api

import com.google.gson.annotations.SerializedName

data class TMDBMovieResponse(
    @SerializedName("page") val page: Int = 0,
    @SerializedName("results") val results: List<TMDBMovieItem> = emptyList(),
    @SerializedName("total_pages") val totalPages: Int = 0,
    @SerializedName("total_results") val totalResults: Int = 0
)

data class TMDBMovieItem(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerializedName("original_language") val originalLanguage: String = ""
) {
    fun getPosterUrl(): String = if (posterPath != null) "${ApiService.TMDB_IMAGE_URL}$posterPath" else ""
    fun getBackdropUrl(): String = if (backdropPath != null) "${ApiService.TMDB_IMAGE_URL}$backdropPath" else ""
    fun getYear(): String = releaseDate.take(4)
    fun getRating(): String = String.format("%.1f", voteAverage)
}

data class TMDBSeriesResponse(
    @SerializedName("page") val page: Int = 0,
    @SerializedName("results") val results: List<TMDBSeriesItem> = emptyList(),
    @SerializedName("total_pages") val totalPages: Int = 0,
    @SerializedName("total_results") val totalResults: Int = 0
)

data class TMDBSeriesItem(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("first_air_date") val firstAirDate: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerializedName("original_language") val originalLanguage: String = ""
) {
    fun getPosterUrl(): String = if (posterPath != null) "${ApiService.TMDB_IMAGE_URL}$posterPath" else ""
    fun getBackdropUrl(): String = if (backdropPath != null) "${ApiService.TMDB_IMAGE_URL}$backdropPath" else ""
    fun getYear(): String = firstAirDate.take(4)
    fun getRating(): String = String.format("%.1f", voteAverage)
}

data class TMDBMovieDetail(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("genres") val genres: List<Genre> = emptyList(),
    @SerializedName("runtime") val runtime: Int = 0,
    @SerializedName("status") val status: String = ""
) {
    fun getPosterUrl(): String = if (posterPath != null) "${ApiService.TMDB_IMAGE_URL}$posterPath" else ""
    fun getBackdropUrl(): String = if (backdropPath != null) "${ApiService.TMDB_IMAGE_URL}$backdropPath" else ""
    fun getYear(): String = releaseDate.take(4)
    fun getRating(): String = String.format("%.1f", voteAverage)
    fun getGenresString(): String = genres.joinToString(", ") { it.name }
}

data class TMDBSeriesDetail(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("first_air_date") val firstAirDate: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("genres") val genres: List<Genre> = emptyList(),
    @SerializedName("number_of_seasons") val numberOfSeasons: Int = 0,
    @SerializedName("number_of_episodes") val numberOfEpisodes: Int = 0,
    @SerializedName("seasons") val seasons: List<TMDBSeasonItem> = emptyList(),
    @SerializedName("status") val status: String = ""
) {
    fun getPosterUrl(): String = if (posterPath != null) "${ApiService.TMDB_IMAGE_URL}$posterPath" else ""
    fun getBackdropUrl(): String = if (backdropPath != null) "${ApiService.TMDB_IMAGE_URL}$backdropPath" else ""
    fun getYear(): String = firstAirDate.take(4)
    fun getRating(): String = String.format("%.1f", voteAverage)
    fun getGenresString(): String = genres.joinToString(", ") { it.name }
}

data class TMDBSeasonItem(
    @SerializedName("season_number") val seasonNumber: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("episode_count") val episodeCount: Int = 0,
    @SerializedName("overview") val overview: String = "",
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("air_date") val airDate: String = ""
)

data class TMDBSeasonDetail(
    @SerializedName("season_number") val seasonNumber: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("episodes") val episodes: List<TMDBEpisode> = emptyList()
)

data class TMDBEpisode(
    @SerializedName("episode_number") val episodeNumber: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("overview") val overview: String = "",
    @SerializedName("still_path") val stillPath: String? = null,
    @SerializedName("air_date") val airDate: String = "",
    @SerializedName("runtime") val runtime: Int = 0
) {
    fun getStillUrl(): String = if (stillPath != null) "${ApiService.TMDB_IMAGE_URL}$stillPath" else ""
}

data class Genre(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = ""
)

data class GenreResponse(
    @SerializedName("genres") val genres: List<Genre> = emptyList()
)
