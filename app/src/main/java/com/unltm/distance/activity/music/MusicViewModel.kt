package com.unltm.distance.activity.music

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unltm.distance.repository.MusicRepository
import kotlinx.coroutines.launch

class MusicViewModel private constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private var _musicLiveData = MutableLiveData<MusicRepository.GetByMD5Result>()
    val musicLiveData: LiveData<MusicRepository.GetByMD5Result> get() = _musicLiveData

    private var _musicsLiveData = MutableLiveData<MusicRepository.GetMusicsResult>()
    val musicsLiveData: LiveData<MusicRepository.GetMusicsResult> get() = _musicsLiveData

    fun getMusicByMD5(md5: String) {
        viewModelScope.launch {
            _musicLiveData.value = musicRepository.getByMd5(md5)
        }
    }

    fun getRandomMusic() {
        viewModelScope.launch {
            _musicsLiveData.value = musicRepository.getMusicsRandomForServer()
        }
    }


    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MusicViewModel(
                musicRepository = MusicRepository.INSTANCE
            )
        }
    }
}