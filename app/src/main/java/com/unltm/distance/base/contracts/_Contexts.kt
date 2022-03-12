package com.unltm.distance.base.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.ToastUtils
import com.unltm.distance.R
import com.unltm.distance.base.file.FileType
import okhttp3.*
import okio.BufferedSink
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import java.io.Serializable

fun Context.requirePermission(
    launcher: ActivityResultLauncher<String>,
    permission: String, lazy: (() -> Unit)? = null,
) {
    if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
        launcher.launch(permission)
    else lazy?.invoke()
}

inline fun <reified T : Activity> Context.startActivity(bundle: Pair<String, Serializable>? = null) {
    startActivity(Intent(this, T::class.java).apply {
        bundle?.also { putExtra(it.first, it.second) }
    })
    (this as Activity).overridePendingTransition(R.anim.slide_from_right, R.anim.popup_out)
}

inline fun <reified T : Activity> Context.startActivityWithString(bundle: Pair<String, String>? = null) {
    startActivity(Intent(this, T::class.java).apply {
        bundle?.also { putExtra(it.first, it.second) }
    })
    (this as Activity).overridePendingTransition(R.anim.slide_from_right, R.anim.popup_out)
}

fun Context.showToast(@StringRes stringRes: Int, resId: Int = R.drawable.live_state_primary) {
    ToastUtils.make()
        .setMode(ToastUtils.MODE.DARK)
        .setDurationIsLong(true)
        .setLeftIcon(resId)
        .show(getString(stringRes))
}

fun Context.showErrorToast(@StringRes stringRes: Int) {
    showToast(stringRes, R.drawable.live_state_warn)
}

fun Context.showErrorToast(s: String?) {
    showToast(s, R.drawable.live_state_warn)
}


fun Context.checkFileExist(uri: String?, saveAs: FileType): Boolean {
    if (uri.isNullOrBlank()) return false
    return File(getExternalFilesDir(saveAs.environment)?.absolutePath + '/' + uri.getFileName()).exists()
}

fun Context.download(uri: String?, saveAs: FileType, callback: (Throwable?) -> Unit) {
    if (uri.isNullOrBlank()) return
    val request = Request.Builder().url(uri.toHttps()!!).build()
    OkHttpClient().newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            val file: File?
            var buffer: BufferedSink? = null
            try {
                val filename = uri.getFileName()
                val path = getExternalFilesDir(saveAs.environment)?.path
                file = File(path, filename)
                buffer = file.sink().buffer()
                response.body()?.let { responseBody ->
                    buffer.writeAll(responseBody.source())
                    callback.invoke(null)
                }
            } catch (e: Exception) {
                callback.invoke(e)
            } finally {
                buffer?.close()
            }
        }

        override fun onFailure(call: Call, e: IOException) {
            callback.invoke(e)
        }
    })
}


fun showToast(stringRes: CharSequence?, resId: Int = R.drawable.live_state_primary) {
    ToastUtils.make()
        .setMode(ToastUtils.MODE.DARK)
        .setDurationIsLong(true)
        .setLeftIcon(resId)
        .show(stringRes)
}

fun showUnCompletedToast() {
    ToastUtils.make()
        .setMode(ToastUtils.MODE.DARK)
        .setDurationIsLong(true)
        .setLeftIcon(R.drawable.live_state_building)
        .show("当前功能未完善")
}

fun Context.colorRes(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun Context.checkContext(block: (Context) -> Unit) {
    if (this is FragmentActivity && !this.isDestroyed) {
        block.invoke(this)
    }
}
