package com.unltm.distance.activity.conversation.components

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.unltm.distance.R
import com.unltm.distance.base.contracts.errorToast
import com.unltm.distance.databinding.DialogCreateConversationBinding

class CreateConversationDialog(
    context: Context,
    private val submitListener: (Editable) -> Unit
) : AlertDialog(context, true, null) {
    private lateinit var binding: DialogCreateConversationBinding

    private var name: Editable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCreateConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window?.setBackgroundDrawable(null)
        binding.apply {
            include.edittext.setHint(R.string.cd_create_conversation_name)
            include.edittext.addTextChangedListener {
                name = it
            }
            submit.setOnClickListener {
                if (name.isNullOrBlank()) {
                    include.root.error = context.getString(R.string.illegal_name)
                } else {
                    try {
                        submitListener.invoke(name!!)
                    } catch (e: Exception) {
                        context.errorToast(e.message)
                    }
                    dismiss()
                }
            }
        }
    }
}