package com.unltm.distance.components

import android.animation.TypeEvaluator
import android.graphics.Color

class HSVEvaluator : TypeEvaluator<Int> {
    private val startHsv = floatArrayOf()
    private val endHsv = floatArrayOf()
    override fun evaluate(fraction: Float, startValue: Int, endValue: Int): Int {
        Color.colorToHSV(startValue, startHsv)
        Color.colorToHSV(endValue, endHsv)
        val color = startHsv + (endValue - startValue) * fraction
        return Color.HSVToColor(color)
    }
}