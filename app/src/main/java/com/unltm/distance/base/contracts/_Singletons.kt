package com.unltm.distance.base.contracts

import android.app.DownloadManager
import android.content.Context
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.unltm.distance.application

val volley by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Volley.newRequestQueue(application) }

val gson: Gson by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { GsonBuilder().serializeNulls().create() }

val downloadManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
}