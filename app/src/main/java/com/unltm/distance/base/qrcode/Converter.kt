package com.unltm.distance.base.qrcode

import android.graphics.Bitmap
import com.unltm.distance.room.entity.User

object Converter {
    private const val USER = "USER_"
    fun encryptUser(user: User?): Bitmap? {
        return Util.createQRCodeBitmap("$USER${user?.id}", 360, 360)
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
