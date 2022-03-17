package com.unltm.distance.ui.conversation

import android.app.Activity
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ClickUtils
import com.unltm.distance.R
import com.unltm.distance.base.contracts.buildTextBitmap
import com.unltm.distance.room.entity.Conversation
import com.unltm.distance.room.entity.Message
import com.unltm.distance.ui.chat.ChatActivity
import com.unltm.distance.ui.chat.ChatActivity.Companion.CONVERSATION_ID
import com.unltm.distance.ui.chat.ChatActivity.Companion.CONVERSATION_NAME

class ConversationAdapter : ListAdapter<Conversation, ConversationAdapter.ViewHolder>(DIFF) {
    private var onItemClickListener: OnItemClickListener? = null

    object DIFF : DiffUtil.ItemCallback<Conversation>() {
        override fun areItemsTheSame(
            oldItem: Conversation,
            newItem: Conversation,
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Conversation,
            newItem: Conversation,
        ): Boolean {
            return oldItem.lastMessageAt == newItem.lastMessageAt
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: AppCompatTextView = itemView.findViewById(R.id.title)
        val headPicture: ImageView = itemView.findViewById(R.id.head_picture)
        val context: AppCompatTextView = itemView.findViewById(R.id.context)
        val time: AppCompatTextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { conversation ->
            holder.apply {
                val content = itemView.context
                ClickUtils.applySingleDebouncing(itemView) {
                    onItemClickListener?.onClick(position)
                    val intent = Intent(
                        itemView.context,
                        ChatActivity::class.java
                    ).putExtra(CONVERSATION_ID, conversation.id)
                        .putExtra(CONVERSATION_NAME, conversation.name)
                    content.startActivity(intent)
                    (content as Activity).overridePendingTransition(
                        R.anim.slide_from_right,
                        R.anim.popup_out
                    )
                }
                title.text = conversation.name
//                when (conversation.lastMessage) {
//                    is TextMessage -> {
//                        context.text = (conversation.lastMessage as LCIMTextMessage).text
//                        context.typeface = Typeface.DEFAULT
//                        context.setTextColorResource(R.color.secondaryTextColor)
//                    }
//                    is ImageMessage -> {
//                        context.typeface = Typeface.DEFAULT_BOLD
//                        context.setTextColorResource(R.color.colorPrimaryText)
//                        context.setTextResource(R.string.image_message)
//                    }
//                    is AudioMessage -> {
//                        context.typeface = Typeface.DEFAULT_BOLD
//                        context.setTextColorResource(R.color.colorPrimaryText)
//                        context.setTextResource(R.string.audio_message)
//                    }
//                    else -> {
//                        context.typeface = Typeface.DEFAULT_BOLD
//                        context.setTextColorResource(R.color.colorPrimaryText)
//                        context.text = content.getString(R.string.unKnow_message)
//                    }
//                }
                time.text =
                    conversation.lastMessageAt?.let { DateUtils.getRelativeTimeSpanString(it) }
                        ?: "--"
                headPicture.setImageBitmap(buildTextBitmap(conversation.name))
            }

        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        this.onItemClickListener = object : OnItemClickListener {
            override fun onClick(position: Int) {
                listener.invoke(position)
            }
        }
    }
}