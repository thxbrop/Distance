package com.unltm.distance.datasource

import android.util.Log
import com.unltm.distance.base.Result
import com.unltm.distance.base.ServerException
import com.unltm.distance.base.file.UploadProgress
import com.unltm.distance.datasource.base.IAccountDataSource
import com.unltm.distance.datasource.config.AccountConfig
import com.unltm.distance.room.entity.User

class AccountDataSource(
    private val accountConfig: AccountConfig
) : IAccountDataSource {
    companion object {
        private const val TAG = "AccountDataSource"
    }

    override suspend fun getRichInfo(user: User) = run {
        try {
            accountConfig.getRichInfo(user)?.let {
                Result.Success(it)
            } ?: return@run Result.Error(ServerException.NOT_FOUND_RICH_USER)
        } catch (e: Exception) {
            Log.e(TAG, "getRichInfo: ", e)
            Result.Error(e)
        }
    }

    override suspend fun getRichInfo(userId: String): Result<User> = run {
        try {
            accountConfig.getRichInfo(userId)?.let {
                Result.Success(it)
            } ?: return@run Result.Error(ServerException.NOT_FOUND_RICH_USER)
        } catch (e: Exception) {
            Log.e(TAG, "getRichInfo: ", e)
            Result.Error(e)
        }
    }

    override suspend fun updateInfo(
        id: String,
        username: String?,
        email: String?,
        password: String?,
        phoneNumber: Long?,
        introduce: String?
    ): Result<User> = run {
        try {
            accountConfig.updateInfo(id, username, email, password, phoneNumber, introduce).let {
                Result.Success(it)
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateInfo: ", e)
            Result.Error(e)
        }
    }

    override suspend fun uploadHeadPicture(id: String, base64: String): Result<UploadProgress> =
        run {
            try {
                accountConfig.uploadHeadPicture(id, base64).let {
                    Result.Success(it)
                }
            } catch (e: Exception) {
                Log.e(TAG, "uploadHeadPicture: ", e)
                Result.Error(e)
            }
        }
}