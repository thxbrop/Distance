package com.unltm.distance.datasource.config

import com.android.volley.toolbox.StringRequest
import com.google.gson.reflect.TypeToken
import com.unltm.distance.base.ServerException
import com.unltm.distance.base.contracts.gson
import com.unltm.distance.base.contracts.volley
import com.unltm.distance.datasource.RequestResult
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

abstract class BaseConfig {
    protected inline fun <reified T> Continuation<T>.resumeWithRequestUrl(
        url: String
    ) {
        val type = object : TypeToken<RequestResult<T>>() {}

        StringRequest(url,
            {
                val fromJson = gson.fromJson<RequestResult<T>>(it, type.type)
                if (fromJson.code == ServerException.OK.code) resume(fromJson.data!!)
                else resumeWithException(ServerException(fromJson.code, fromJson.message))
            },
            { resumeWithException(it) }
        ).also {
            volley.add(it)
        }

    }

    companion object {
        var address = "127.0.0.1"
        var port = "8080"
        val baseUrl: String get() = "http://$address:$port/distance/"
    }
}