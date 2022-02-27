package com.unltm.distance.base.jvm

import android.graphics.Bitmap
import com.unltm.distance.room.entity.User
import com.unltm.distance.room.entity.UserRich

object QRCodeConverter {
    private const val USER = "USER_"
    fun encryptUser(user: User?): Bitmap? {
        return QRCodeUtil.createQRCodeBitmap("$USER${user?.id}", 360, 360)
    }

    fun encryptUser(user: UserRich?): Bitmap? {
        return QRCodeUtil.createQRCodeBitmap("$USER${user?.id}", 360, 360)
    }

    fun decrypt(result: String?): Result {
        if (result == null) return Result.EMPTY_RESULT
        if (result.startsWith(USER)) {
            return decryptUser(result)
        }
        return Result(result)
    }

    private fun decryptUser(result: String): UserResult {
        return UserResult(result.drop(5))
    }

}

open class Result(
    val result: String?
) : CharSequence {
    companion object {
        val EMPTY_RESULT = Result(null)
    }

    override val length: Int
        get() = result?.length ?: 0

    override fun get(index: Int): Char {
        return result?.get(index) ?: throw IndexOutOfBoundsException()
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return result?.subSequence(startIndex, endIndex) ?: throw IndexOutOfBoundsException()
    }
}

data class UserResult(
    val objectId: String
) : Result(objectId)