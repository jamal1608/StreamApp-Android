package com.streamapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streamapp.api.FreeRetrofitClient
import com.streamapp.api.TMDBSeriesItem
import kotlinx.coroutines.launch

class SeriesViewModel : ViewModel() {

    private val _series = MutableLiveData<List<TMDBSeriesItem>>()
    val series: LiveData<List<TMDBSeriesItem>> = _series

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var currentPage = 1
    private var currentCategory = "Popular"
    private var isLoadingMore = false

    fun loadSeries(category: String = "Popular", refresh: Boolean = false) {
        if (isLoadingMore && !refresh) return
        currentCategory = category
        if (refresh) currentPage = 1

        viewModelScope.launch {
            _isLoading.value = currentPage == 1
            isLoadingMore = true
            try {
                val api = FreeRetrofitClient.api
                val response = when (category) {
                    "Popular" -> api.getPopularSeries()
                    "Airing Today" -> api.getAiringTodaySeries()
                    "Top Rated" -> api.getTopRatedSeries()
                    else -> api.getPopularSeries()
                }
                if (response.isSuccessful) {
                    val newSeries = response.body()?.results ?: emptyList()
                    if (currentPage == 1) {
                        _series.value = newSeries
                    } else {
                        _series.value = (_series.value ?: emptyList()) + newSeries
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
        loadSeries(currentCategory)
    }

    fun searchSeries(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = FreeRetrofitClient.api.searchSeries(query)
                if (response.isSuccessful) {
                    _series.value = response.body()?.results ?: emptyList()
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
