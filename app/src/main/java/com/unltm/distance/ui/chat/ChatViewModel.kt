package com.unltm.distance.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.repository.ConversationRepository
import com.unltm.distance.ui.conversation.result.GetConversationResult
import kotlinx.coroutines.launch

class ChatViewModel private constructor(
    private val conversationRepository: ConversationRepository
) : ViewModel() {
    private var _getConversationLive = MutableLiveData<GetConversationResult>()
    val getConversationLive: LiveData<GetConversationResult> get() = _getConversationLive

    fun getConversation(id: String) {
        viewModelScope.launch {
            _getConversationLive.value = conversationRepository.getByIdFromCache(id)
            _getConversationLive.value = conversationRepository.getByIdFromServer(id)
        }
    }

    companion object {
        val INSTANCE = ChatViewModel(
            conversationRepository = ConversationRepository.INSTANCE
        )
    }
}