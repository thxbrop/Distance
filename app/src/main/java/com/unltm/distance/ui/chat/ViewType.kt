package com.unltm.distance.ui.chat

import androidx.annotation.IntDef

@IntDef(
    ViewType.TEXT_LEFT,
    ViewType.TEXT_RIGHT,
    ViewType.IMAGE_LEFT,
    ViewType.IMAGE_RIGHT,
    ViewType.AUDIO_LEFT,
    ViewType.AUDIO_RIGHT
)
internal annotation class ViewType {
    companion object {
        internal const val TEXT_LEFT = 0
        internal const val TEXT_RIGHT = 1
        internal const val IMAGE_LEFT = 2
        internal const val IMAGE_RIGHT = 3
        internal const val AUDIO_LEFT = 4
        internal const val AUDIO_RIGHT = 5
    }
}