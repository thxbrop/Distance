package com.unltm.distance.activity.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.repository.ConversationRepository
import kotlinx.coroutines.launch
import com.unltm.distance.activity.conversation.result.ConversationInformationResult as InformationResult

class ChatViewModel private constructor(
    private val conversationRepository: ConversationRepository
) : ViewModel() {
    private var _informationLive = MutableLiveData<InformationResult>()
    val informationLive: LiveData<InformationResult> get() = _informationLive

    fun getConversation(id: String) {
        viewModelScope.launch {
            _informationLive.value = conversationRepository.getByIdFromCache(id)
            _informationLive.value = conversationRepository.getByIdFromServer(id)
        }
    }

    companion object {
        val INSTANCE = ChatViewModel(
            conversationRepository = ConversationRepository.INSTANCE
        )
    }
}