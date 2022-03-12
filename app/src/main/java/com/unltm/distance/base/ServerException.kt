package com.unltm.distance.base

data class ServerException(
    val code: Int,
    override val message: String
) : Exception(message) {
    companion object {
        val OK = ServerException(200, "")
        val ERROR_NETWORK = ServerException(201, "No network connection")
        val NOT_FOUND_USER = ServerException(202, "User not found")
        val NOT_FOUND_CONVERSATION = ServerException(203, "Conversation not found")

        val NOT_FOUND_RICH_USER = ServerException(302, "User Information not found")
        val NOT_FOUND_RICH_CONVERSATION =
            ServerException(303, "Conversation Information not found")

        val NOT_FOUND_FILE = ServerException(404, "Server File not found")

        val AUTH_NOT_EXIST = ServerException(500, "Auth is not exist")
        val USER_EXIST = ServerException(501, "User has exist")
        val ILLEGAL_EMAIL = ServerException(502, "Email is illegal")
        val ILLEGAL_PASSWORD = ServerException(503, "Password is illegal")
        val WRONG_PASSWORD = ServerException(504, "Password is wrong")
        val USER_NOT_EXIST = ServerException(505, "User is not exist")
        val CONVERSATION_NOT_EXIST = ServerException(506, "Conversation not exist")

        val ERROR_LIVE_PARSING = ServerException(701, "Live room parsing failed.")
        val ERROR_LIVE_SUPPORTED = ServerException(702, "Live room is not supported.")
        val ERROR_LIVE_NOT_PLAYING = ServerException(703, "Live room is not playing.")

        val ERROR_OTHER = ServerException(999, "")
    }
}