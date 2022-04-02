package com.unltm.distance.repository

import com.unltm.distance.base.Result
import com.unltm.distance.datasource.MessageDataSource
import com.unltm.distance.storage.MessageStorage
import com.unltm.distance.activity.chat.result.GetMessageResult
import com.unltm.distance.activity.chat.result.GetMessagesResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageRepository private constructor(
    private val dataSource: MessageDataSource,
    private val storage: MessageStorage
) {
    suspend fun getByConversationIdFromServer(id: String): GetMessagesResult {
        return when (val result = dataSource.getByConversationId(id)) {
            is Result.Success -> {
                withContext(Dispatchers.IO) {
                    storage.saveMessage(*result.data.toTypedArray())
                }
                GetMessagesResult(data = result.data)
            }
            is Result.Error -> GetMessagesResult(error = result.exception)
        }
    }

    suspend fun getByConversationIdFromCache(id: String): GetMessagesResult {
        return GetMessagesResult(data = storage.getMessagesByConversationId(id))
    }

    suspend fun getByIdFromServer(id: String): GetMessageResult {
        return when (val result = dataSource.getById(id)) {
            is Result.Success -> {
                withContext(Dispatchers.IO) {
                    storage.saveMessage(result.data)
                }
                GetMessageResult(data = result.data)
            }
            is Result.Error -> GetMessageResult(error = result.exception)
        }
    }

    suspend fun getByIdFromCache(id: String): GetMessageResult {
        return GetMessageResult(data = storage.getMessageById(id))
    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MessageRepository(
                dataSource = MessageDataSource.INSTANCE,
                storage = MessageStorage()
            )
        }
    }
}