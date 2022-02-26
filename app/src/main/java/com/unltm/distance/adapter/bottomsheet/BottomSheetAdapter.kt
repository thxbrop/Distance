package com.unltm.distance.adapter.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unltm.distance.R

class BottomSheetAdapter :
    ListAdapter<SettingItem, BottomSheetAdapter.ViewHolder>(DIFF) {

    private var onItemClickListener: OnItemClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.item_bottom_sheet_icon)
        val title: TextView = itemView.findViewById(R.id.item_bottom_sheet_title)
    }

    object DIFF : DiffUtil.ItemCallback<SettingItem>() {
        override fun areItemsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
            return oldItem.title == newItem.title
        }
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
                onItemClickListener?.onClick()
                onClickListener?.onClick(it)
            }
        }
    }

    fun setItemOnClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
}