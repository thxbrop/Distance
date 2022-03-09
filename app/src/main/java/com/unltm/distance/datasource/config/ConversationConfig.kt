package com.unltm.distance.datasource.config

import com.unltm.distance.base.contracts.gson
import com.unltm.distance.room.entity.Conversation
import kotlinx.coroutines.suspendCancellableCoroutine

class ConversationConfig private constructor() : BaseConfig() {
    suspend fun getById(id: String) =
        suspendCancellableCoroutine<Conversation> {
            it.resumeWithRequestUrl("$queryUrl?$KEY_ID=$id")
        }

    suspend fun create(
        userId: String,
        invitedIds: List<String>,
        name: String = "Conversation_${System.currentTimeMillis()}",
        simpleName: String = name
    ) = suspendCancellableCoroutine<Conversation> {
        val invitedIdsGson = gson.toJson(invitedIds)
        it.resumeWithRequestUrl(
            "$createUrl?" +
                    "$KEY_USER_ID=$userId&" +
                    "$KEY_INVITED_IDS=$invitedIdsGson&" +
                    "$KEY_NAME=$name&" +
                    "$KEY_SIMPLE_NAME=$simpleName"
        )
    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ConversationConfig() }
        private const val KEY_ID = "id"
        private const val KEY_USER_ID = "userId"
        private const val KEY_INVITED_IDS = "invited_ids"
        private const val KEY_NAME = "name"
        private const val KEY_SIMPLE_NAME = "simple_name"
        private val queryUrl: String get() = baseUrl + "conversation"
        private val createUrl: String get() = queryUrl + "create"
    }
}