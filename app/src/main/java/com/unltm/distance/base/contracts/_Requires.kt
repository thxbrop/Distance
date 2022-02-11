package com.unltm.distance.base.contracts

import android.os.Build
import com.blankj.utilcode.util.NetworkUtils

inline fun requireSdk(target: Int, code: () -> Boolean, lower: () -> Boolean): Boolean {
    return if (Build.VERSION.SDK_INT >= target)
        code.invoke()
    else lower.invoke()
}

inline fun requireSdk(target: Int, code: () -> Unit) {
    if (Build.VERSION.SDK_INT >= target) code.invoke()
}

fun requireNetwork(lazy: (Boolean) -> Unit) {
    if (NetworkUtils.isConnected()) lazy.invoke(true)
    else lazy.invoke(false)
}
