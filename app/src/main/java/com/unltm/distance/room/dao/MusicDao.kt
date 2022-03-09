package com.unltm.distance.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.unltm.distance.room.entity.Music

@Dao
interface MusicDao {
    @Insert(onConflict = REPLACE)
    fun insert(vararg music: Music)

    @Delete
    suspend fun delete(vararg music: Music)

    @Query("SELECT * FROM Music ORDER BY title")
    suspend fun getAllMusics(): List<Music>

    @Query("SELECT * FROM Music WHERE md5 = :md5")
    suspend fun getMusicByMd5(md5: String): Music?
}