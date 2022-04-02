package com.unltm.distance.ui.dialog

import android.content.Context
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.unltm.distance.R
import com.unltm.distance.base.contracts.checkContext

object DialogUtils {
    private val NOTIFY_POSITION_BUTTON = R.string.dialog_notify_position_button to { }
    private val CALLBACK_POSITION_BUTTON = R.string.dialog_callback_position_button to { }
    private val CALLBACK_NEGATIVE_BUTTON = R.string.dialog_callback_negative_button to { }

    private fun showDialog(
        context: Context,
        title: String?, message: String?,
        positiveButton: Pair<Int, () -> Unit>?,
        negativeButton: Pair<Int, () -> Unit>?
    ) {
        context.checkContext { activityContext ->
            AlertDialog.Builder(activityContext).apply {
                setTitle(title)
                setMessage(message)
                positiveButton?.let {
                    setPositiveButton(
                        context.getString(positiveButton.first)
                    ) { _, _ -> positiveButton.second() }
                }
                negativeButton?.let {
                    setNegativeButton(
                        context.getString(negativeButton.first)
                    ) { _, _ -> negativeButton.second() }
                }
                create().apply {
                    window?.let {
                        it.setWindowAnimations(R.style.BaseDialog)
                        it.setGravity(Gravity.CENTER)
                        it.setBackgroundDrawableResource(R.drawable.ripple_card_round)
                    }
                    show()
                }
            }
        }
    }

    fun showCallbackDialog(
        context: Context,
        title: String?, message: String?,
        positiveButton: Pair<Int, () -> Unit> = CALLBACK_POSITION_BUTTON,
        negativeButton: Pair<Int, () -> Unit> = CALLBACK_NEGATIVE_BUTTON,
    ) {
        showDialog(context, title, message, positiveButton, negativeButton)
    }

    fun showNotifyDialog(
        context: Context,
        title: String?, message: String?,
        positiveButton: Pair<Int, () -> Unit> = NOTIFY_POSITION_BUTTON,
        negativeButton: Pair<Int, () -> Unit>? = null,
    ) {
        showDialog(context, title, message, positiveButton, negativeButton)
    }

    fun showListDialog(
        context: Context,
        title: String?,
        list: Pair<Array<String>, (Int) -> Unit>,
        positiveButton: Pair<Int, () -> Unit>? = CALLBACK_POSITION_BUTTON,
        negativeButton: Pair<Int, () -> Unit>? = CALLBACK_NEGATIVE_BUTTON,
    ) {
        context.checkContext { context1 ->
            AlertDialog.Builder(context1).apply {
                setTitle(title)
                setItems(list.first) { _, which -> list.second(which) }
                positiveButton?.let {
                    setPositiveButton(
                        context.getString(positiveButton.first)
                    ) { _, _ ->
                        positiveButton.second()
                    }
                }
                negativeButton?.let {
                    setNegativeButton(
                        context.getString(negativeButton.first)
                    ) { _, _ ->
                        negativeButton.second()
                    }
                }

                create().apply {
                    window?.let { window ->
                        window.attributes?.let {
                            it.windowAnimations = R.style.BaseDialog
                            it.gravity = Gravity.CENTER
                        }
                        window.setContentView(android.R.layout.select_dialog_item)
                        window.findViewById<TextView>(android.R.id.text1).apply { maxLines = 1 }
                        window.setBackgroundDrawableResource(R.drawable.ripple_card_round)
                    }
                    show()
                }
            }
        }
    }
}