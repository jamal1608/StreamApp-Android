package com.streamapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.streamapp.utils.Helpers
import com.streamapp.utils.LiveTvChannel

class LiveTvViewModel : ViewModel() {

    private val _channels = MutableLiveData<List<LiveTvChannel>>()
    val channels: LiveData<List<LiveTvChannel>> = _channels

    private val _filteredChannels = MutableLiveData<List<LiveTvChannel>>()
    val filteredChannels: LiveData<List<LiveTvChannel>> = _filteredChannels

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val allChannels = mutableListOf<LiveTvChannel>()

    fun loadChannels() {
        _isLoading.value = true
        allChannels.clear()
        allChannels.addAll(Helpers.getLiveTvChannels())
        _channels.value = allChannels.toList()
        _filteredChannels.value = allChannels.toList()
        _categories.value = Helpers.getLiveTvCategories()
        _isLoading.value = false
    }

    fun filterByCategory(category: String) {
        if (category == "All") {
            _filteredChannels.value = allChannels.toList()
        } else {
            _filteredChannels.value = allChannels.filter {
                it.category.equals(category, ignoreCase = true)
            }
        }
    }

    fun searchChannels(query: String) {
        if (query.isEmpty()) {
            _filteredChannels.value = allChannels.toList()
        } else {
            _filteredChannels.value = allChannels.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.country.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
        }
    }
}
