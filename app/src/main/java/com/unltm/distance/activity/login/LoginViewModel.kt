package com.unltm.distance.activity.login

import androidx.lifecycle.*
import com.unltm.distance.activity.login.result.LoginResult
import com.unltm.distance.activity.login.result.SignResult
import com.unltm.distance.activity.vm
import com.unltm.distance.base.ServerException
import com.unltm.distance.repository.AccountRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val accountRepository: AccountRepository = AccountRepository.INSTANCE
) : ViewModel() {
    private var _loginResultLive = MutableLiveData<LoginResult>()
    val loginResultLive: LiveData<LoginResult> = _loginResultLive

    private val observeForever =
        Observer<LoginResult> { result -> result.success?.let { vm.getAccounts() } }


    private var _signResultLive = MutableLiveData<SignResult>()
    val signResultLive: LiveData<SignResult> = _signResultLive

    fun login(email: String?, password: String?) {
        when {
            email.isNullOrBlank() -> {
                _loginResultLive.value =
                    LoginResult(error = ServerException.ILLEGAL_EMAIL)
            }
            password.isNullOrBlank() -> {
                _loginResultLive.value =
                    LoginResult(error = ServerException.ILLEGAL_PASSWORD)
            }
            else -> {
                viewModelScope.launch {
                    _loginResultLive.value = accountRepository.login(email, password)
                }
            }
        }
    }

    fun sign(email: String?, password: String?) {
        when {
            email.isNullOrBlank() -> {
                _signResultLive.value = SignResult(error = ServerException.ILLEGAL_EMAIL)
            }
            password.isNullOrBlank() -> {
                _signResultLive.value =
                    SignResult(error = ServerException.ILLEGAL_PASSWORD)
            }
            else -> {
                viewModelScope.launch {
                    _signResultLive.value = accountRepository.sign(email, password)
                }
            }
        }
    }

    fun observerForever() {
        _loginResultLive.observeForever(observeForever)
    }
    fun removeObserverForever() {
        _loginResultLive.removeObserver(observeForever)
    }
}