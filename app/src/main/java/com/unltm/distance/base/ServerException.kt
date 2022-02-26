package com.unltm.distance.base

data class ServerException(
    val code: Int,
    override val message: String
) : Exception(message) {
    companion object {
        val OK = ServerException(200, "")
        val ERROR_NETWORK = ServerException(201, "No network")
        val ERROR_User_NOT_FOUND = ServerException(202, "User not found")
        val ERROR_CONVERSATION_NOT_FOUND = ServerException(203, "Conversation not found")

        val ERROR_RICH_USER_NOT_FOUND = ServerException(302, "User Information not found")
        val ERROR_RICH_CONVERSATION_NOT_FOUND =
            ServerException(303, "Conversation Information not found")

        val ERROR_FILE_NOT_FOUND = ServerException(404, "Server File not found")

        val ERROR_SIGN_USER_EXIST = ServerException(501, "User has exist")
        val ERROR_SIGN_ILLEGAL_EMAIL = ServerException(502, "Email is illegal")
        val ERROR_SIGN_ILLEGAL_PASSWORD = ServerException(503, "Password is illegal")
        val ERROR_LOGIN_WRONG_PASSWORD = ServerException(504, "Password is wrong")
        val ERROR_LOGIN_USER_EXIST = ServerException(505, "User is not exist")
    }
}