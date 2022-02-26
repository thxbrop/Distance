package com.unltm.distance.datasource.gson

import com.unltm.distance.ui.live.UnsupportedLiveRoomException

sealed class Com {
    object DouYu : Com()
    object HuYa : Com()
    object Bili : Com()
    companion object {
        @Throws(UnsupportedLiveRoomException::class)
        fun valueOf(value: String?): Com = run {
            when (value) {
                "douyu" -> DouYu
                "huya" -> HuYa
                "bilibili" -> Bili
                else -> throw UnsupportedLiveRoomException(value)
            }
        }
    }
}
