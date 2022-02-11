package com.unltm.distance.ui.account.result

import java.io.File

data class UploadHeadPicture(
    val file: File? = null,
    val error: Exception? = null,
    val progress: Int? = null
)