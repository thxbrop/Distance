package com.unltm.distance.ui.live

import java.io.Serializable

data class LivePreview(
    val avatar: String,
    val cateId: Int,
    val cateName: String,
    val com: String,
    val online: Int,
    val ownerName: String,
    val realUrl: Any,
    val roomId: Int,
    val roomName: String,
    val roomStatus: Int,
    val roomThumb: String,
    val startTime: String
) : Serializable