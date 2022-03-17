package com.unltm.distance.base.file

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class DownloaderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "onReceive: ")
    }

    companion object {
        private const val TAG = "DownloaderBroadcastReceiver"
    }
}