package com.unltm.distance.ui.live.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.base.Result
import com.unltm.distance.datasource.LiveDataSource
import com.unltm.distance.datasource.gson.Com
import com.unltm.distance.ui.live.GetRealUriException
import com.unltm.distance.ui.live.LiveRoomNotPlayingException
import com.unltm.distance.ui.live.result.GetRealUriResult
import kotlinx.coroutines.launch

class PlayerViewModel private constructor(
    private val liveDataSource: LiveDataSource
) : ViewModel() {
    private var _realUriLive = MutableLiveData<GetRealUriResult>()
    val realUriLive: LiveData<GetRealUriResult> = _realUriLive

    fun clearAll() {
        _realUriLive.value = GetRealUriResult()
    }

    fun getRealUri(roomId: Int, com: String) {
        viewModelScope.launch {
            when (val checkLiveStates = liveDataSource.checkLiveStates(roomId, com)) {
                is Result.Success ->
                    when (val result = liveDataSource.getRoomRealUri(com, roomId)) {
                        is Result.Success -> {
                            _realUriLive.value = GetRealUriResult(data = result.data)
                        }
                        is Result.Error -> {
                            _realUriLive.value =
                                GetRealUriResult(error = GetRealUriException(roomId, com))
                        }
                    }
                is Result.Error -> _realUriLive.value =
                    GetRealUriResult(error = LiveRoomNotPlayingException(roomId, com))
            }

        }
    }

    fun checkLiveStates(com: Com, roomId: Int) {

    }

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            PlayerViewModel(
                liveDataSource = LiveDataSource()
            )
        }
    }
}