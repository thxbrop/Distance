package com.unltm.distance.ui.live

class GetRealUrlException(roomId: Int, com: String) : Exception(
    "直播间解析失败 (roomId=${roomId}, from=${com})."
)