package com.unltm.distance.repository

import com.unltm.distance.base.Result
import com.unltm.distance.datasource.impl.MusicDataSource
import com.unltm.distance.room.entity.Music
import com.unltm.distance.storage.MusicStorage

class MusicRepository private constructor(
    private val musicDataSource: MusicDataSource,
    private val musicStorage: MusicStorage
) {
    data class GetByMD5Result(
        val data: Music? = null,
        val error: Exception? = null
    )

    data class GetMusicsResult(
        val data: List<Music>? = null,
        val error: Exception? = null
    )

    suspend fun getByMd5(md5: String): GetByMD5Result {
        musicStorage.getByMD5(md5)?.let {
            return GetByMD5Result(data = it)
        }
        return when (val result = musicDataSource.getByMD5(md5)) {
            is Result.Success -> GetByMD5Result(data = result.data)
            is Result.Error -> GetByMD5Result(error = result.exception)
        }
    }

    suspend fun getMusicsRandomForServer(): GetMusicsResult {
        return when (val result = musicDataSource.getRandomFromServer()) {
            is Result.Success -> GetMusicsResult(data = result.data)
            is Result.Error -> GetMusicsResult(error = result.exception)
        }
    }


    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MusicRepository(
                musicDataSource = MusicDataSource.INSTANCE,
                musicStorage = MusicStorage()
            )
        }
    }
}