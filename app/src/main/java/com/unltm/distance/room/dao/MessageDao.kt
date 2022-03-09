package com.unltm.distance.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.unltm.distance.room.entity.Message
import com.unltm.distance.room.entity.User

@Dao
interface MessageDao {
    @Insert(onConflict = REPLACE)
    fun insert(vararg message: Message)

    @Delete
    suspend fun delete(vararg message: Message)

    @Query("SELECT * FROM Message ORDER BY createdAt")
    suspend fun getAllMessages(): List<Message>

    @Query("SELECT * FROM Message WHERE id = :id")
    suspend fun getMessageById(id: String): Message?
}