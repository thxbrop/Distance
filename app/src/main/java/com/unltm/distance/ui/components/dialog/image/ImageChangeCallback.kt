package com.unltm.distance.ui.components.dialog.image

import androidx.viewpager2.widget.ViewPager2

data class ImageChangeCallback(
    private var currentPosition: Int
) : ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        currentPosition = position
    }
}