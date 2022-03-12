package com.unltm.distance.ui

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.unltm.distance.application
import com.unltm.distance.repository.AccountRepository
import com.unltm.distance.repository.ConversationRepository
import com.unltm.distance.repository.MessageRepository
import com.unltm.distance.room.entity.User
import com.unltm.distance.ui.chat.result.GetMessagesResult
import com.unltm.distance.ui.conversation.result.GetConversationsResult
import kotlinx.coroutines.launch

class GlobalViewModel private constructor(
    private val conversationRepository: ConversationRepository = ConversationRepository.INSTANCE,
    private val messageRepository: MessageRepository = MessageRepository.INSTANCE,
    private val accountRepository: AccountRepository = AccountRepository.INSTANCE
) : AndroidViewModel(application) {
    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            GlobalViewModel()
        }
    }

    private var _conversationsLive = MutableLiveData<GetConversationsResult>()
    val conversationsLive: LiveData<GetConversationsResult> get() = _conversationsLive

    private var _messagesLive = MutableLiveData<Map<String, GetMessagesResult>>()
    val messagesLive: LiveData<Map<String, GetMessagesResult>> get() = _messagesLive

    private var _accountLive = MutableLiveData<List<User>>()
    val accountLive: LiveData<List<User>> get() = _accountLive

    fun changeCurrentAccount(id: String) {
        viewModelScope.launch {
            val allAccount = accountRepository.getCurrentUser()
            if (allAccount.size > 1) {
                if (allAccount.first().id != id) {
                    val list = allAccount.toMutableList()
                    list.firstOrNull { it.id == id }?.let { user ->
                        val sortedList = mutableListOf<User>()
                        sortedList.add(user)
                        list.remove(user)
                        sortedList.addAll(list)
                        _accountLive.value = sortedList
                    }
                }
            }
        }
    }

    fun getConversations() {
        viewModelScope.launch {
            _conversationsLive.value = conversationRepository.getMyConversations()
        }
    }

    fun getMessages(conId: String) {
        viewModelScope.launch {
            val old = _messagesLive.value?.toMutableMap() ?: mutableMapOf()
            old[conId] = messageRepository.getByConversationIdFromCache(conId)
            _messagesLive.value = old
            old[conId] = messageRepository.getByConversationIdFromServer(conId)
            _messagesLive.value = old
        }
    }

    fun getAccounts() {
        viewModelScope.launch {
            _accountLive.value = accountRepository.getCurrentUser()
        }
    }
}