package com.unltm.distance.ui.live.reco.editor

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.unltm.distance.R
import com.unltm.distance.base.contracts.colorRes
import com.unltm.distance.base.contracts.dp
import com.unltm.distance.base.contracts.setTextColorResource
import com.unltm.distance.base.contracts.setTextResource
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher

class TextEditor(
    context: Context,
    private val parentHeight: Int
) : BottomSheetDialog(context) {
    private lateinit var rootView: View
    private var callback: TextEditorCallback? = null

    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)

        setContentView(
            ConstraintLayout(context).apply {
                this@TextEditor.rootView = this
                layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    parentHeight
                )
                setBackgroundResource(R.color.backgroundSecondary)
                addView(
                    EditText(context).apply {
                        editText = this
                        id = R.id.id_issue_editor_edittext
                        layoutParams = ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                        ).also {
                            it.topToTop = PARENT_ID
                            it.startToStart = PARENT_ID
                            it.endToEnd = PARENT_ID
                            it.bottomToBottom = PARENT_ID
                            it.verticalBias = 0f
                            it.horizontalBias = 0f
                            it.setMargins(24.dp)
                        }
                        setPadding(18.dp)
                        background =
                            AppCompatResources.getDrawable(context, R.drawable.ripple_card_round)
                        backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                        setTextColor(Color.WHITE)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            textCursorDrawable =
                                AppCompatResources.getDrawable(
                                    context,
                                    R.drawable.text_cursor_white
                                )
                        }
                    }
                )
                addView(
                    TextView(context).apply {
                        id = R.id.id_issue_editor_text
                        layoutParams = ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                        ).also {
                            it.topToBottom = R.id.id_issue_editor_edittext
                            it.startToStart = R.id.id_issue_editor_edittext
                            it.endToEnd = R.id.id_issue_editor_edittext
                            it.horizontalBias = 0f
                            it.topMargin = 12.dp
                            it.marginStart = 18.dp
                            it.marginEnd = 18.dp
                        }
                        setTextColorResource(R.color.secondaryTextColor)
                        setTextResource(R.string.search_room_text)
                    }
                )
                val markwon = Markwon.create(context)
                val editor = MarkwonEditor.create(markwon)
                editText.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor))

                addView(
                    FloatingActionButton(context).apply {
                        contentDescription = context.getString(R.string.cd_submit)
                        setBackgroundResource(R.color.background)
                        foregroundTintList =
                            ColorStateList.valueOf(context.colorRes(R.color.floating_background))
                        compatElevation = 2f.dp
                        setImageResource(R.drawable.ic_round_send_24)
                        imageTintList = ColorStateList.valueOf(Color.WHITE)
                        layoutParams = ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply {
                            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                            setMargins(16.dp)
                        }
                        setOnClickListener {
                            val text = editText.text.toString()
                            if (text.isBlank()) return@setOnClickListener
                            callback?.onSubmit(text)
                            dismiss()
                        }
                    }
                )

                setOnDismissListener { callback?.onCancel() }

            }
        )
        window?.attributes = window?.attributes?.also { it.dimAmount = 0f }
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.peekHeight = parentHeight
    }


    fun setOnIssueCallback(callback: TextEditorCallback) {
        this.callback = callback
    }

    companion object {
        private const val PARENT_ID = ConstraintLayout.LayoutParams.PARENT_ID
    }

}