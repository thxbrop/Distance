package com.unltm.distance.ui.conversation.result

import com.unltm.distance.room.entity.Conversation

data class CreateConversationResult(
    val success: Conversation? = null,
    val error: Exception? = null
)
