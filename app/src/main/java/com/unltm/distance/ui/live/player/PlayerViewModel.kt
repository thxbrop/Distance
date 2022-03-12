package com.unltm.distance.ui.live.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.base.Result
import com.unltm.distance.base.ServerException
import com.unltm.distance.datasource.LiveDataSource
import com.unltm.distance.datasource.gson.Platform
import com.unltm.distance.ui.live.result.GetRealUrlResult
import kotlinx.coroutines.launch

class PlayerViewModel private constructor(
    private val liveDataSource: LiveDataSource
) : ViewModel() {
    private var _realUriLive = MutableLiveData<GetRealUrlResult>()
    val realUrlLive: LiveData<GetRealUrlResult> = _realUriLive

    fun clearAll() {
        _realUriLive.value = GetRealUrlResult()
    }

    fun getRealUri(roomId: Int, com: String) {
        viewModelScope.launch {
            when (val checkLiveStates = liveDataSource.checkLiveStates(roomId, com)) {
                is Result.Success ->
                    when (val result = liveDataSource.getRoomRealUri(com, roomId)) {
                        is Result.Success -> {
                            _realUriLive.value = GetRealUrlResult(data = result.data)
                        }
                        is Result.Error -> {
                            _realUriLive.value =
                                GetRealUrlResult(error = ServerException.ERROR_LIVE_PARSING)
                        }
                    }
                is Result.Error -> _realUriLive.value =
                    GetRealUrlResult(error = ServerException.ERROR_LIVE_NOT_PLAYING)
            }

        }
    }

    fun checkLiveStates(platform: Platform, roomId: Int) {

    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            PlayerViewModel(
                liveDataSource = LiveDataSource()
            )
        }
    }
}