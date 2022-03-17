package com.unltm.distance.datasource.config

import com.unltm.distance.room.entity.User
import kotlinx.coroutines.suspendCancellableCoroutine

class AccountConfig private constructor() : BaseConfig() {
    companion object {
        private val uri: String get() = baseUrl + "account/"
        private const val INFO = "info"
        private const val KEY_ID = "id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_PHONE_NUMBER = "phoneNumber"
        private const val KEY_INTRODUCE = "introduce"
        private const val UPDATE = "update"
        private val infoUrl: String get() = uri + INFO
        private val updateUrl: String get() = uri + UPDATE

        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { AccountConfig() }
    }

    suspend fun getRichInfo(user: User) =
        suspendCancellableCoroutine<User?> { it.resumeWithRequestUrl("$infoUrl?$KEY_ID=${user.id}") }

    suspend fun updateInfo(
        id: String,
        username: String? = null,
        email: String? = null,
        password: String? = null,
        phoneNumber: Long? = null,
        introduce: String?
    ) = suspendCancellableCoroutine<User> { coroutine ->
        var s = "$updateUrl?$KEY_ID=$id&"
        username?.also { s += "$KEY_USERNAME=$it&" }
        email?.also { s += "$KEY_EMAIL=$it&" }
        password?.also { s += "$KEY_PASSWORD=$it" }
        phoneNumber?.also { s += "$KEY_PHONE_NUMBER=$it" }
        introduce?.also { s += "$KEY_INTRODUCE=$it" }
        coroutine.resumeWithRequestUrl(s)
    }
}