package com.unltm.distance.ui.conversation

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.repository.ConversationRepository
import com.unltm.distance.storage.AuthStorage
import com.unltm.distance.ui.conversation.result.CreateConversationResult
import com.unltm.distance.ui.conversation.result.GetConversationResult
import kotlinx.coroutines.launch

class ConversationViewModel private constructor(
    private val authStorage: AuthStorage
    /** Just Read Local DB*/
    ,
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

    fun createConversation(editable: Editable) {

    }


    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ConversationViewModel(
                authStorage = AuthStorage(),
                conversationRepository = ConversationRepository.INSTANCE
            )
        }
    }
}