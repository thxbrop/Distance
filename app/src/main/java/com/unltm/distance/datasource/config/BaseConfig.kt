package com.unltm.distance.datasource.config

import com.android.volley.toolbox.StringRequest
import com.unltm.distance.base.contracts.gson
import com.unltm.distance.base.contracts.volley
import kotlinx.coroutines.CancellableContinuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

abstract class BaseConfig {
    protected inline fun <reified T> request(
        url: String,
        c: CancellableContinuation<T>
    ) {
        StringRequest(url,
            { c.resume(gson.fromJson(it, T::class.java)) },
            { c.resumeWithException(it) }
        ).also { volley.add(it) }
    }

    companion object {
        var address = "127.0.0.1"
        var port = "8080"
        val baseUrl: String get() = "http://$address:$port/DistanceTomcat_war_exploded/"
    }
}