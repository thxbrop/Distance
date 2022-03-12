package com.unltm.distance.datasource

import com.unltm.distance.base.Result
import com.unltm.distance.base.ServerException
import com.unltm.distance.datasource.base.IMessageDataSource
import com.unltm.distance.datasource.config.MessageConfig
import com.unltm.distance.room.entity.Message

class MessageDataSource private constructor(
    private val config: MessageConfig
) : IMessageDataSource {
    override suspend fun getById(id: String): Result<Message> {
        return try {
            Result.Success(config.getById(id))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getByConversationId(id: String): Result<List<Message>> {
        return try {
            Result.Success(config.getByConversationId(id))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MessageDataSource(
                config = MessageConfig.INSTANCE
            )
        }
    }
}