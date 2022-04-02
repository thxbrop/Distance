package com.unltm.distance.base.contracts

import android.os.Build
import com.blankj.utilcode.util.NetworkUtils

inline fun requiresApi(target: Int, code: () -> Boolean, lower: () -> Boolean): Boolean {
    return if (Build.VERSION.SDK_INT >= target)
        code.invoke()
    else lower.invoke()
}

inline fun requiresApi(target: Int, code: () -> Unit) {
    if (Build.VERSION.SDK_INT >= target) code.invoke()
}

inline fun <T> T.buildOnApi(target: Int, code: T.() -> Unit): T =
    let {
        if (Build.VERSION.SDK_INT >= target) {
            code.invoke(this)
        }
        this
    }


fun requireNetwork(lazy: (Boolean) -> Unit) {
    if (NetworkUtils.isConnected()) lazy.invoke(true)
    else lazy.invoke(false)
}

fun <T> withNotNull(t: T?, block: (T) -> Unit) = t?.let(block)

