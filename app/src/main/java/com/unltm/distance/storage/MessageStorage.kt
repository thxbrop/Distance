package com.unltm.distance.storage

import com.unltm.distance.room.MyDatabase
import com.unltm.distance.room.entity.Message

class MessageStorage {
    private val dao = MyDatabase.getInstance().getMessageDao()

    fun saveMessage(vararg message: Message) {
        dao.insert(*message)
    }

    suspend fun removeMessage(vararg message: Message) {
        dao.delete(*message)
    }

    suspend fun getAllMessages(): List<Message> {
        return dao.getAllMessages()
    }

    suspend fun getMessageById(id: String): Message? {
        return dao.getMessageById(id)
    }

    suspend fun getMessagesByConversationId(id: String): List<Message> {
        return dao.getMessagesByConversationId(id)
    }
}