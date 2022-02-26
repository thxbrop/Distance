package com.unltm.distance.ui.settings.components

import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.unltm.distance.R
import com.unltm.distance.ui.components.dialog.DialogUtils

internal class TextSetting(
    parent: LinearLayout,
    @StringRes title: Int,
    private val items: Array<Int>,
    isDivided: Boolean = false,
    callback: (Int) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.include_settings_text, parent, false)
) {
    private val titleTextView: TextView = itemView.findViewById(R.id.title)
    private val valueTextView: TextView = itemView.findViewById(R.id.value)
    private val context = itemView.context

    init {
        parent.addView(itemView)
        titleTextView.text = context.getText(title)
        valueTextView.text = context.getText(items[0])
        if (isDivided) {
            val layoutParams = itemView.layoutParams as LinearLayout.LayoutParams
            val marginTop = itemView.resources.getDimension(R.dimen.margin_m).toInt()
            layoutParams.setMargins(0, marginTop, 0, 0)
        }
        itemView.setOnClickListener {
            show(title, items, callback)
        }
    }

    fun selectItem(position: Int) {
        valueTextView.text = context.getString(items[position])
    }

    private fun show(
        @StringRes title: Int,
        items: Array<Int>,
        callback: (Int) -> Unit
    ) {
        val string = context.getString(title)
        DialogUtils.showListDialog(
            context,
            string,
            items.map { context.getString(it) }.toTypedArray() to
                    DialogInterface.OnClickListener { _, which ->
                        selectItem(which)
                        callback.invoke(which)
                    },
            null
        )
    }
}
