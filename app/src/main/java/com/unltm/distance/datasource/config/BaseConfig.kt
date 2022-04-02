package com.unltm.distance.datasource.config

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.Keep
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android.volley.Request
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.google.gson.reflect.TypeToken
import com.unltm.distance.application
import com.unltm.distance.base.ServerException
import com.unltm.distance.base.contracts.dataStoreSetting
import com.unltm.distance.base.contracts.getValueFlow
import com.unltm.distance.base.contracts.gson
import com.unltm.distance.base.contracts.volley
import com.unltm.distance.datasource.RequestResult
import com.unltm.distance.fragment.setting.SettingFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Keep
abstract class BaseConfig {
    protected inline fun <reified T> Continuation<T>.resumeStringRequest(
        url: String,
        method: Int = Request.Method.GET
    ) {
        val type = object : TypeToken<RequestResult<T>>() {}
        StringRequest(method, url,
            {
                val fromJson = gson.fromJson<RequestResult<T>>(it, type.type)
                if (fromJson.code == ServerException.OK.code) resume(fromJson.data!!)
                else resumeWithException(ServerException(fromJson.code, fromJson.message))
            },
            { resumeWithException(it) }
        ).also { volley.add(it) }

    }

    protected fun Continuation<Bitmap>.resumeImageRequest(
        url: String
    ) {
        ImageRequest(
            url,
            { resume(it) },
            0,
            0,
            ImageView.ScaleType.CENTER_INSIDE,
            Bitmap.Config.ARGB_8888
        ) { resumeWithException(it) }.also { volley.add(it) }
    }

    companion object {
        var address = "127.0.0.1"
        var port = "8080"
        val baseUrl: String get() = "http://$address:$port/distance/"
        suspend fun fetchData() {
            withContext(Dispatchers.IO) {
                application.dataStoreSetting.getValueFlow(
                    stringPreferencesKey(SettingFragment.SETTING_PROXY_ADDRESS),
                    address
                ).collectLatest {
                    address = it
                }
            }
//            withContext(Dispatchers.IO) {
//                application.dataStoreSetting.getValueFlow(
//                    stringPreferencesKey(SettingFragment.SETTING_PROXY_PORT),
//                    port
//                ).collectLatest {
//                    port = it
//                }
//            }
        }
    }
}