package com.unltm.distance.activity

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.unltm.distance.activity.chat.result.GetMessagesResult
import com.unltm.distance.activity.conversation.result.GetConversationsResult
import com.unltm.distance.application
import com.unltm.distance.repository.AccountRepository
import com.unltm.distance.repository.ConversationRepository
import com.unltm.distance.repository.MessageRepository
import com.unltm.distance.room.entity.User
import kotlinx.coroutines.launch

val vm get() = GlobalViewModel.INSTANCE

class GlobalViewModel private constructor(
    private val conversationRepository: ConversationRepository = ConversationRepository.INSTANCE,
    private val messageRepository: MessageRepository = MessageRepository.INSTANCE,
    private val accountRepository: AccountRepository = AccountRepository.INSTANCE
) : AndroidViewModel(application) {
    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { GlobalViewModel() }
    }

    private var _conversationsLive = MutableLiveData<GetConversationsResult>()
    val conversationsLive: LiveData<GetConversationsResult> get() = _conversationsLive

    private var _messagesLive =
        mutableMapOf<String, MutableLiveData<GetMessagesResult>>().onEach { (conId, liveData) ->
            liveData.observeForever { result ->
                result.data?.let { messages ->
                    val conversations = _conversationsLive.value?.data?.toMutableList()
                    if (conversations != null) {
                        conversations.firstOrNull { con -> con.id == conId }?.also { conversation ->
                            val copy = conversation.copy(
                                lastMessageId = messages.last().id,
                                lastMessageAt = messages.last().updateAt
                            )
                            conversations.remove(conversation)
                            conversations.add(copy)
                            viewModelScope.launch {
                                _conversationsLive.value =
                                    GetConversationsResult(data = conversations)
                            }
                        }
                    }
                }
            }
        }
    val messagesLive: Map<String, LiveData<GetMessagesResult>> get() = _messagesLive

    private var _accountLive = MutableLiveData<List<User>>()
    val accountLive: LiveData<List<User>> get() = _accountLive
    val currentUser: User? get() = accountLive.value?.firstOrNull()

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

    /**
     * Get all my conversations from cache and server.
     * @see conversationsLive
     */
    fun getConversations() {
        viewModelScope.launch {
            _conversationsLive.value = conversationRepository.getMyConversations()
        }
    }

    /**
     * Get Messages from Cache and Server.
     * Make sure invoke [registerConversation] before this.
     * @param conId conversationId
     * @throws IllegalArgumentException If the conversation has not been registered
     * @see messagesLive
     */
    fun getMessages(conId: String) {
        viewModelScope.launch {
            requireNotNull(_messagesLive[conId])
            _messagesLive[conId]!!.value = messageRepository.getByConversationIdFromCache(conId)
            _messagesLive[conId]!!.value = messageRepository.getByConversationIdFromServer(conId)
        }
    }

    /**
     * Register to make sure messages and other notifies of the conversation could be observed.
     * @param conId conversationId
     */
    fun registerConversation(conId: String) {
        if (!_messagesLive.containsKey(conId)) {
            _messagesLive[conId] = MutableLiveData()
        }
    }

    fun unregisterConversation(conId: String) {
        if (_messagesLive.containsKey(conId)) {
            _messagesLive.remove(conId)
        }
    }

    /**
     * Get local accounts with sorted, the first one is the current user.
     * @see accountLive
     */
    fun getAccounts() {
        viewModelScope.launch {
            _accountLive.value = accountRepository.getCurrentUser()
        }
    }

    fun logoutAllAccount() {
        viewModelScope.launch {
            accountRepository.logoutAllAccount()
            _accountLive.value = emptyList()
        }
    }
}