package com.unltm.distance.ui.account

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.UploadTask
import com.unltm.distance.base.contracts.isNull
import com.unltm.distance.base.file.FileType
import com.unltm.distance.datasource.FileDataSource
import com.unltm.distance.storage.AuthStorage
import com.unltm.distance.ui.conversation.AccountNotExistException
import com.unltm.distance.ui.conversation.result.GetCurrentUser
import kotlinx.coroutines.launch

class AccountViewModel private constructor(
    private val authStorage: AuthStorage,
    private val fileDataSource: FileDataSource
) : ViewModel() {
    private var _currentUserLive = MutableLiveData<GetCurrentUser>()
    val currentUserLive: LiveData<GetCurrentUser> = _currentUserLive

    private var _uploadHeadPictureLive = MutableLiveData<UploadTask>()
    val uploadHeadPictureLive: LiveData<UploadTask> = _uploadHeadPictureLive

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

    fun uploadHeadPicture(file: Uri) {
        _uploadHeadPictureLive.value = fileDataSource.upload(file, FileType.Pictures)
    }

    fun uploadHeadPicture(bitmap: Bitmap) {
        _uploadHeadPictureLive.value = fileDataSource.upload(bitmap)
    }

    companion object {
        private var INSTANCE: AccountViewModel? = null
        fun getInstance() = run {
            if (INSTANCE.isNull) {
                INSTANCE = AccountViewModel(
                    authStorage = AuthStorage(),
                    fileDataSource = FileDataSource()
                )
            }
            INSTANCE!!
        }
    }
}