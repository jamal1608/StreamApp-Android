package com.streamapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streamapp.api.FreeRetrofitClient
import com.streamapp.api.TMDBMovieItem
import kotlinx.coroutines.launch

class MoviesViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<TMDBMovieItem>>()
    val movies: LiveData<List<TMDBMovieItem>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var currentPage = 1
    private var currentCategory = "Popular"
    private var isLoadingMore = false

    fun loadMovies(category: String = "Popular", refresh: Boolean = false) {
        if (isLoadingMore && !refresh) return
        currentCategory = category
        if (refresh) currentPage = 1

        viewModelScope.launch {
            _isLoading.value = currentPage == 1
            isLoadingMore = true
            try {
                val api = FreeRetrofitClient.api
                val response = when (category) {
                    "Popular" -> api.getPopularMovies()
                    "Now Playing" -> api.getNowPlayingMovies()
                    "Top Rated" -> api.getTopRatedMovies()
                    else -> api.getPopularMovies()
                }
                if (response.isSuccessful) {
                    val newMovies = response.body()?.results ?: emptyList()
                    if (currentPage == 1) {
                        _movies.value = newMovies
                    } else {
                        _movies.value = (_movies.value ?: emptyList()) + newMovies
                    }
                }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
                isLoadingMore = false
            }
        }
    }

    fun loadNextPage() {
        currentPage++
        loadMovies(currentCategory)
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = FreeRetrofitClient.api.searchMovies(query)
                if (response.isSuccessful) {
                    _movies.value = response.body()?.results ?: emptyList()
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
