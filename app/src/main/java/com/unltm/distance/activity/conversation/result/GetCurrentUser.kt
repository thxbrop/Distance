package com.unltm.distance.activity.conversation.result

import com.unltm.distance.room.entity.User

data class GetCurrentUser(
    var data: List<User>? = null,
    var error: Exception? = null
)