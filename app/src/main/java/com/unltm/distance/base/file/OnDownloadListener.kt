package com.unltm.distance.base.file

import androidx.annotation.IntRange

interface OnDownloadListener {
    fun onSuccess()
    fun onFailed(throwable: Throwable)
    fun onProcess(@IntRange(from = 0, to = 100) progress: Int)
}