package com.unltm.distance.activity.conversation.result

import com.unltm.distance.room.entity.Conversation

data class GetConversationsResult(
    val data: List<Conversation>? = null,
    val error: Exception? = null
)
