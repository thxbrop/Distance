package com.unltm.distance.datasource.config

import com.unltm.distance.room.entity.Message
import kotlinx.coroutines.suspendCancellableCoroutine

class MessageConfig private constructor() : BaseConfig() {
    suspend fun getById(id: String) =
        suspendCancellableCoroutine<Message> {
            it.resumeWithRequestUrl("$messageUrl?$KEY_ID=$id")
        }

    suspend fun getByConversationId(id: String) =
        suspendCancellableCoroutine<List<Message>> {
            it.resumeWithRequestUrl("$messageUrl?$KEY_CONVERSATION_ID=$id")
        }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MessageConfig() }
        private const val KEY_ID = "id"
        private const val KEY_CONVERSATION_ID = "conId"
        private val messageUrl: String get() = baseUrl + "message/"
    }
}