package com.unltm.distance.datasource.gson

import com.unltm.distance.ui.live.LivePreview

data class GetLivePreview(
    val code: Int,
    val `data`: List<LivePreview>,
    val msg: String
)