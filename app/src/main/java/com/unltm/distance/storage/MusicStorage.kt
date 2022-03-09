package com.unltm.distance.storage

import com.unltm.distance.room.MyDatabase
import com.unltm.distance.room.dao.MusicDao
import com.unltm.distance.room.entity.Music

class MusicStorage {
    private var musicDao: MusicDao = MyDatabase.getInstance().getMusicDao()

    fun saveMusic(vararg user: Music) {
        musicDao.insert(*user)
    }

    suspend fun removeMusic(vararg user: Music) {
        musicDao.delete(*user)
    }

    suspend fun getAllMusics(): List<Music> {
        return musicDao.getAllMusics()
    }

    suspend fun getByMD5(md5: String): Music? {
        return musicDao.getMusicByMd5(md5)
    }

}