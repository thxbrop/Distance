package com.unltm.distance.activity.music

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unltm.distance.R
import com.unltm.distance.databinding.ItemMusicBinding
import com.unltm.distance.room.entity.Music

class MusicAdapter : ListAdapter<Music, MusicAdapter.MusicItem>(DIFF) {
    class MusicItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemMusicBinding = ItemMusicBinding.bind(itemView)
    }

    object DIFF : DiffUtil.ItemCallback<Music>() {
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
            return (oldItem.md5 == newItem.md5) && (oldItem.isDownloaded == newItem.isDownloaded)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicItem {
        return MusicItem(
            LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MusicItem, position: Int) {
        val item = getItem(position)
        holder.apply {
            binding.author.text = item.singer
            binding.content.text = item.title

        }
    }
}