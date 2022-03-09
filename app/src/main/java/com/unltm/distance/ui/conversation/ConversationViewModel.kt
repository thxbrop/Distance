package com.unltm.distance.ui.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.repository.ConversationRepository
import com.unltm.distance.storage.AuthStorage
import com.unltm.distance.ui.conversation.exception.AccountNotExistException
import com.unltm.distance.ui.conversation.result.GetConversationResult
import com.unltm.distance.ui.conversation.result.GetCurrentUser
import kotlinx.coroutines.launch

class ConversationViewModel private constructor(
    private val authStorage: AuthStorage /** Just Read Local DB*/,
    private val conversationRepository: ConversationRepository
) : ViewModel() {
    private var _currentUserLive = MutableLiveData<GetCurrentUser>()
    val currentUserLive: LiveData<GetCurrentUser> = _currentUserLive

    private var _conversationLive = MutableLiveData<GetConversationResult>()
    val conversationLive: LiveData<GetConversationResult> = _conversationLive

    fun getCurrentUser() {
        viewModelScope.launch {
            val allAccount = authStorage.getAllAccount()
            _currentUserLive.value = if (allAccount.isEmpty()) {
                GetCurrentUser(error = AccountNotExistException())
            } else {
                GetCurrentUser(success = allAccount)
            }
        }
    }

    fun getConversationById(id: String) {
        viewModelScope.launch {
            conversationRepository.getByIdFromCache(id).also { result ->
                result.data?.let { _conversationLive.value = result }
                _conversationLive.value = conversationRepository.getByIdFromServer(id)
            }
        }
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