package com.unltm.distance

import android.app.Application
import com.unltm.distance.base.CrashHandler

val application get() = MyApplication.INSTANCE

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        CrashHandler.getInstance().init(this)
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance())
    }

    companion object {
        lateinit var INSTANCE: MyApplication
    }
}