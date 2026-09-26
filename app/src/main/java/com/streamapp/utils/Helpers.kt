package com.streamapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.streamapp.api.ApiService
import com.streamapp.api.Genre

object Helpers {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private val movieGenreMap = mutableMapOf<Int, String>()
    private val seriesGenreMap = mutableMapOf<Int, String>()

    fun setMovieGenres(genres: List<Genre>) {
        movieGenreMap.clear()
        genres.forEach { movieGenreMap[it.id] = it.name }
    }

    fun setSeriesGenres(genres: List<Genre>) {
        seriesGenreMap.clear()
        genres.forEach { seriesGenreMap[it.id] = it.name }
    }

    fun getMovieGenreName(id: Int): String = movieGenreMap[id] ?: "Unknown"
    fun getSeriesGenreName(id: Int): String = seriesGenreMap[id] ?: "Unknown"

    fun getMovieGenresFromIds(ids: List<Int>): List<String> {
        return ids.mapNotNull { movieGenreMap[it] }
    }

    fun getSeriesGenresFromIds(ids: List<Int>): List<String> {
        return ids.mapNotNull { seriesGenreMap[it] }
    }

    private val liveTvCategories = listOf(
        "All", "Entertainment", "Movies", "Sports", "News", "Music",
        "Kids", "Documentary", "Religious", "Shopping", "Education",
        "French", "Arabic", "Turkish", "Hindi", "Punjabi", "Urdu"
    )

    fun getLiveTvCategories(): List<String> = liveTvCategories

    fun getLiveTvChannels(): List<LiveTvChannel> {
        return listOf(
            LiveTvChannel("1", "Al Jazeera", "https://upload.wikimedia.org/wikipedia/en/thumb/f/f2/Aljazeera.svg/200px-Aljazeera.svg.png", "News", "Qatar", "Arabic", "https://live-hls-web-aje.getaj.net/AJE/index.m3u8"),
            LiveTvChannel("2", "France 24", "https://upload.wikimedia.org/wikipedia/commons/thumb/3/34/France_24_logo.svg/200px-France_24_logo.svg.png", "News", "France", "French", "https://stream.france24.com/hls/live/2036578/F24_EN_HI_HLS/master.m3u8"),
            LiveTvChannel("3", "DW News", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/DW_Logo_2012.svg/200px-DW_Logo_2012.svg.png", "News", "Germany", "English", "https://dwamdstream102.akamaized.net/hls/live/2015525/dwstream102/index.m3u8"),
            LiveTvChannel("4", "NHK World", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e2/NHK_World-Japan_logo.svg/200px-NHK_World-Japan_logo.svg.png", "News", "Japan", "English", "https://nhkwlive-ojp.akamaized.net/hls/live/2003459/nhkwlive-ojp-en/index.m3u8"),
            LiveTvChannel("5", "CGTN", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/CGTN_logo.svg/200px-CGTN_logo.svg.png", "News", "China", "English", "https://english.cctv.com/live/cctv_English/index.shtml"),
            LiveTvChannel("6", "RT News", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Russia-today-logo.svg/200px-Russia-today-logo.svg.png", "News", "Russia", "English", "https://rt-glb.rttv.com/live/rtaNews/playlist.m3u8"),
            LiveTvChannel("7", "TRT World", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/TRT_World_logo.svg/200px-TRT_World_logo.svg.png", "News", "Turkey", "English", "https://tv-trtworld.medya.trt.com.tr/master.m3u8"),
            LiveTvChannel("8", "CNA", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Channel_NewsAsia_Logo.svg/200px-Channel_NewsAsia_Logo.svg.png", "News", "Singapore", "English", "https://cna-i.akamaized.net/hls/live/2522908/cna-i/master.m3u8"),
            LiveTvChannel("9", "Euronews", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Euronews_2016_logo.svg/200px-Euronews_2016_logo.svg.png", "News", "France", "English", "https://euronews-euronews-english-2-us.plex.wurl.tv/playlist.m3u8"),
            LiveTvChannel("10", "Bloomberg TV", "https://upload.wikimedia.org/wikipedia/commons/thumb/7/76/Bloomberg_logo.svg/200px-Bloomberg_logo.svg.png", "News", "USA", "English", "https://bloomberg-cmdl-live.akamaized.net/bloombergtv-us/master.m3u8"),
            LiveTvChannel("11", "TED Talks", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/TED_%28conference%29_logo.svg/200px-TED_%28conference%29_logo.svg.png", "Education", "USA", "English", ""),
            LiveTvChannel("12", "NASA TV", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/NASA_logo.svg/200px-NASA_logo.svg.png", "Education", "USA", "English", "https://ntv1.akamaized.net/hls/live/2014075/NASA-NTV1-HLS/master.m3u8"),
            LiveTvChannel("13", "Animal Planet", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Animal_Planet_logo_%282018%29.svg/200px-Animal_Planet_logo_%282018%29.svg.png", "Documentary", "USA", "English", ""),
            LiveTvChannel("14", "National Geographic", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Natgeotvlogo.svg/200px-Natgeotvlogo.svg.png", "Documentary", "USA", "English", ""),
            LiveTvChannel("15", "Discovery Channel", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Discovery_Channel_logo.svg/200px-Discovery_Channel_logo.svg.png", "Documentary", "USA", "English", ""),
            LiveTvChannel("16", "Cartoon Network", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/Cartoon_Network_2010_logo.svg/200px-Cartoon_Network_2010_logo.svg.png", "Kids", "USA", "English", ""),
            LiveTvChannel("17", "Nickelodeon", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9e/Nickelodeon_logo_%282009%29.svg/200px-Nickelodeon_logo_%282009%29.svg.png", "Kids", "USA", "English", ""),
            LiveTvChannel("18", "MTV", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/MTV_logo_2010.svg/200px-MTV_logo_2010.svg.png", "Music", "USA", "English", ""),
            LiveTvChannel("19", "VH1", "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3a/VH1_logo_2010.svg/200px-VH1_logo_2010.svg.png", "Music", "USA", "English", ""),
            LiveTvChannel("20", "CNN", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/CNN_International_logo.svg/200px-CNN_International_logo.svg.png", "News", "USA", "English", "")
        )
    }
}

data class LiveTvChannel(
    val id: String,
    val name: String,
    val logo: String,
    val category: String,
    val country: String,
    val language: String,
    val streamUrl: String
)
