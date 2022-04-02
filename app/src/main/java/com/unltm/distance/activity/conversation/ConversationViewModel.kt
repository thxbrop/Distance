package com.unltm.distance.activity.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.activity.conversation.useCase.CreateConversationUseCase
import com.unltm.distance.repository.ConversationRepository
import kotlinx.coroutines.launch
import com.unltm.distance.activity.conversation.result.ConversationInformationResult as InformationResult
import com.unltm.distance.activity.conversation.result.CreateConversationResult as CreateResult

class ConversationViewModel(
    private val conversationRepository: ConversationRepository = ConversationRepository.INSTANCE
) : ViewModel() {

    private var _informationLive = MutableLiveData<InformationResult>()
    val informationLive: LiveData<InformationResult> = _informationLive

    private var _createLive = MutableLiveData<CreateResult>()
    val createLive: LiveData<CreateResult> get() = _createLive

    fun getConversationById(id: String) {
        viewModelScope.launch {
            conversationRepository.getByIdFromCache(id).also { result ->
                result.data?.let { _informationLive.value = result }
                _informationLive.value = conversationRepository.getByIdFromServer(id)
            }
        }
    }

    fun createConversation(useCase: CreateConversationUseCase) {
        viewModelScope.launch {
            _createLive.value = conversationRepository.createConversation(useCase)
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