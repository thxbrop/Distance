package com.unltm.distance.ui.settings.components

import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.unltm.distance.R
import com.unltm.distance.ui.components.ceil.Switch

internal class SwitchSetting(
    parent: LinearLayout,
    @StringRes title: Int,
    listener: (Switch) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.include_settings_switch, parent, false)
) {
    private val titleTextView: TextView = itemView.findViewById(R.id.title)
    private val checkbox: Switch = itemView.findViewById(R.id.checkbox)
    private val context = itemView.context

    init {
        parent.addView(itemView)
        titleTextView.text = context.getString(title)
        itemView.setOnClickListener {
            listener.invoke(checkbox)
            it.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
        }
    }

    fun setCheck(isChecked: Boolean) {
        checkbox.setChecked(isChecked, true)
    }
}
