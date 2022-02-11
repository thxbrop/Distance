package com.unltm.distance.base.file

import androidx.annotation.IntRange

interface OnDownloadListener {
    fun onSuccess()
    fun onFailed(throwable: Throwable)
    fun onDownloading(@IntRange(from = 0, to = 100) progress: Int)
}