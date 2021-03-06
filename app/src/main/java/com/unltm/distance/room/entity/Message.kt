package com.unltm.distance.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity
open class Message(
    @PrimaryKey
    open val id: String,
    open val conversationId: String,
    @Expose(serialize = false, deserialize = false)
    open val content: String,
    open val from: String,
    open val createdAt: Long = 0,
    open val updateAt: Long = createdAt,
)

data class TextMessage(
    override val id: String,
    override val conversationId: String,
    override val content: String,
    override val from: String,
    override val createdAt: Long = 0,
    override val updateAt: Long = createdAt,
    val text: String? = null
) : Message(id, conversationId, content, from, createdAt, updateAt)

data class ImageMessage(
    override val id: String,
    override val conversationId: String,
    override val content: String,
    override val from: String,
    override val createdAt: Long = 0,
    override val updateAt: Long = createdAt,
    val fileUrl: String
) : Message(id, conversationId, content, from, createdAt, updateAt)

data class AudioMessage(
    override val id: String,
    override val conversationId: String,
    override val content: String,
    override val from: String,
    override val createdAt: Long = 0,
    override val updateAt: Long = createdAt,
    val fileUrl: String
) : Message(id, conversationId, content, from, createdAt, updateAt)