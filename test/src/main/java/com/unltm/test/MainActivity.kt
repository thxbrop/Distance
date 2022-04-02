package com.unltm.test

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.unltm.test.config.LiveConfig
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = javaClass.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            LiveConfig().getDouYinRealUrl("https://v.douyin.com/NxfMULC/").also {
                Log.e(TAG, it)
                findViewById<TextView>(R.id.text).text = it
            }
        }
    }
}