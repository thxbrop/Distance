package com.unltm.distance.ui.components.dialog.image

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.unltm.distance.R

abstract class BaseImageDialog(
    context: Context
) : Dialog(context, R.style.QRCodeDialog) {

    lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_x_photo)
        viewPager = findViewById(R.id.vp2)
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        viewPager.registerOnPageChangeCallback(ImageChangeCallback(currentPosition))
    }

    companion object {
        var currentPosition = 0
    }

}