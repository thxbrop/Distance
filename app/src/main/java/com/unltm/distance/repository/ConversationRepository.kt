package com.unltm.distance.repository

import com.unltm.distance.base.Result
import com.unltm.distance.datasource.ConversationDataSource
import com.unltm.distance.storage.ConversationStorage
import com.unltm.distance.activity.conversation.result.CreateConversationResult
import com.unltm.distance.activity.conversation.result.ConversationInformationResult
import com.unltm.distance.activity.conversation.result.GetConversationsResult
import com.unltm.distance.activity.conversation.useCase.CreateConversationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConversationRepository private constructor(
    private val conversationDataSource: ConversationDataSource,
    private val conversationStorage: ConversationStorage
) {
    suspend fun getByIdFromServer(id: String): ConversationInformationResult {
        return when (val result = conversationDataSource.getById(id)) {
            is Result.Success -> {
                withContext(Dispatchers.IO) {
                    conversationStorage.saveConversation(result.data)
                }
                ConversationInformationResult(data = result.data)
            }
            is Result.Error -> ConversationInformationResult(error = result.exception)
        }
    }

    suspend fun getMyConversations(): GetConversationsResult {
        return GetConversationsResult(data = conversationStorage.getAllConversations())
    }

    suspend fun getByIdFromCache(id: String): ConversationInformationResult {
        return conversationStorage.getConversationById(id)?.let {
            ConversationInformationResult(data = it)
        } ?: ConversationInformationResult()
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