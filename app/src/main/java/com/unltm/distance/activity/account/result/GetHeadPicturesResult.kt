package com.unltm.distance.activity.account.result

import android.graphics.Bitmap

data class GetHeadPicturesResult(
    val data: List<Bitmap>? = null,
    val error: Exception? = null
)
