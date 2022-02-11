package com.unltm.distance.base.contracts

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import com.unltm.distance.application
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.Q)
fun Uri.toFileQ(): File? =
    if (scheme == ContentResolver.SCHEME_FILE)
        File(requireNotNull(path))
    else if (scheme == ContentResolver.SCHEME_CONTENT) {
        val context = application.applicationContext
        val contentResolver = context.contentResolver
        val displayName = "${System.currentTimeMillis()}${Random.nextInt(0, 9999)}.${
            MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(this))
        }"
        val ios = contentResolver.openInputStream(this)
        if (ios != null) {
            File("${context.cacheDir.absolutePath}/$displayName")
                .apply {
                    val fos = FileOutputStream(this)
                    FileUtils.copy(ios, fos)
                    fos.close()
                    ios.close()
                }
        } else null
    } else null