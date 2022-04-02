package com.unltm.distance.activity.chat.result

import com.unltm.distance.room.entity.Message

data class SendMessageResult(
    val success: Message? = null,
    val error: Int? = null
)