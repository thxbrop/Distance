package com.unltm.distance.base.contracts

fun String?.toHttps(): String? {
    return when {
        this?.startsWith("https") == true -> this
        this != null -> {
            if (length <= 4) return null
            "https" + takeLast(length - 4)
        }
        else -> null
    }
}