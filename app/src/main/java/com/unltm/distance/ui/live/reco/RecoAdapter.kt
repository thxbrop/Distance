package com.unltm.distance.ui.live.reco

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.unltm.distance.R
import com.unltm.distance.base.Result
import com.unltm.distance.base.contracts.loadHTTPS
import com.unltm.distance.datasource.LiveDataSource
import com.unltm.distance.ui.live.LivePreview
import com.unltm.distance.ui.live.player.PlayerActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecoAdapter : PagingDataAdapter<LivePreview, RecoAdapter.PreviewViewHolder>(DIFF) {
    private val liveDataSource = LiveDataSource()

    inner class PreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomName: TextView = itemView.findViewById(R.id.roomName)
        val ownerName: TextView = itemView.findViewById(R.id.ownerName)
        val thumb: ShapeableImageView = itemView.findViewById(R.id.thumb)
        val avatar: ShapeableImageView = itemView.findViewById(R.id.avatar)
        val state: TextView = itemView.findViewById(R.id.state)
        val content: Context = itemView.context
    }

    object DIFF : DiffUtil.ItemCallback<LivePreview>() {
        override fun areItemsTheSame(oldItem: LivePreview, newItem: LivePreview): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LivePreview, newItem: LivePreview): Boolean {
            return oldItem.roomStatus == newItem.roomStatus
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        return PreviewViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_live_pre, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        val cache = getItem(position)
        holder.apply {
            cache?.let {
                roomName.text = it.roomName
                ownerName.text = it.ownerName
                thumb.loadHTTPS(it.roomThumb)
                avatar.loadHTTPS(it.avatar)
                MainScope().launch(Dispatchers.IO) {
                    val result = liveDataSource.checkLiveStates(it.roomId, it.com)
                    if (result is Result.Success && result.data) {
                        withContext(Dispatchers.Main) {
                            state.isVisible = true
                        }
                    }
                }
                itemView.setOnClickListener {
                    Intent(content, PlayerActivity::class.java).apply {
                        putExtra(PlayerActivity.INTENT_ROOM, cache)
                        content.startActivity(this)
                    }
                }
            }

        }
    }
}