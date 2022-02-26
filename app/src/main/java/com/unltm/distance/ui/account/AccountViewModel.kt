package com.unltm.distance.ui.account

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.repository.AccountRepository
import com.unltm.distance.room.entity.User
import com.unltm.distance.room.entity.UserRich
import com.unltm.distance.ui.conversation.exception.AccountNotExistException
import com.unltm.distance.ui.conversation.result.GetCurrentUser
import com.unltm.distance.ui.login.result.GetRichUserResult
import kotlinx.coroutines.launch

class AccountViewModel private constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {
    private var _currentUserLive = MutableLiveData<GetCurrentUser>()
    val currentUserLive: LiveData<GetCurrentUser> = _currentUserLive

//    private var _uploadHeadPictureLive = MutableLiveData<UploadTask>()
//    val uploadHeadPictureLive: LiveData<UploadTask> = _uploadHeadPictureLive

    private var _richInfoLive = MutableLiveData<GetRichUserResult>()
    val richUserResult: LiveData<GetRichUserResult> = _richInfoLive

    fun getCurrentUser() {
        viewModelScope.launch {
            val allAccount = accountRepository.getCurrentUser()
            _currentUserLive.value = if (allAccount.isEmpty()) {
                GetCurrentUser(error = AccountNotExistException())
            } else {
                GetCurrentUser(success = allAccount)
            }
        }
    }

    fun getRichInfo(user: User) {
        viewModelScope.launch {
            _richInfoLive.value = accountRepository.getRichInfo(user)
        }
    }

    fun updateRichInfo(userRich: UserRich) {
        _richInfoLive.value = GetRichUserResult(success = userRich)
    }

    fun uploadHeadPicture(bitmap: Bitmap) {
        //_uploadHeadPictureLive.value = fileDataSource.upload(bitmap)
        TODO()
    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AccountViewModel(
                accountRepository = AccountRepository.INSTANCE
            )
        }
    }
}