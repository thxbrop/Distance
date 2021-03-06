package com.unltm.distance.base.media

import android.media.MediaPlayer

interface MediaCallback {
    fun onPrepared(mediaPlayer: MediaPlayer)
    fun onCompleted(mediaPlayer: MediaPlayer)
    fun onError(mediaPlayer: MediaPlayer, what: Int, extra: Int)
}