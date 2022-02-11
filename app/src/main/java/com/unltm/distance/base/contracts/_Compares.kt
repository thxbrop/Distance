package com.unltm.distance.base.contracts

val <T> T.isNull get() = this == null
val <T> T.isNotNull get() = this != null

inline fun <T> T.requireEquals(value: T, block: (T) -> Unit) {
    if (this != value) return
    block.invoke(this)
}

inline fun <T> T.requireNotEquals(value: T, block: (T) -> Unit) {
    if (this == value) return
    block.invoke(this)
}