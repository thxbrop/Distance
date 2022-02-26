package com.unltm.distance.ui.live

class GetRealUriException(roomId: Int, com: String) : Exception(
    "直播间解析失败 (roomId=${roomId}, from=${com})."
)