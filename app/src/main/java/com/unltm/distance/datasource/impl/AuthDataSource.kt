package com.unltm.distance.datasource.impl

import android.util.Log
import com.unltm.distance.base.Result
import com.unltm.distance.base.ServerException
import com.unltm.distance.datasource.IAuthDataSource
import com.unltm.distance.datasource.config.AuthConfig

class AuthDataSource(
    private val authConfig: AuthConfig
) : IAuthDataSource {
    companion object {
        private const val TAG = "AuthDataSourceImpl"
    }

    override suspend fun sign(email: String, password: String) = run {
        try {
            val user = authConfig.sign(email, password)
            Result.Success(user)
        } catch (e: Exception) {
            Log.e(TAG, "sign: ", e)
            Result.Error(e)
        }
    }

    override suspend fun login(email: String, password: String) = run {
        try {
            val user = authConfig.login(email, password)
            Result.Success(user)
        }catch (e: Exception) {
            Log.e(TAG, "login: ", e)
            Result.Error(e)
        }
    }
}