package com.unltm.distance.activity.conversation.result

import com.unltm.distance.room.entity.Conversation

data class CreateConversationResult(
    val data: Conversation? = null,
    val error: Exception? = null
)
