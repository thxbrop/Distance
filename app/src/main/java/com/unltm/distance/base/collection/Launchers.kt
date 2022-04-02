package com.unltm.distance.base.collection

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

object Launchers {
    //context(ComponentActivity)
    fun selectMediaLauncher(activity: ComponentActivity, block: (Uri) -> Unit) =
        activity.registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { n -> n?.let { block.invoke(it) } }

    fun takePicturePreviewLauncher(activity: ComponentActivity, block: (Bitmap?) -> Unit) =
        activity.registerForActivityResult(ActivityResultContracts.TakePicturePreview(), block)

    fun requestPermissionLauncher(activity: ComponentActivity, block: ((Boolean) -> Unit)? = null) =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission(), block ?: {})
}