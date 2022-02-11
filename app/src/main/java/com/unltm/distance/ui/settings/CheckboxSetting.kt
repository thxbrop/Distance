package com.unltm.distance.ui.settings

import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.unltm.distance.R
import com.unltm.distance.base.contracts.setTextColorResource
import com.unltm.distance.base.contracts.setTextResource

internal class CheckboxSetting(
    parent: LinearLayout,
    @StringRes title: Int,
    enabled: Boolean = true
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(
            R.layout.include_settings_checkbox,
            parent.findViewById(R.id.linear),
            false
        )
) {
    private val titleTextView: TextView = itemView.findViewById(R.id.title)
    private val checkbox: MaterialCheckBox = itemView.findViewById(R.id.checkbox)
    //private val context = itemView.context

    init {
        parent.addView(itemView)
        titleTextView.setTextResource(title)
        if (enabled) {
            itemView.setOnClickListener {
                checkbox.isChecked = !checkbox.isChecked
                it.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
            }
        } else {
            checkbox.isEnabled = enabled
            titleTextView.setTextColorResource(R.color.secondaryTextColor)
        }
    }

    fun setCheck(isChecked: Boolean) {
        checkbox.isChecked = isChecked
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun isChecked() = checkbox.isChecked
}
