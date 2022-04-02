package com.unltm.distance.base.file

import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi

sealed class FileType(val environment: String) {
    object Music : FileType(Environment.DIRECTORY_MUSIC)
    object Ringtones : FileType(Environment.DIRECTORY_RINGTONES)
    object Alarms : FileType(Environment.DIRECTORY_ALARMS)
    object Notifications : FileType(Environment.DIRECTORY_NOTIFICATIONS)
    object Pictures : FileType(Environment.DIRECTORY_PICTURES)
    object Movies : FileType(Environment.DIRECTORY_MOVIES)
    object Documents : FileType(Environment.DIRECTORY_DOCUMENTS)
    object Downloads : FileType(Environment.DIRECTORY_DOWNLOADS)
    object DCIM : FileType(Environment.DIRECTORY_DCIM)
    @RequiresApi(Build.VERSION_CODES.S)
    object Record : FileType(Environment.DIRECTORY_RECORDINGS)
}