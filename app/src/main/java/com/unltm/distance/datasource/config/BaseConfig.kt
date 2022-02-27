package com.unltm.distance.datasource.config

import com.android.volley.toolbox.StringRequest
import com.google.gson.reflect.TypeToken
import com.unltm.distance.base.ServerException
import com.unltm.distance.base.contracts.gson
import com.unltm.distance.base.contracts.volley
import com.unltm.distance.datasource.RequestResult
import kotlinx.coroutines.CancellableContinuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

abstract class BaseConfig {
    protected inline fun <reified T> buildStringRequest(
        url: String,
        c: CancellableContinuation<T>
    ) {
        val type = object : TypeToken<RequestResult<T>>() {}
        StringRequest(url,
            {
                val fromJson = gson.fromJson<RequestResult<T>>(it, type.type)
                if (fromJson.code == ServerException.OK.code) c.resume(fromJson.data!!)
                else c.resumeWithException(ServerException(fromJson.code, fromJson.message))
            },
            { c.resumeWithException(it) }
        ).also { volley.add(it) }
    }


    companion object {
        var address = "127.0.0.1"
        var port = "8080"
        val baseUrl: String get() = "http://$address:$port/distance/"
    }
}