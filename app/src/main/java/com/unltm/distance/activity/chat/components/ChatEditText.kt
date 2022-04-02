package com.unltm.distance.activity.chat.components

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.util.component1
import androidx.core.util.component2
import androidx.core.view.ViewCompat

typealias OnImageAddedListener = (contentUri: Uri, mimeType: String, label: String) -> Unit

private val SUPPORTED_MIME_TYPES = arrayOf(
    "image/jpeg",
    "image/jpg",
    "image/png",
    "image/gif"
)

class ChatEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {
    private var onImageAddedListener: OnImageAddedListener? = null

    init {
        ViewCompat.setOnReceiveContentListener(this, SUPPORTED_MIME_TYPES) { _, payload ->
            val (content, remaining) = payload.partition { it.uri != null }
            if (content != null) {
                val clip = content.clip
                val mineType =
                    SUPPORTED_MIME_TYPES.find { clip.description.hasMimeType(it) }
                if (mineType != null && clip.itemCount > 0) {
                    onImageAddedListener?.invoke(
                        clip.getItemAt(0).uri,
                        mineType,
                        clip.description.label.toString()
                    )
                }
            }
            remaining
        }
    }

    fun setOnImageAddListener(listener: OnImageAddedListener) {
        onImageAddedListener = listener
    }
}