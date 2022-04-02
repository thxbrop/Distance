package com.unltm.distance.base.contracts

import android.view.Gravity
import androidx.transition.Fade
import androidx.transition.Slide

const val DURATION = 300L

val fade = Fade().apply {
    duration = DURATION
}

val slideFromStart = Slide(Gravity.START).apply {
    duration = DURATION
}
val slideFromTop = Slide(Gravity.TOP).apply {
    duration = DURATION
}