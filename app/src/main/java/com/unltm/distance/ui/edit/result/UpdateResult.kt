package com.unltm.distance.ui.edit.result

import com.unltm.distance.room.entity.UserRich

data class UpdateResult(
    val data: UserRich? = null,
    val error: Exception? = null
)
