package com.unltm.distance.datasource.config

import com.unltm.distance.room.entity.Music
import kotlinx.coroutines.suspendCancellableCoroutine

class MusicConfig private constructor() : BaseConfig() {
    companion object {
        private val md5Uri: String get() = baseUrl + "music"
        private val randomUri: String get() = "$md5Uri/random"
        private const val KEY_MD5 = "md5"

        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MusicConfig() }
    }

    suspend fun getByMd5(md5: String) = suspendCancellableCoroutine<Music> {
        it.resumeWithRequestUrl("$md5Uri?$KEY_MD5=$md5")
    }

    suspend fun getRandom() = suspendCancellableCoroutine<List<Music>> { coroutine ->
        coroutine.resumeWithRequestUrl(randomUri)
    }
}