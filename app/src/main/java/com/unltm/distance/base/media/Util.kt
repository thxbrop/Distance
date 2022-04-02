package com.unltm.distance.base.media

import android.media.MediaPlayer

val mediaPlayer by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MediaPlayer() }

fun buildMediaPlayer(absolutePath: String, callback: MediaCallback) {
    mediaPlayer.apply {
        setDataSource(absolutePath)
        setOnCompletionListener {
            callback.onCompleted(it)
        }
        setOnPreparedListener {
            callback.onPrepared(it)
        }
        setOnErrorListener { mp, what, extra ->
            callback.onError(mp, what, extra)
            true
        }
    }
}