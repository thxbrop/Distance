package com.unltm.distance.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blankj.utilcode.util.GsonUtils
import com.unltm.distance.activity.live.LivePreview
import com.unltm.distance.application
import com.unltm.distance.base.Result
import com.unltm.distance.base.ServerException
import com.unltm.distance.datasource.gson.GetLivePreview
import com.unltm.distance.datasource.gson.GetLiveState
import com.unltm.distance.datasource.gson.GetRealUrl
import com.unltm.distance.datasource.gson.Platform
import kotlinx.coroutines.suspendCancellableCoroutine
import java.nio.charset.Charset
import kotlin.coroutines.resume

class LiveDataSource(
    private val keyword: String = DEFAULT_KEY_WORD
) : PagingSource<Int, LivePreview>() {
    companion object {
        private const val TAG = "LiveDataSource"
        private const val NOT_ALIVE = "未开播或房间不存在"
        private const val START_INDEX = 0
        const val DEFAULT_KEY_WORD = "overwatch"
    }

    private val queue = Volley.newRequestQueue(application.applicationContext)
    private val baseUri = "http://debugers.com:8888/api/"
    private fun queryKeywordUrl(keyword: String, pageNum: Int = START_INDEX): String =
        baseUri + "top/live/$keyword?pageNum=$pageNum"

    private fun queryRealUriOfDouYu(roomId: Int): String =
        baseUri + "douyu/real_url/$roomId"

    private fun queryRealUriOfBili(roomId: Int): String =
        baseUri + "bilibili/real_url/$roomId"

    private fun queryRealUriOfHuYa(roomId: Int): String =
        baseUri + "huya/real_url/$roomId"

    private fun queryLiveStateBili(roomId: Int): String =
        baseUri + "bilibili/checkLiveStatus?roomId=$roomId"

    private fun queryLiveStateDouyu(roomId: Int): String =
        baseUri + "douyu/checkLiveStatus?roomId=$roomId"

    private fun queryLiveStateHuya(roomId: Int): String =
        baseUri + "huya/checkLiveStatus?roomId=$roomId"

    private suspend fun getRoomsByKeyword(keyword: String, pageNum: Int = START_INDEX) =
        suspendCancellableCoroutine<Result<List<LivePreview>>> { coroutine ->
            val url = queryKeywordUrl(keyword, pageNum)
            url.request(
                {
                    val result = GsonUtils.fromJson(it, GetLivePreview::class.java)
                    coroutine.resume(Result.Success(result.data))
                }, {
                    coroutine.resume(Result.Error(it))
                }
            )
        }

    suspend fun getRoomRealUri(com: String, roomId: Int) =
        suspendCancellableCoroutine<Result<String>> { coroutine ->
            when (Platform.valueOf(com)) {
                Platform.DouYu -> queryRealUriOfDouYu(roomId)
                Platform.HuYa -> queryRealUriOfHuYa(roomId)
                Platform.Bili -> queryRealUriOfBili(roomId)
            }.request(
                {
                    val result = GsonUtils.fromJson(it, GetRealUrl::class.java)
                    if (result.data.realUrl != NOT_ALIVE) coroutine.resume(Result.Success(result.data.realUrl))
                        .also {
                            Log.e(TAG, "com:$com result:${result.data.realUrl}")
                        }
                    else coroutine.resume(Result.Error(ServerException.ERROR_LIVE_NOT_PLAYING))
                }, {
                    coroutine.resume(Result.Error(it))
                }
            )
        }

    suspend fun checkLiveStates(roomId: Int, com: String) =
        suspendCancellableCoroutine<Result<Boolean>> { coroutine ->
            when (Platform.valueOf(com)) {
                Platform.DouYu -> queryLiveStateDouyu(roomId)
                Platform.HuYa -> queryLiveStateHuya(roomId)
                Platform.Bili -> queryLiveStateBili(roomId)
            }.request(
                {
                    val result = GsonUtils.fromJson(it, GetLiveState::class.java)
                    coroutine.resume(Result.Success(result.data))
                }, {
                    coroutine.resume(Result.Error(it))
                    Log.e(TAG, "checkLiveStates: ", it)
                }
            )
        }

    override fun getRefreshKey(state: PagingState<Int, LivePreview>): Int = START_INDEX

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LivePreview> {
        val pos = params.key ?: START_INDEX
        return try {
            when (val result = getRoomsByKeyword(keyword, params.key ?: START_INDEX)) {
                is Result.Success -> {
                    val list = result.data
                    LoadResult.Page(
                        list,
                        if (pos <= START_INDEX) null else pos - 1,
                        if (list.isEmpty()) null else pos + 1
                    )
                }
                is Result.Error -> LoadResult.Error(result.exception)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun String.request(listener: (String) -> Unit, errorListener: (VolleyError) -> Unit) {
        val request = StringRequest(Request.Method.GET, this,
            {
                val r = String(
                    it.trim().toByteArray(Charset.forName("iso-8859-1")),
                    Charset.defaultCharset()
                )
                listener.invoke(r)
            }, {
                Log.e(TAG, "getRoomsByKeyword:", it)
                errorListener.invoke(it)
            })
        queue.add(request)
    }
}