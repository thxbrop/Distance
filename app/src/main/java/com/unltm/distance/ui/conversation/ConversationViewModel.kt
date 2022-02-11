package com.unltm.distance.ui.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.base.contracts.isNull
import com.unltm.distance.storage.AuthStorage
import com.unltm.distance.ui.conversation.result.GetCurrentUser
import kotlinx.coroutines.launch

class ConversationViewModel private constructor(
    private val authStorage: AuthStorage
) : ViewModel() {
    private var _currentUserLive = MutableLiveData<GetCurrentUser>()
    val currentUserLive: LiveData<GetCurrentUser> = _currentUserLive

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

    companion object {
        private var INSTANCE: ConversationViewModel? = null
        fun getInstance() = run {
            if (INSTANCE.isNull) {
                INSTANCE = ConversationViewModel(
                    authStorage = AuthStorage()
                )
            }
            INSTANCE!!

        }
    }
}