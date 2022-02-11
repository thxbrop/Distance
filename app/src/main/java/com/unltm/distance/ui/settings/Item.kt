package com.unltm.distance.ui.settings

import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.unltm.distance.R

internal class Item(
    @ColorRes private val color: Int,
    parent: LinearLayoutCompat,
    listener: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(
            R.layout.item_include_settings_theme_span,
            parent.findViewById(R.id.linear),
            false
        )
) {
    private val tint: ShapeableImageView = itemView.findViewById(R.id.tint)
    //val checked: ImageView = itemView.findViewById(R.id.checked)

    init {
        tint.setBackgroundResource(color)
        itemView.setOnClickListener {
            listener.invoke()
        }
    }
}