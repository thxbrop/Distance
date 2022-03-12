package com.unltm.distance.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Conversation(
    @PrimaryKey
    val id: String = System.currentTimeMillis().toString(),
    val name: String,
    val simpleName: String,
    val createdAt: Long = System.currentTimeMillis(),
    val lastMessageId: String? = null,
    val lastMessageAt: Long? = null
)
