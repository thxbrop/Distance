package com.unltm.distance

import androidx.appcompat.app.AppCompatActivity
import com.unltm.distance.base.ProgramSplitter

class MainActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        ProgramSplitter(this)
    }
}