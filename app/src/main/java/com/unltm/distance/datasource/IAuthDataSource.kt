package com.unltm.distance.datasource

import com.unltm.distance.base.Result
import com.unltm.distance.room.entity.User

interface IAuthDataSource {
    suspend fun sign(email: String, password: String): Result<User>
    suspend fun login(email: String, password: String): Result<User>
}