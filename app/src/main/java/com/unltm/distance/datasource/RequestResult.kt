package com.unltm.distance.datasource

import com.unltm.distance.base.ServerException

/**
 * 请求服务器数据的包装
 * @param code 错误码
 * @param message 错误信息
 * @param data 数据
 *
 * @see ServerException
 */
data class RequestResult<out T>(
    val code: Int = ServerException.OK.code,
    val message: String = ServerException.OK.message,
    val data: T? = null
)
