package com.unltm.distance.datasource

import com.unltm.distance.base.Result
import com.unltm.distance.room.entity.Conversation

interface IConversationDataSource {
    suspend fun getById(id: String): Result<Conversation>
    suspend fun create(
        userId: String,
        invitedIds: List<String>,
        name: String,
        simpleName: String
    ): Result<Conversation>
}