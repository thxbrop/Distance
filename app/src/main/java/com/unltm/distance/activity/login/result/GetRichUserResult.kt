package com.unltm.distance.activity.login.result

import com.unltm.distance.room.entity.User

data class GetRichUserResult(
    val success: User? = null,
    val error: Exception? = null
)
