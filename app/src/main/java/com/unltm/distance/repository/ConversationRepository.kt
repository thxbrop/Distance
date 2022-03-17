package com.unltm.distance.repository

import com.unltm.distance.base.Result
import com.unltm.distance.datasource.ConversationDataSource
import com.unltm.distance.storage.ConversationStorage
import com.unltm.distance.ui.conversation.result.CreateConversationResult
import com.unltm.distance.ui.conversation.result.GetConversationResult
import com.unltm.distance.ui.conversation.result.GetConversationsResult
import com.unltm.distance.ui.conversation.useCase.CreateConversationUseCase
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

    suspend fun createConversation(useCase: CreateConversationUseCase): CreateConversationResult {
        return conversationDataSource.create(
            userId = useCase.creator,
            invitedIds = useCase.invitedIds,
            name = useCase.name,
            simpleName = useCase.simpleName
        ).let {
            when (it) {
                is Result.Success -> CreateConversationResult(data = it.data)
                is Result.Error -> CreateConversationResult(error = it.exception)
            }
        }
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