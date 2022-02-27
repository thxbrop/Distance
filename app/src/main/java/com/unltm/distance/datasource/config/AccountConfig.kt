package com.unltm.distance.datasource.config

import com.unltm.distance.room.entity.User
import com.unltm.distance.room.entity.UserRich
import kotlinx.coroutines.suspendCancellableCoroutine

class AccountConfig : BaseConfig() {
    companion object {
        private val uri: String get() = baseUrl + "account/"
        private const val INFO = "info"
        private const val KEY_ID = "id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_PHONE_NUMBER = "phoneNumber"
        private const val UPDATE = "update"
        private val infoUrl: String get() = uri + INFO
        private val updateUrl: String get() = uri + UPDATE

        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { AccountConfig() }
    }

    suspend fun getRichInfo(user: User) =
        suspendCancellableCoroutine<UserRich?> { buildStringRequest("$infoUrl?$KEY_ID=${user.id}", it) }

    suspend fun updateInfo(
        id: String,
        username: String? = null,
        email: String? = null,
        password: String? = null,
        phoneNumber: Long? = null
    ) = suspendCancellableCoroutine<UserRich> { coroutine ->
        var s = "$updateUrl?$KEY_ID=$id&"
        username?.also { s += "$KEY_USERNAME=$it&" }
        email?.also { s += "$KEY_EMAIL=$it&" }
        password?.also { s += "$KEY_PASSWORD=$it" }
        phoneNumber?.also { s += "$KEY_PHONE_NUMBER=$it" }
        buildStringRequest(s, coroutine)
    }
}