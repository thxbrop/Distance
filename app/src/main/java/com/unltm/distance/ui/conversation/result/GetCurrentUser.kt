package com.unltm.distance.ui.conversation.result

import com.unltm.distance.room.entity.User

data class GetCurrentUser(
    var success: List<User>? = null,
    var error: Exception? = null
)