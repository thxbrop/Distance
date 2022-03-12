package com.unltm.distance.ui.chat.result

import com.unltm.distance.room.entity.Message

data class GetMessageResult(
    val data: Message? = null,
    val error: Exception? = null
)