package com.unltm.distance.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.base.contracts.isNull
import com.unltm.distance.repository.AccountRepository
import com.unltm.distance.ui.login.result.LoginResult
import com.unltm.distance.ui.login.result.SignResult
import kotlinx.coroutines.launch

class LoginViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {
    private var _loginResultLive = MutableLiveData<LoginResult>()
    val loginResultLive: LiveData<LoginResult> = _loginResultLive

    private var _signResultLive = MutableLiveData<SignResult>()
    val signResultLive: LiveData<SignResult> = _signResultLive

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResultLive.value = accountRepository.login(email, password)
        }
    }

    fun sign(email: String, password: String) {
        viewModelScope.launch {
            _signResultLive.value = accountRepository.sign(email, password)
        }
    }

    companion object {
        private var INSTANCE: LoginViewModel? = null
        fun getInstance() = run {
            if (INSTANCE.isNull) {
                INSTANCE = LoginViewModel(
                    accountRepository = AccountRepository.getInstance()
                )
            }
            INSTANCE!!
        }
    }
}