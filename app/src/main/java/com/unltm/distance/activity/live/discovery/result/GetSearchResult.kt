package com.unltm.distance.activity.live.discovery.result

import com.unltm.distance.datasource.LiveDataSource

data class GetSearchResult(
    val data: LiveDataSource? = null,
    val error: Exception? = null
)
