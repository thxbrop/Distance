package com.unltm.distance.ui.chat

import android.graphics.Typeface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unltm.distance.R
import com.unltm.distance.base.ServerException
import com.unltm.distance.base.contracts.buildTextBitmap
import com.unltm.distance.base.contracts.onInserted
import com.unltm.distance.base.contracts.setTextColorResource
import com.unltm.distance.base.contracts.showErrorToast
import com.unltm.distance.databinding.ActivityChatBinding
import com.unltm.distance.room.entity.Conversation
import com.unltm.distance.room.entity.Message
import com.unltm.distance.ui.GlobalViewModel
import com.unltm.distance.ui.chat.components.ConversationFinishDialog

class ChatActivity : AppCompatActivity() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var globalViewModel: GlobalViewModel

    private lateinit var binding: ActivityChatBinding
    private lateinit var mHeadImageView: ImageView
    private lateinit var mToolbar: Toolbar
    private lateinit var mTextSwitcher: TextSwitcher
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mEmojiButton: ImageButton
    private lateinit var mClipButton: ImageButton
    private lateinit var mEditText: EditText
    private lateinit var mEmojiList: RecyclerView
    private lateinit var mClipList: RecyclerView

    private lateinit var adapter: MessageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.init()
        viewModel = ChatViewModel.INSTANCE
        globalViewModel = GlobalViewModel.INSTANCE
        initBase()
        initObserver()
    }

    private fun ActivityChatBinding.init() {
        mHeadImageView = headPicture
        mToolbar = activityChatToolbar
        mTextSwitcher = textSwitcher.apply {
            setFactory {
                TextView(context).also {
                    it.typeface = Typeface.DEFAULT_BOLD
                    it.setTextColorResource(R.color.toolbar_text)
                    it.textSize = 18f
                }
            }
        }
        mRecyclerView = activityChatRecyclerview.apply {
            layoutManager = LinearLayoutManager(
                this@ChatActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
        mEditText = edittext
        mEmojiButton = emojiScan
        mClipButton = clip
        mEmojiList = emojiList
        mClipList = menuList
    }

    private fun initBase() {
        intent.apply {
            getStringExtra(CONVERSATION_ID)
                ?.let { viewModel.getConversation(it) }
                ?: ConversationFinishDialog(
                    this@ChatActivity,
                    ServerException.CONVERSATION_NOT_EXIST
                ) { finish() }
        }
    }

    private fun initObserver() {
        globalViewModel.accountLive.observe(this) { result ->
            adapter = MessageAdapter(result.first().id).also { adapter ->
                mRecyclerView.adapter = adapter
                adapter.onInserted { positionStart, itemCount ->
                    (mRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                        positionStart + itemCount - 1, 0
                    )
                }
            }
            initBase()
        }

        viewModel.getConversationLive.observe(this) { result ->
            result.data?.let { initConversation(it) }
            result.error?.let { showErrorToast(it.message) }
        }

    }


    private fun initConversation(conversation: Conversation) {
        mTextSwitcher.setText(conversation.name)
        mHeadImageView.setImageBitmap(buildTextBitmap(conversation.name))
        globalViewModel.messagesLive[conversation.id]?.observe(this) { result ->
            result.data?.let { initMessage(it) }
            result.error?.let { showErrorToast(it.message) }
        }
        globalViewModel.registerConversation(conversation.id)
        globalViewModel.getMessages(conversation.id)
    }

    private fun initMessage(list: List<Message>) {
        adapter.submitList(list)
    }

    companion object {
        const val CONVERSATION_ID = "intent:conversation_id"
        const val CONVERSATION_NAME = "intent:conversation_name"
    }

}

