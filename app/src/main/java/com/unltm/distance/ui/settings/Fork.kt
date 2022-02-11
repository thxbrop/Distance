package com.unltm.distance.ui.settings

import java.io.Serializable

internal sealed class Fork : Serializable {
    object Notification : Fork()
    object PrivacySafe : Fork()
    object Theme : Fork()
    object Cache : Fork()
    object Unknown : Fork()
}