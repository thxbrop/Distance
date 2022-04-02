package com.unltm.distance.activity.account

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.activity.account.result.GetHeadPicturesResult
import com.unltm.distance.activity.login.result.GetRichUserResult
import com.unltm.distance.base.Result
import com.unltm.distance.base.contracts.toBase64
import com.unltm.distance.base.contracts.toBitmap
import com.unltm.distance.base.file.UploadProgress
import com.unltm.distance.repository.AccountRepository
import com.unltm.distance.room.entity.User
import com.unltm.distance.storage.AuthStorage
import kotlinx.coroutines.launch

class AccountViewModel(
    private val accountRepository: AccountRepository = AccountRepository.INSTANCE,
    private val authStorage: AuthStorage = AuthStorage()
) : ViewModel() {
    private var _uploadedHeadPictureLive = MutableLiveData<Result<UploadProgress>>()
    val uploadedHeadPictureLive: LiveData<Result<UploadProgress>> = _uploadedHeadPictureLive

    private var _informationLive = MutableLiveData<GetRichUserResult>()
    val informationResult: LiveData<GetRichUserResult> = _informationLive

    @Deprecated("")
    fun getInformation(user: User) {
        viewModelScope.launch {
            _informationLive.value =
                GetRichUserResult(success = authStorage.getAccountById(user.id))
            _informationLive.value = accountRepository.getRichInfo(user)
        }
    }

    fun getInformation(userId: String) {
        viewModelScope.launch {
            _informationLive.value =
                GetRichUserResult(success = authStorage.getAccountById(userId))
            _informationLive.value = accountRepository.getRichInfo(userId)
        }
    }

    private var _headPicturesLive = MutableLiveData<GetHeadPicturesResult>()
    val headPicturesLive: LiveData<GetHeadPicturesResult> get() = _headPicturesLive

    fun getHeadPictures(urls: List<String>) {
        viewModelScope.launch {
            try {
                urls.map { it.toBitmap() }.also {
                    _headPicturesLive.value = GetHeadPicturesResult(data = it)
                }
            } catch (e: Exception) {
                _headPicturesLive.value = GetHeadPicturesResult(error = e)
            }
        }
    }

    fun uploadHeadPicture(id: String, uri: Uri) {
        viewModelScope.launch {
            _uploadedHeadPictureLive.value =
                accountRepository.uploadHeadImage(id, uri.toBitmap().toBase64())
        }
    }
}