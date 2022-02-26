package com.unltm.distance.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.base.ServerException
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

    fun login(email: String?, password: String?) {
        when {
            email.isNullOrBlank() -> {
                _loginResultLive.value =
                    LoginResult(error = ServerException.ERROR_SIGN_ILLEGAL_EMAIL)
            }
            password.isNullOrBlank() -> {
                _loginResultLive.value =
                    LoginResult(error = ServerException.ERROR_SIGN_ILLEGAL_PASSWORD)
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
                _signResultLive.value = SignResult(error = ServerException.ERROR_SIGN_ILLEGAL_EMAIL)
            }
            password.isNullOrBlank() -> {
                _signResultLive.value =
                    SignResult(error = ServerException.ERROR_SIGN_ILLEGAL_PASSWORD)
            }
            else -> {
                viewModelScope.launch {
                    _signResultLive.value = accountRepository.sign(email, password)
                }
            }
        }
    }

    fun clear() {
        _signResultLive.value = SignResult()
        _loginResultLive.value = LoginResult()
    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            LoginViewModel(
                accountRepository = AccountRepository.INSTANCE
            )
        }
    }
}