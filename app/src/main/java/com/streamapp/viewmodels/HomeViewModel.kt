package com.streamapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streamapp.api.FreeRetrofitClient
import com.streamapp.api.TMDBMovieItem
import com.streamapp.api.TMDBSeriesItem
import com.streamapp.utils.Helpers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _popularMovies = MutableLiveData<List<TMDBMovieItem>>()
    val popularMovies: LiveData<List<TMDBMovieItem>> = _popularMovies

    private val _trendingMovies = MutableLiveData<List<TMDBMovieItem>>()
    val trendingMovies: LiveData<List<TMDBMovieItem>> = _trendingMovies

    private val _popularSeries = MutableLiveData<List<TMDBSeriesItem>>()
    val popularSeries: LiveData<List<TMDBSeriesItem>> = _popularSeries

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val api = FreeRetrofitClient.api
                val popularMoviesResponse = api.getPopularMovies()
                if (popularMoviesResponse.isSuccessful) {
                    _popularMovies.value = popularMoviesResponse.body()?.results ?: emptyList()
                }

                val trendingResponse = api.getNowPlayingMovies()
                if (trendingResponse.isSuccessful) {
                    _trendingMovies.value = trendingResponse.body()?.results ?: emptyList()
                }

                val seriesResponse = api.getPopularSeries()
                if (seriesResponse.isSuccessful) {
                    _popularSeries.value = seriesResponse.body()?.results ?: emptyList()
                }

                val genresResponse = api.getMovieGenres()
                if (genresResponse.isSuccessful) {
                    Helpers.setMovieGenres(genresResponse.body()?.genres ?: emptyList())
                }

                val seriesGenresResponse = api.getSeriesGenres()
                if (seriesGenresResponse.isSuccessful) {
                    Helpers.setSeriesGenres(seriesGenresResponse.body()?.genres ?: emptyList())
                }

                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
