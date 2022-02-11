package com.unltm.distance.ui.login.result

import com.unltm.distance.room.entity.User

data class SignResult(
    var success: User? = null,
    var error: Exception? = null
)