package com.unltm.distance.base.contracts

import android.app.DownloadManager
import android.net.Uri

fun downloadManagerRequest(uri: Uri) = DownloadManager.Request(uri)