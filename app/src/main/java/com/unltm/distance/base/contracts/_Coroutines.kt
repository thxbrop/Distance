package com.unltm.distance.base.contracts

import kotlinx.coroutines.delay

suspend fun loop(duration: Long = 50, block: suspend () -> Unit) {
    while (true) {
        block.invoke()
        delay(duration)
    }
}
