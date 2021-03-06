package com.unltm.distance.activity.live.discovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unltm.distance.datasource.LiveDataSource
import com.unltm.distance.activity.live.discovery.result.GetSearchResult

class DiscoveryViewModel : ViewModel() {
    private var _searchLive = MutableLiveData(GetSearchResult(data = LiveDataSource()))
    val searchResult: LiveData<GetSearchResult> = _searchLive
    fun search(keyword: String) {
        try {
            _searchLive.value = GetSearchResult(data = LiveDataSource(keyword))
        } catch (e: Exception) {
            _searchLive.value = GetSearchResult(error = e)
        }
    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DiscoveryViewModel()
        }
    }
}