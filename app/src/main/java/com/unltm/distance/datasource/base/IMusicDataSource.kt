package com.unltm.distance.datasource.base

import com.unltm.distance.base.Result
import com.unltm.distance.room.entity.Music

interface IMusicDataSource {
    suspend fun getByMD5(md5: String): Result<Music>
    suspend fun getRandomFromServer(): Result<List<Music>>
}