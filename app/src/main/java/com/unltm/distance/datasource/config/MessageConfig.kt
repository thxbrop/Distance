package com.unltm.distance.datasource.config

import androidx.annotation.Keep
import com.unltm.distance.base.contracts.gson
import com.unltm.distance.room.entity.Message
import kotlinx.coroutines.suspendCancellableCoroutine

@Keep
class MessageConfig private constructor() : BaseConfig() {
    suspend fun getById(id: String) =
        suspendCancellableCoroutine<Message> {
            it.resumeStringRequest("$messageUrl?$KEY_ID=$id")
        }

    suspend fun getByConversationId(id: String) =
        suspendCancellableCoroutine<List<Message>> {
            it.resumeStringRequest("$messageUrl?$KEY_CONVERSATION_ID=$id")
        }

    suspend fun sendMessage(userId: String, conId: String, message: Message) =
        suspendCancellableCoroutine<List<Message>> {
            it.resumeStringRequest(
                "$sendMessageUrl?${KEY_USER_ID}=$$userId&$KEY_CONVERSATION_ID=$conId&$KEY_CONTENT=${
                    gson.toJson(message)
                }"
            )
        }


    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MessageConfig() }
        private const val KEY_ID = "id"
        private const val KEY_USER_ID = "userId"
        private const val KEY_CONVERSATION_ID = "conId"
        private const val KEY_CONTENT = "content"
        private val messageUrl: String get() = baseUrl + "message/"
        private val sendMessageUrl: String get() = baseUrl + "message/send"
    }
}