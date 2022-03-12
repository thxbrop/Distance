package com.unltm.distance.datasource.base

import com.unltm.distance.base.Result
import com.unltm.distance.room.entity.Message

interface IMessageDataSource {
    suspend fun getById(id: String): Result<Message>
    suspend fun getByConversationId(id: String): Result<List<Message>>
}