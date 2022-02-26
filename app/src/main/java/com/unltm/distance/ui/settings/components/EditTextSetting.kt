package com.unltm.distance.ui.settings.components

import android.text.Editable
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.unltm.distance.R

internal class EditTextSetting(
    parent: LinearLayout,
    @StringRes title: Int,
    listener: (String) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(
            R.layout.include_settings_edittext,
            parent.findViewById(R.id.linear),
            false
        )
) {
    private val editText: TextInputEditText =
        itemView.findViewById<TextInputEditText?>(R.id.edittext).apply {
            addTextChangedListener {
                it?.let { listener.invoke(it.toString()) }
            }
        }

    init {
        itemView.findViewById<TextInputLayout>(R.id.textInputLayout).setHint(title)
        parent.addView(itemView)
    }

    fun setText(s: String) {
        editText.text = Editable.Factory.getInstance().newEditable(s)
    }
}