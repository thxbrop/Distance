package com.unltm.distance.activity.chat.result

import com.unltm.distance.room.entity.Message

data class GetMessagesResult(
    val data: List<Message>? = null,
    val error: Exception? = null
)