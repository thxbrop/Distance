package com.unltm.distance.base.contracts

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.annotation.IntRange
import androidx.annotation.RawRes
import com.unltm.distance.R

fun Context.soundIn() {
    sound(R.raw.sound_in, 1)
}

fun Context.soundOut() {
    sound(R.raw.sound_out, 1)
}

fun Context.soundJoin() {
    sound(R.raw.voicechat_join, 1)
}

fun Context.soundLeave() {
    sound(R.raw.voicechat_leave, 1)
}

fun Context.sound(@RawRes soundId: Int, @IntRange(from = 0, to = 16) priority: Int) {
    notificationSoundPoolBuilder.autoPause()
    notificationSoundPoolBuilder.load(this, soundId, 1)
    notificationSoundPoolBuilder.setOnLoadCompleteListener { soundPool, sampleId, status ->
        if (soundPool != null && status == 0) {
            soundPool.play(sampleId, 0.5f, 0.5f, priority, 0, 1f)
        }
    }
    notificationSoundPoolBuilder.autoResume()
}

private val notificationSoundPoolBuilder = run {
    val builder = SoundPool.Builder()
    val attrBuilder = AudioAttributes.Builder()
    attrBuilder.setUsage(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT)
    attrBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
    builder.setAudioAttributes(attrBuilder.build())
    builder.build()
}