package com.unltm.distance.activity.settings

import java.io.Serializable

sealed class Fork : Serializable {
    object Notification : Fork()
    object PrivacySafe : Fork()
    object Theme : Fork()
    object Cache : Fork()
    object Proxy : Fork()
}