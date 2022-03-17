package com.unltm.distance.adapter.bottomsheet

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SettingItem(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val badge: Int? = null,
    val onClickListener: View.OnClickListener? = null,
)