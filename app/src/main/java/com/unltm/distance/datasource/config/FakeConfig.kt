package com.unltm.distance.datasource.config

import com.unltm.distance.room.entity.Conversation

object FakeConfig {
    fun getConversations() = listOf(
        Conversation("1", "会话001", "hui"),
        Conversation("2", "会话002", "hui"),
        Conversation("3", "会话003", "hui"),
        Conversation("4", "会话004", "hui"),
    )
}