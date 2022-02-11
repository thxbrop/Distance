package com.unltm.distance.repository

import com.unltm.distance.base.contracts.isNull
import com.unltm.distance.datasource.AuthDataSource
import com.unltm.distance.room.entity.User
import com.unltm.distance.storage.AuthStorage
import com.unltm.distance.ui.login.result.LoginResult
import com.unltm.distance.ui.login.result.SignResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepository private constructor(
    private val authDataSource: AuthDataSource,
    private val authStorage: AuthStorage
) {
    suspend fun login(email: String, password: String): LoginResult {
        return when (val result = authDataSource.login(email, password)) {
            is Result.Success -> {
                val firebaseUser = result.data.user!!
                val user = User(
                    id = firebaseUser.uid,
                    username = firebaseUser.displayName
                )
                withContext(Dispatchers.IO) {
                    authStorage.saveAccount(user)
                }
                LoginResult(success = user)
            }
            is Result.Error -> LoginResult(
                error = result.exception
            )
        }
    }

    suspend fun sign(email: String, password: String): SignResult {
        return when (val result = authDataSource.sign(email, password)) {
            is Result.Success -> {
                val firebaseUser = result.data.user!!
                val user = User(
                    id = firebaseUser.uid,
                    username = firebaseUser.displayName
                )
                withContext(Dispatchers.IO) {
                    authStorage.saveAccount(user)
                }
                SignResult(success = user)
            }
            is Result.Error -> SignResult(
                error = result.exception
            )
        }
    }

    suspend fun getCurrentUser(): List<User> {
        return authStorage.getAllAccount()
    }

    companion object {
        private var INSTANCE: AccountRepository? = null
        fun getInstance() = run {
            if (INSTANCE.isNull) {
                INSTANCE = AccountRepository(
                    authDataSource = AuthDataSource(),
                    authStorage = AuthStorage()
                )
            }
            INSTANCE!!
        }
    }
}