package com.unltm.distance.datasource.impl

import android.util.Log
import com.unltm.distance.base.Result
import com.unltm.distance.datasource.IAccountDataSource
import com.unltm.distance.datasource.config.AccountConfig
import com.unltm.distance.room.entity.User
import com.unltm.distance.room.entity.UserRich
import com.unltm.distance.ui.account.exception.GetRichInfoException

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
            } ?: return@run Result.Error(GetRichInfoException())
        } catch (e: Exception) {
            Log.e(TAG, "getRichInfo: ", e)
            Result.Error(e)
        }
    }

    override suspend fun updateInfo(
        id: String,
        username: String?,
        email: String?,
        password: String?
    ): Result<UserRich> = run {
        try {
            accountConfig.updateInfo(id, username, email, password).let {
                val toSafeTyped = it.toSafeTyped()
                Log.e(TAG, "updateInfo: $toSafeTyped")
                Result.Success(toSafeTyped)
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateInfo: ", e)
            Result.Error(e)
        }
    }
}