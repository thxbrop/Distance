package com.unltm.distance.ui.live.reco.result

import com.unltm.distance.datasource.LiveDataSource

data class GetSearchResult(
    val data: LiveDataSource? = null,
    val error: Exception? = null
)
