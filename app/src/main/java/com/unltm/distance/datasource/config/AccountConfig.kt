package com.unltm.distance.datasource.config

import androidx.annotation.Keep
import com.unltm.distance.base.file.UploadProgress
import com.unltm.distance.room.entity.User
import kotlinx.coroutines.suspendCancellableCoroutine

@Keep
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
        private const val KEY_FILETYPE = "fileType"
        private const val UPDATE = "update"
        private const val UPLOAD = "upload"
        private val infoUrl: String get() = uri + INFO
        private val updateUrl: String get() = uri + UPDATE
        private val uploadUrl: String get() = baseUrl + UPLOAD

        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { AccountConfig() }
    }

    @Deprecated("")
    suspend fun getRichInfo(user: User) =
        suspendCancellableCoroutine<User?> { it.resumeStringRequest("$infoUrl?$KEY_ID=${user.id}") }

    suspend fun getRichInfo(userId: String) =
        suspendCancellableCoroutine<User?> { it.resumeStringRequest("$infoUrl?$KEY_ID=${userId}") }

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
        coroutine.resumeStringRequest(s)
    }

    suspend fun uploadHeadPicture(
        id: String,
        base64: String
    ) = suspendCancellableCoroutine<UploadProgress> { coroutine ->
        val s = "$uploadUrl?$KEY_ID=$id&$KEY_FILETYPE=avatar"
        // FIXME: upload
        // coroutine.resumeDataWithRequestUrl(s, mutableMapOf("data" to base64))
    }
}