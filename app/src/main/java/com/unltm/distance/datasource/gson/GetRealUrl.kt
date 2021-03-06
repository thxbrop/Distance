package com.unltm.distance.datasource.gson

data class GetRealUrl(
    val code: Int,
    val `data`: Data,
    val msg: String
) {
    data class Data(
        val realUrl: String
    )
}