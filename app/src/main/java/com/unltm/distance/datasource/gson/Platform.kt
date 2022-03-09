package com.unltm.distance.datasource.gson

import com.unltm.distance.ui.live.UnsupportedLiveRoomException

sealed class Platform {
    object DouYu : Platform()
    object HuYa : Platform()
    object Bili : Platform()
    companion object {
        @Throws(UnsupportedLiveRoomException::class)
        fun valueOf(value: String?): Platform = run {
            when (value) {
                "douyu" -> DouYu
                "huya" -> HuYa
                "bilibili" -> Bili
                else -> throw UnsupportedLiveRoomException(value)
            }
        }
    }
}
