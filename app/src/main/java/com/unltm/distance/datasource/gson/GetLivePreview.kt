package com.unltm.distance.datasource.gson

import com.unltm.distance.activity.live.LivePreview

data class GetLivePreview(
    val code: Int,
    val `data`: List<LivePreview>,
    val msg: String
)