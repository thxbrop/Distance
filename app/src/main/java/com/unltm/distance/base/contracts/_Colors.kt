package com.unltm.distance.base.contracts

import android.animation.ArgbEvaluator
import kotlinx.coroutines.delay

suspend fun Int.transitionColor(value: Int, block: (Int) -> Unit) {
    fun each(fraction: Float) {
        val color = ArgbEvaluator().evaluate(fraction, this, value)
        block.invoke(color as Int)
    }

    var fraction = 0f
    while (fraction <= 1.0f) {
        when (fraction) {
            0.0f -> block.invoke(this)
            1.0f -> block.invoke(value)
            else -> each(fraction)
        }
        fraction += 0.1f
        delay(40)
    }
}
