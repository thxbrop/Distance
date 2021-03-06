package com.unltm.distance.datasource.config

import androidx.annotation.Keep
import com.unltm.distance.room.entity.User
import kotlinx.coroutines.suspendCancellableCoroutine

@Keep
class AuthConfig private constructor() : BaseConfig() {
    companion object {
        private val uri: String get() = baseUrl + "auth/"
        private const val LOGIN = "login"
        private const val SIGN = "sign"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private val loginUrl: String get() = uri + LOGIN
        private val signUrl: String get() = uri + SIGN

        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { AuthConfig() }
    }

    suspend fun login(email: String, password: String) =
        suspendCancellableCoroutine<User> {
            it.resumeStringRequest("$loginUrl?$KEY_EMAIL=$email&$KEY_PASSWORD=$password")
        }

    suspend fun sign(email: String, password: String) =
        suspendCancellableCoroutine<User> {
            it.resumeStringRequest("$signUrl?$KEY_EMAIL=$email&$KEY_PASSWORD=$password")
        }

}