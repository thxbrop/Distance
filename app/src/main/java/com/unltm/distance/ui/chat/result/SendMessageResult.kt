package com.unltm.distance.ui.chat.result

import com.unltm.distance.room.entity.Message

data class SendMessageResult(
    val success: Message? = null,
    val error: Int? = null
)