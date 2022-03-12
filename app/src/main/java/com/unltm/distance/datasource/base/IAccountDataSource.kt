package com.unltm.distance.datasource.base

import com.unltm.distance.base.Result
import com.unltm.distance.room.entity.User
import com.unltm.distance.room.entity.UserRich

interface IAccountDataSource {
    suspend fun getRichInfo(user: User): Result<UserRich>
    suspend fun updateInfo(
        id: String,
        username: String? = null,
        email: String? = null,
        password: String? = null,
        phoneNumber: Long? = null,
        introduce: String?
    ): Result<UserRich>
}