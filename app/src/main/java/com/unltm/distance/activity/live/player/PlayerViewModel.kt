package com.unltm.distance.activity.live.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.activity.live.result.GetRealUrlResult
import com.unltm.distance.base.Result
import com.unltm.distance.base.ServerException
import com.unltm.distance.datasource.LiveDataSource
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val liveDataSource: LiveDataSource = LiveDataSource()
) : ViewModel() {
    private var _realUriLive = MutableLiveData<GetRealUrlResult>()
    val realUrlLive: LiveData<GetRealUrlResult> = _realUriLive

    fun getRealUri(roomId: Int, com: String) {
        viewModelScope.launch {
            when (liveDataSource.checkLiveStates(roomId, com)) {
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
}