package com.unltm.distance.activity.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.unltm.distance.R
import com.unltm.distance.base.contracts.loadHTTPS
import com.unltm.distance.base.contracts.setTime
import com.unltm.distance.room.entity.AudioMessage
import com.unltm.distance.room.entity.ImageMessage
import com.unltm.distance.room.entity.Message
import com.unltm.distance.room.entity.TextMessage
import com.unltm.distance.ui.wave.AXWaveView

class MessageAdapter(private val currentUserId: String) :
    ListAdapter<Message, MessageAdapter.MessageItem>(DIFF) {
    class MessageItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: AppCompatTextView? = itemView.findViewById(R.id.text)
        var time: AppCompatTextView = itemView.findViewById(R.id.time)
        val photo: AppCompatImageView? = itemView.findViewById(R.id.photo)
        val progress: LinearProgressIndicator? = itemView.findViewById(R.id.progress)
        val waveView: AXWaveView? = itemView.findViewById(R.id.wave)
        val cardView: CardView =
            (itemView as ConstraintLayout).children.first { it is CardView } as CardView
    }

    object DIFF : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.updateAt == newItem.updateAt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageItem {
        val resId = when (viewType) {
            ViewType.TEXT_LEFT -> R.layout.item_message_left
            ViewType.IMAGE_LEFT -> R.layout.item_message_photo_left
            ViewType.AUDIO_LEFT -> R.layout.item_message_record_left
            ViewType.TEXT_RIGHT -> R.layout.item_message_right
            ViewType.IMAGE_RIGHT -> R.layout.item_message_photo_right
            ViewType.AUDIO_RIGHT -> R.layout.item_message_record_right
            else -> R.layout.item_message_left
        }
        val inflate = LayoutInflater.from(parent.context).inflate(resId, parent, false)
        return MessageItem(inflate)
    }

    @ViewType
    override fun getItemViewType(position: Int): Int {
        when (val item = getItem(position)) {
            is TextMessage -> {
                return if (item.from != currentUserId) ViewType.TEXT_LEFT
                else ViewType.TEXT_RIGHT
            }
            is ImageMessage -> {
                return if (item.from != currentUserId) ViewType.IMAGE_LEFT
                else ViewType.IMAGE_RIGHT
            }
            is AudioMessage -> {
                return if (item.from != currentUserId) ViewType.AUDIO_LEFT
                else ViewType.AUDIO_RIGHT
            }
        }
        return ViewType.TEXT_LEFT
    }

    override fun onBindViewHolder(holder: MessageItem, position: Int) {
        val item = getItem(position)
        val viewType = getItemViewType(position)
        holder.apply {
            when (viewType) {
                ViewType.TEXT_LEFT, ViewType.TEXT_RIGHT -> {
                    item as TextMessage
                    text?.text = item.text
                }
                ViewType.IMAGE_LEFT, ViewType.IMAGE_RIGHT -> {
                    item as ImageMessage
                    photo?.loadHTTPS(item.fileUrl)
                }
                ViewType.AUDIO_LEFT, ViewType.AUDIO_RIGHT -> {
                    item as AudioMessage
                    //TODO()
                }
            }
            time.setTime(item.createdAt)
        }
    }
}