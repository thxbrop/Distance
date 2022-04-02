package com.unltm.distance.activity.chat.components

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.unltm.distance.base.ServerException
import com.unltm.distance.databinding.DialogConversationFinishBinding

class ConversationFinishDialog(
    context: Context,
    private val exception: ServerException,
    private val submitCallback: () -> Unit
) : AlertDialog(context, false, null) {
    private lateinit var binding: DialogConversationFinishBinding
    private lateinit var mTitleTextView: TextView
    private lateinit var mContentTextView: TextView
    private lateinit var mSubmitButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogConversationFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.init()
        mContentTextView.text = exception.message
        mSubmitButton.setOnClickListener {
            submitCallback.invoke()
        }
    }

    private fun DialogConversationFinishBinding.init() {
        mTitleTextView = title
        mContentTextView = content
        mSubmitButton = submit
    }

}