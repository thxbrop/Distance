package com.unltm.distance.base.file

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import com.unltm.distance.application
import com.unltm.distance.base.contracts.requireSdk
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

object FileUtils {

    private val CachePath = "${application.applicationContext?.externalCacheDir?.absolutePath}"

    private fun getFilePath(fileType: FileType): String {
        return "${application.applicationContext?.getExternalFilesDir(fileType.environment)}"
    }

    @JvmStatic
    val MusicPath = getFilePath(FileType.Music)

    @JvmStatic
    val PicturesPath = getFilePath(FileType.Pictures)

    @JvmStatic
    val MoviesPath = getFilePath(FileType.Movies)

    @JvmStatic
    val DocumentsPath = getFilePath(FileType.Documents)

    @JvmStatic
    val DownloadsPath = getFilePath(FileType.Downloads)

    @JvmStatic
    val DCIMPath = getFilePath(FileType.DCIM)

    @JvmStatic
    val RingtonesPath = getFilePath(FileType.Ringtones)

    @JvmStatic
    val AlarmsPath = getFilePath(FileType.Alarms)

    @JvmStatic
    val NotificationsPath = getFilePath(FileType.Notifications)

    @JvmStatic
    val RecordPath = "$CachePath/Record"

    @JvmStatic
    val CrashPath = "$CachePath/Crash"

    fun saveBitmap2Gallery(
        context: Context,
        bitmap: Bitmap,
        title: String = "Distance",
        description: String? = null,
    ): Boolean {
        return requireSdk(Build.VERSION_CODES.Q, {
            val insert = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            ) ?: return@requireSdk false
            context.contentResolver.openOutputStream(insert).use {
                it ?: return@requireSdk false
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            true
        }, {
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                bitmap,
                title,
                description
            )
            true
        })
    }

    private fun download(url: String, os: OutputStream): Boolean {
        val urls = URL(url)
        (urls.openConnection() as HttpURLConnection).also { conn ->
            conn.requestMethod = "GET"
            conn.connectTimeout = 5 * 1000
            return if (conn.responseCode == 200) {
                conn.inputStream.use { ins ->
                    val buf = ByteArray(2048)
                    var len: Int
                    while (ins.read(buf).also { len = it } != -1) os.write(buf, 0, len)
                    os.flush()
                }
                true
            } else false
        }
    }

}