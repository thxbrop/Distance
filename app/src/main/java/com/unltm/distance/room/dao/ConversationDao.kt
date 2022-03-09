package com.unltm.distance.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.unltm.distance.room.entity.Conversation

@Dao
interface ConversationDao {
    @Insert(onConflict = REPLACE)
    fun insert(vararg message: Conversation)

    @Delete
    suspend fun delete(vararg message: Conversation)

    @Query("SELECT * FROM Conversation ORDER BY lastMessageAt")
    suspend fun getAllConversations(): List<Conversation>

    @Query("SELECT * FROM Conversation WHERE id = :id")
    suspend fun getConversationById(id: String): Conversation?
}