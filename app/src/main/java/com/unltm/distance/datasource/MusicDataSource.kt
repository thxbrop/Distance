package com.unltm.distance.datasource

import android.util.Log
import com.unltm.distance.base.Result
import com.unltm.distance.datasource.base.IMusicDataSource
import com.unltm.distance.datasource.config.MusicConfig
import com.unltm.distance.room.entity.Music

private const val TAG = "MusicDataSource"

class MusicDataSource private constructor(
    private val musicConfig: MusicConfig
) : IMusicDataSource {

    override suspend fun getByMD5(md5: String): Result<Music> {
        return try {
            val musicByMd5 = musicConfig.getByMd5(md5)
            Result.Success(musicByMd5)
        } catch (e: Exception) {
            Log.e(TAG, "getByMd5: ", e)
            Result.Error(e)
        }
    }

    override suspend fun getRandomFromServer(): Result<List<Music>> {
        return try {
            val musics = musicConfig.getRandom()
            Result.Success(musics)
        } catch (e: Exception) {
            Log.e(TAG, "getRandomFromServer: ", e)
            Result.Error(e)
        }
    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MusicDataSource(
                musicConfig = MusicConfig.INSTANCE
            )
        }
    }
}