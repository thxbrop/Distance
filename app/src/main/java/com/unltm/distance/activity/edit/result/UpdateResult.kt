package com.unltm.distance.activity.edit.result

import com.unltm.distance.room.entity.User

data class UpdateResult(
    val data: User? = null,
    val error: Exception? = null
)
