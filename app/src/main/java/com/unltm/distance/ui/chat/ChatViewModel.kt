package com.unltm.distance.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.base.ServerException
import com.unltm.distance.repository.AccountRepository
import com.unltm.distance.repository.ConversationRepository
import com.unltm.distance.repository.MessageRepository
import com.unltm.distance.ui.chat.result.GetMessagesResult
import com.unltm.distance.ui.conversation.result.GetConversationResult
import com.unltm.distance.ui.conversation.result.GetCurrentUser
import kotlinx.coroutines.launch

class ChatViewModel private constructor(
    private val accountRepository: AccountRepository,
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private var _getCurrentUserLive = MutableLiveData<GetCurrentUser>()
    val getCurrentUserLive: LiveData<GetCurrentUser> get() = _getCurrentUserLive

    private var _getConversationLive = MutableLiveData<GetConversationResult>()
    val getConversationLive: LiveData<GetConversationResult> get() = _getConversationLive

    private var _getMessagesLive = MutableLiveData<GetMessagesResult>()
    val getMessagesLive: LiveData<GetMessagesResult> get() = _getMessagesLive

    fun getCurrentUser() {
        viewModelScope.launch {
            accountRepository.getCurrentUser().also {
                _getCurrentUserLive.value =
                    if (it.isNullOrEmpty()) GetCurrentUser(error = ServerException.AUTH_NOT_EXIST)
                    else GetCurrentUser(data = it)
            }
        }
    }

    fun getConversation(id: String) {
        viewModelScope.launch {
            _getConversationLive.value = conversationRepository.getByIdFromCache(id)
            _getConversationLive.value = conversationRepository.getByIdFromServer(id)
        }
    }

    fun getMessages(id: String) {
        viewModelScope.launch {
            _getMessagesLive.value = messageRepository.getByConversationIdFromCache(id)
            _getMessagesLive.value = messageRepository.getByConversationIdFromServer(id)
        }
    }

    companion object {
        val INSTANCE = ChatViewModel(
            accountRepository = AccountRepository.INSTANCE,
            conversationRepository = ConversationRepository.INSTANCE,
            messageRepository = MessageRepository.INSTANCE
        )
    }
}