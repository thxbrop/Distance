package com.unltm.distance.storage

import com.unltm.distance.room.MyDatabase
import com.unltm.distance.room.entity.Conversation

class ConversationStorage {
    private val conversationDao = MyDatabase.getInstance().getConversationDao()

    fun saveConversation(vararg conversation: Conversation) {
        conversationDao.insert(*conversation)
    }

    suspend fun removeConversation(vararg conversation: Conversation) {
        conversationDao.delete(*conversation)
    }

    suspend fun getAllConversations(): List<Conversation> {
        return conversationDao.getAllConversations()
    }

    suspend fun getConversationById(id: String): Conversation? {
        return conversationDao.getConversationById(id)
    }
}