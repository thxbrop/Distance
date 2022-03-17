package com.unltm.distance.ui.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.repository.ConversationRepository
import com.unltm.distance.ui.conversation.result.CreateConversationResult
import com.unltm.distance.ui.conversation.result.GetConversationResult
import com.unltm.distance.ui.conversation.useCase.CreateConversationUseCase
import kotlinx.coroutines.launch

class ConversationViewModel private constructor(
    private val conversationRepository: ConversationRepository
) : ViewModel() {

    private var _conversationLive = MutableLiveData<GetConversationResult>()
    val conversationLive: LiveData<GetConversationResult> = _conversationLive

    private var _createConversationLive = MutableLiveData<CreateConversationResult>()
    val createConversationLive: LiveData<CreateConversationResult> get() = _createConversationLive

    fun getConversationById(id: String) {
        viewModelScope.launch {
            conversationRepository.getByIdFromCache(id).also { result ->
                result.data?.let { _conversationLive.value = result }
                _conversationLive.value = conversationRepository.getByIdFromServer(id)
            }
        }
    }

    fun createConversation(useCase: CreateConversationUseCase) {
        viewModelScope.launch {
            _createConversationLive.value = conversationRepository.createConversation(useCase)
        }
    }


    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ConversationViewModel(
                conversationRepository = ConversationRepository.INSTANCE
            )
        }
    }
}