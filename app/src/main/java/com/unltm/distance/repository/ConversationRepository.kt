package com.unltm.distance.repository

import com.unltm.distance.base.Result
import com.unltm.distance.datasource.ConversationDataSource
import com.unltm.distance.storage.ConversationStorage
import com.unltm.distance.ui.conversation.result.GetConversationResult
import com.unltm.distance.ui.conversation.result.GetConversationsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConversationRepository private constructor(
    private val conversationDataSource: ConversationDataSource,
    private val conversationStorage: ConversationStorage
) {
    suspend fun getByIdFromServer(id: String): GetConversationResult {
        return when (val result = conversationDataSource.getById(id)) {
            is Result.Success -> {
                withContext(Dispatchers.IO) {
                    conversationStorage.saveConversation(result.data)
                }
                GetConversationResult(data = result.data)
            }
            is Result.Error -> GetConversationResult(error = result.exception)
        }
    }

    suspend fun getMyConversations(): GetConversationsResult {
        return GetConversationsResult(data = conversationStorage.getAllConversations())
    }

    suspend fun getByIdFromCache(id: String): GetConversationResult {
        return conversationStorage.getConversationById(id)?.let {
            GetConversationResult(data = it)
        } ?: GetConversationResult()
    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ConversationRepository(
                conversationDataSource = ConversationDataSource.INSTANCE,
                conversationStorage = ConversationStorage()
            )
        }
    }
}