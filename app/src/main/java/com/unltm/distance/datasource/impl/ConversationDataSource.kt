package com.unltm.distance.datasource.impl

import com.unltm.distance.base.Result
import com.unltm.distance.base.ServerException
import com.unltm.distance.datasource.IConversationDataSource
import com.unltm.distance.datasource.config.ConversationConfig
import com.unltm.distance.room.entity.Conversation

class ConversationDataSource private constructor(
    private val conversationConfig: ConversationConfig
) : IConversationDataSource {
    override suspend fun getById(id: String): Result<Conversation> {
        return try {
            Result.Success(conversationConfig.getById(id))
        } catch (e: ServerException) {
            Result.Error(e)
        }
    }

    override suspend fun create(
        userId: String,
        invitedIds: List<String>,
        name: String,
        simpleName: String
    ): Result<Conversation> {
        return try {
            Result.Success(conversationConfig.create(userId, invitedIds, name, simpleName))
        } catch (e: ServerException) {
            Result.Error(e)
        }
    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ConversationDataSource(
                conversationConfig = ConversationConfig.INSTANCE
            )
        }
    }
}