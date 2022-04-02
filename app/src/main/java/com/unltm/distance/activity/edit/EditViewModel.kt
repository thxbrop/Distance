package com.unltm.distance.activity.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.activity.edit.result.UpdateResult
import com.unltm.distance.repository.AccountRepository
import kotlinx.coroutines.launch

class EditViewModel(
    private val accountRepository: AccountRepository = AccountRepository.INSTANCE
) : ViewModel() {
    private var _updateLive = MutableLiveData<UpdateResult>()
    val updateResult: LiveData<UpdateResult> = _updateLive
    fun updateInformation(
        id: String,
        username: String? = null,
        email: String? = null,
        password: String? = null,
        phoneNumber: Long? = null,
        introduce: String? = null
    ) {
        viewModelScope.launch {
            _updateLive.value =
                accountRepository.updateInfo(id, username, email, password, phoneNumber, introduce)
        }
    }
}