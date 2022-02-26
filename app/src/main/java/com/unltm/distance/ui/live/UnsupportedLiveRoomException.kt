package com.unltm.distance.ui.live

class UnsupportedLiveRoomException(com: String?) : Exception(
    "The liveRoom is from $com,but current version is not supported it." +
            "Please consider update your Application to latest."
) {
}