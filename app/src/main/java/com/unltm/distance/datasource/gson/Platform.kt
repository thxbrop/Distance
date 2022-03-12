package com.unltm.distance.datasource.gson

import com.unltm.distance.base.ServerException

sealed class Platform {
    object DouYu : Platform()
    object HuYa : Platform()
    object Bili : Platform()
    companion object {
        @Throws(ServerException::class)
        fun valueOf(value: String?): Platform = run {
            when (value) {
                "douyu" -> DouYu
                "huya" -> HuYa
                "bilibili" -> Bili
                else -> throw ServerException.ERROR_LIVE_SUPPORTED
            }
        }
    }
}
