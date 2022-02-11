package com.unltm.distance.datasource

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.core.net.toFile
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.unltm.distance.base.contracts.toFileQ
import com.unltm.distance.base.file.FileType
import java.io.ByteArrayOutputStream

class FileDataSource {
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    fun upload(file: Uri, type: FileType): UploadTask {
        val toFile =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) file.toFileQ() else file.toFile()
        val mountainImagesRef = storageRef.child("images/${file.lastPathSegment}")
        val uri = Uri.fromFile(toFile)
        return mountainImagesRef.putFile(uri)
    }

    fun upload(bitmap: Bitmap): UploadTask {
        val mountainImagesRef =
            storageRef.child("images/").child("${System.currentTimeMillis()}.png")
        val bas = ByteArrayOutputStream()
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            bas
        )

        return mountainImagesRef.putBytes(bas.toByteArray())
    }
}