package com.unltm.distance.base.file

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

private const val TAG = "DownloaderBroadcastReceiver"

class DownloaderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "onReceive: ")
    }
}