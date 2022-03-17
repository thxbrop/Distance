package com.unltm.distance.repository

import com.unltm.distance.base.Result
import com.unltm.distance.base.ServerException
import com.unltm.distance.datasource.AccountDataSource
import com.unltm.distance.datasource.AuthDataSource
import com.unltm.distance.datasource.config.AccountConfig
import com.unltm.distance.datasource.config.AuthConfig
import com.unltm.distance.room.entity.User
import com.unltm.distance.storage.AccountStorage
import com.unltm.distance.storage.AuthStorage
import com.unltm.distance.ui.edit.result.UpdateResult
import com.unltm.distance.ui.login.result.GetRichUserResult
import com.unltm.distance.ui.login.result.LoginResult
import com.unltm.distance.ui.login.result.SignResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepository private constructor(
    private val authDataSource: AuthDataSource,
    private val authStorage: AuthStorage,
    private val accountDataSource: AccountDataSource,
    private val accountStorage: AccountStorage
) {
    suspend fun login(email: String, password: String): LoginResult {
        return when (val result = authDataSource.login(email, password)) {
            is Result.Success -> {
                val user = result.data
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
                val user = result.data
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

    suspend fun getRichInfo(user: User): GetRichUserResult {
        return when (val result = accountDataSource.getRichInfo(user)) {
            is Result.Success -> {
                val userRich = result.data
                withContext(Dispatchers.IO) {
                    accountStorage.saveAccount(userRich)
                }
                GetRichUserResult(success = userRich)
            }
            is Result.Error -> GetRichUserResult(
                error = ServerException.NOT_FOUND_RICH_USER
            )
        }
    }

    suspend fun updateInfo(
        id: String,
        username: String? = null,
        email: String? = null,
        password: String? = null,
        phoneNumber: Long? = null,
        introduce: String? = null
    ): UpdateResult {
        return when (val result =
            accountDataSource.updateInfo(id, username, email, password, phoneNumber, introduce)) {
            is Result.Success -> {
                withContext(Dispatchers.IO) {
                    authStorage.saveAccount(result.data)
                }
                UpdateResult(data = result.data)
            }
            is Result.Error -> UpdateResult(error = result.exception)
        }
    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AccountRepository(
                authDataSource = AuthDataSource(
                    authConfig = AuthConfig.INSTANCE,
                ),
                authStorage = AuthStorage(),
                accountStorage = AccountStorage(),
                accountDataSource = AccountDataSource(
                    accountConfig = AccountConfig.INSTANCE
                )
            )
        }
    }
}