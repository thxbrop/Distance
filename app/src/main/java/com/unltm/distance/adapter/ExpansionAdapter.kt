package com.unltm.distance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unltm.distance.R
import com.unltm.distance.base.contracts.colorRes

class ExpansionAdapter :
    ListAdapter<ExpansionAdapter.Expansion, ExpansionAdapter.ViewHolder>(DIFF) {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: AppCompatTextView = itemView.findViewById(R.id.title)
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val card: CardView = itemView.findViewById(R.id.card)
    }

    object DIFF : DiffUtil.ItemCallback<Expansion>() {
        override fun areItemsTheSame(oldItem: Expansion, newItem: Expansion): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Expansion, newItem: Expansion): Boolean {
            return oldItem.title == newItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_expansion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).apply {
            val context = holder.itemView.context
            holder.title.text = context.getString(title)
            holder.card.setCardBackgroundColor(context.colorRes(tint))
            holder.icon.setImageResource(icon)
            holder.itemView.setOnClickListener(onClickListener)
        }
    }

    data class Expansion(
        @StringRes val title: Int,
        @DrawableRes val icon: Int,
        @ColorRes val tint: Int,
        val onClickListener: View.OnClickListener? = null,
    )

}