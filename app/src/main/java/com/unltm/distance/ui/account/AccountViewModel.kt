package com.unltm.distance.ui.account

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.repository.AccountRepository
import com.unltm.distance.room.entity.User
import com.unltm.distance.ui.login.result.GetRichUserResult
import kotlinx.coroutines.launch

class AccountViewModel private constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

//    private var _uploadHeadPictureLive = MutableLiveData<UploadTask>()
//    val uploadHeadPictureLive: LiveData<UploadTask> = _uploadHeadPictureLive

    private var _richInfoLive = MutableLiveData<GetRichUserResult>()
    val richUserResult: LiveData<GetRichUserResult> = _richInfoLive

    fun getRichInfo(user: User) {
        viewModelScope.launch {
            _richInfoLive.value = accountRepository.getRichInfo(user)
        }
    }

    fun updateRichInfo(user: User) {
        _richInfoLive.value = GetRichUserResult(success = user)
    }

    fun uploadHeadPicture(bitmap: Bitmap) {
        //_uploadHeadPictureLive.value = fileDataSource.upload(bitmap)
        TODO()
    }

    fun clear() {
        _richInfoLive.value = GetRichUserResult()
    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AccountViewModel(
                accountRepository = AccountRepository.INSTANCE
            )
        }
    }
}