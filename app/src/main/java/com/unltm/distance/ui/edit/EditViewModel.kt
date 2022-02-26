package com.unltm.distance.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.repository.AccountRepository
import com.unltm.distance.ui.edit.result.UpdateResult
import kotlinx.coroutines.launch

class EditViewModel private constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {
    private var _editLive = MutableLiveData<UpdateResult>()
    val updateResult: LiveData<UpdateResult> = _editLive
    fun edit(
        id: String,
        username: String? = null,
        email: String? = null,
        password: String? = null
    ) {
        viewModelScope.launch {
            _editLive.value = accountRepository.updateInfo(id, username, email, password)
        }
    }

    fun clear() {
        _editLive.value = UpdateResult()
    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            EditViewModel(
                accountRepository = AccountRepository.INSTANCE
            )
        }
    }
}