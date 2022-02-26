package com.unltm.distance.ui.live

class LiveRoomNotPlayingException(roomId: Int, com: String) : Exception(
    "未开播或房间不存在 (RoomId=$roomId,From= ${com})."
)