package com.unltm.test

import android.app.Application

private lateinit var _application: MyApplication
val application get() = _application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        _application = this
    }
}