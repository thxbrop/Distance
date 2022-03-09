package com.unltm.distance.base.file

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.google.android.exoplayer2.util.MimeTypes
import com.unltm.distance.application
import com.unltm.distance.base.contracts.downloadManager
import com.unltm.distance.base.contracts.downloadManagerRequest
import com.unltm.distance.base.contracts.isNotNull
import com.unltm.distance.base.contracts.requireSdk
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

@Suppress("unused")
object Files {

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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageInQ(bitmap: Bitmap): Boolean = run {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream?
        var imageUri: Uri?
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }
        val contentResolver = application.contentResolver
        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }
        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 70, it) }
        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        imageUri?.also { contentResolver.update(it, contentValues, null, null) }.isNotNull
    }

    private fun saveImageLegacy(bitmap: Bitmap) = run {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, filename)
        val fos = FileOutputStream(image)
        fos.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
    }

    fun saveImage(bitmap: Bitmap): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) saveImageInQ(bitmap)
        else saveImageLegacy(bitmap)
    }

    fun saveImageLegacy(
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

    sealed class RequestMethod(val value: String) {
        object GET : RequestMethod("GET")
        object POST : RequestMethod("POST")
        object HEAD : RequestMethod("HEAD")
        object OPTIONS : RequestMethod("OPTIONS")
        object PUT : RequestMethod("PUT")
        object DELETE : RequestMethod("DELETE")
        object TRACE : RequestMethod("TRACE")
    }

    /**
     * @see MimeTypes
     */
    fun downloadHTTP(
        url: String,
        title: String,
        description: String?,
        file: File,
        mimeType: String
    ): Long? {
        return downloadManager?.let {
            val request = downloadManagerRequest(Uri.parse(url))
                .setTitle(title)
                .setDescription(description)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE or DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setMimeType(mimeType)
                .setDestinationUri(Uri.fromFile(file))
            it.enqueue(request)
        }
    }

    fun download(
        url: String,
        os: OutputStream,
        requestMethod: RequestMethod = RequestMethod.GET,
        connectionTimeout: Int = 5_000
    ): Boolean {
        val urls = URL(url)
        (urls.openConnection() as HttpURLConnection).also { conn ->
            conn.requestMethod = requestMethod.value
            conn.connectTimeout = connectionTimeout
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