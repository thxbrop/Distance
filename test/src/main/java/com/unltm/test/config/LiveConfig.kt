package com.unltm.test.config

import com.unltm.test.base.volley
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LiveConfig {
    suspend fun getDouYinRealUrl(sharedUrl: String) =
        suspendCancellableCoroutine<String> { coroutine ->
            HeaderStringRequest(
                sharedUrl,
                linkedMapOf(
                    "User-Agent" to "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) " +
                            "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"
                ),
                coroutine::resume,
                coroutine::resumeWithException
            ).also(volley::add)
            //Regex("(\\d{19})").findAll()
        }
}