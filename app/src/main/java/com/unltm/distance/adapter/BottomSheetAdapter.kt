package com.unltm.distance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unltm.distance.R

class BottomSheetAdapter :
    ListAdapter<BottomSheetAdapter.Setting, BottomSheetAdapter.ViewHolder>(DIFF) {

    private var onItemOnClickListener: ItemOnClickListener? = null

    data class Setting(
        @DrawableRes val icon: Int,
        @StringRes val title: Int,
        val onClickListener: View.OnClickListener? = null,
    )

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.item_bottom_sheet_icon)
        val title: TextView = itemView.findViewById(R.id.item_bottom_sheet_title)
    }

    object DIFF : DiffUtil.ItemCallback<Setting>() {
        override fun areItemsTheSame(oldItem: Setting, newItem: Setting): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Setting, newItem: Setting): Boolean {
            return oldItem.title == newItem.title
        }
    }

    interface ItemOnClickListener {
        fun onClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_bottom_sheet, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).apply {
            holder.icon.setImageResource(icon)
            holder.title.setText(title)
            holder.itemView.setOnClickListener {
                onItemOnClickListener?.onClick()
                onClickListener?.onClick(it)
            }
        }
    }

    fun setItemOnClickListener(listener: ItemOnClickListener) {
        this.onItemOnClickListener = listener
    }
}