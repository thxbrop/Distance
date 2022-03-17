package com.unltm.distance.ui.conversation.useCase

data class CreateConversationUseCase(
    val creator: String,
    val name: String,
    val simpleName: String = name,
    val invitedIds: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)