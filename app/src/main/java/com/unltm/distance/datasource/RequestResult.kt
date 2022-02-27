package com.unltm.distance.datasource

import com.unltm.distance.base.ServerException


data class RequestResult<out T>(
    val code: Int = ServerException.OK.code,
    val message: String = ServerException.OK.message,
    val data: T? = null
)
