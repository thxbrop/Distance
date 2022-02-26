package com.unltm.distance.ui.login.result

import com.unltm.distance.room.entity.UserRich

data class GetRichUserResult(
    val success: UserRich? = null,
    val error: Exception? = null
)
