package com.unltm.distance.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blankj.utilcode.util.FileUtils
import com.google.android.exoplayer2.util.MimeTypes
import com.unltm.distance.base.file.FileType
import com.unltm.distance.base.file.Files
import java.io.File

@Entity
data class Music(
    @PrimaryKey val md5: String,
    val title: String,
    val singer: String,
    val url: String,
    var localPath: String? = null
) {
    val isDownloaded
        get() = run {
            localPath?.let {
                FileUtils.isFileExists(File(it)).also { isExists ->
                    if (!isExists) localPath = null
                }
            } ?: false
        }

    fun download() {
        if (isDownloaded) return
        val fileName = url.subSequence(url.lastIndexOf('/'), url.length)
        localPath = Files.MusicPath + fileName
        Files.downloadHTTP(
            url, title, singer,
            File(Files.MusicPath),
            MimeTypes.AUDIO_WAV
        )
    }

    fun saveTo(fileType: FileType): SaveResult {
        return when (fileType) {
            is FileType.Music -> {
                SaveResult(filePath = Files.MusicPath)
            }
            is FileType.Ringtones -> {
                SaveResult(filePath = Files.RingtonesPath)
            }
            is FileType.Downloads -> {
                SaveResult(filePath = Files.DownloadsPath)
            }
            else -> SaveResult(error = "")
        }
    }

    data class SaveResult(
        val filePath: String? = null,
        val error: String? = null
    )
}
