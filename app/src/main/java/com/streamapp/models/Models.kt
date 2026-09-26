package com.streamapp.models

import com.google.gson.annotations.SerializedName

data class ContentResponse(
    @SerializedName("categories") val categories: List<String> = emptyList(),
    @SerializedName("movies") val movies: List<Movie> = emptyList(),
    @SerializedName("series") val series: List<Series> = emptyList(),
    @SerializedName("live_tv") val liveTv: List<Channel> = emptyList(),
    @SerializedName("stream") val stream: StreamData? = null
)

data class Movie(
    @SerializedName("id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("poster") val poster: String = "",
    @SerializedName("backdrop") val backdrop: String = "",
    @SerializedName("year") val year: String = "",
    @SerializedName("rating") val rating: String = "",
    @SerializedName("quality") val quality: String = "",
    @SerializedName("category") val category: String = "",
    @SerializedName("language") val language: String = "",
    @SerializedName("genre") val genre: List<String> = emptyList(),
    @SerializedName("stream_url") val streamUrl: String = "",
    @SerializedName("sources") val sources: List<StreamSource> = emptyList()
)

data class Series(
    @SerializedName("id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("poster") val poster: String = "",
    @SerializedName("backdrop") val backdrop: String = "",
    @SerializedName("year") val year: String = "",
    @SerializedName("rating") val rating: String = "",
    @SerializedName("category") val category: String = "",
    @SerializedName("language") val language: String = "",
    @SerializedName("genre") val genre: List<String> = emptyList(),
    @SerializedName("seasons") val seasons: List<Season> = emptyList()
)

data class Season(
    @SerializedName("season_number") val seasonNumber: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("episodes") val episodes: List<Episode> = emptyList()
)

data class Episode(
    @SerializedName("episode_number") val episodeNumber: Int = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("duration") val duration: String = "",
    @SerializedName("stream_url") val streamUrl: String = "",
    @SerializedName("sources") val sources: List<StreamSource> = emptyList()
)

data class Channel(
    @SerializedName("id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("category") val category: String = "",
    @SerializedName("country") val country: String = "",
    @SerializedName("language") val language: String = "",
    @SerializedName("stream_url") val streamUrl: String = "",
    @SerializedName("sources") val sources: List<StreamSource> = emptyList()
)

data class StreamSource(
    @SerializedName("name") val name: String = "",
    @SerializedName("url") val url: String = ""
)

data class StreamData(
    @SerializedName("url") val url: String = "",
    @SerializedName("sources") val sources: List<StreamSource> = emptyList()
)

data class SearchResponse(
    @SerializedName("movies") val movies: List<Movie> = emptyList(),
    @SerializedName("series") val series: List<Series> = emptyList(),
    @SerializedName("live_tv") val liveTv: List<Channel> = emptyList()
)
