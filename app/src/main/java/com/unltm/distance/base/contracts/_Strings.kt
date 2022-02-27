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

fun String?.isPhoneNumber(): Boolean {
    if (this == null) return false
    return length == 11
}

fun Long?.formatPhoneNumber(areaCode: Int = 86): String? {
    if (this == null) return null
    return "+$areaCode ${toString().take(3)}-${
        toString().substring(3, 7)
    }-${toString().takeLast(4)}"
}
