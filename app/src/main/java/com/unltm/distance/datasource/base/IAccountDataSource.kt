package com.unltm.distance.datasource.base

import com.unltm.distance.base.Result
import com.unltm.distance.base.file.UploadProgress
import com.unltm.distance.room.entity.User

interface IAccountDataSource {
    @Deprecated("")
    suspend fun getRichInfo(user: User): Result<User>
    suspend fun getRichInfo(userId: String): Result<User>
    suspend fun updateInfo(
        id: String,
        username: String? = null,
        email: String? = null,
        password: String? = null,
        phoneNumber: Long? = null,
        introduce: String?
    ): Result<User>

    suspend fun uploadHeadPicture(
        id: String,
        base64: String
    ): Result<UploadProgress>
}