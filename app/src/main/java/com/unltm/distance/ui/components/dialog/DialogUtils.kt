package com.unltm.distance.ui.components.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.unltm.distance.R
import com.unltm.distance.base.contracts.checkContext

object DialogUtils {
    private val NOTIFY_POSITION_BUTTON =
        R.string.dialog_notify_position_button to DialogInterface.OnClickListener { _, _ -> }
    private val CALLBACK_POSITION_BUTTON =
        R.string.dialog_callback_position_button to DialogInterface.OnClickListener { _, _ -> }
    private val CALLBACK_NEGATIVE_BUTTON =
        R.string.dialog_callback_negative_button to DialogInterface.OnClickListener { _, _ -> }

    private fun showDialog(
        context: Context,
        title: String?, message: String?,
        positiveButton: Pair<Int, DialogInterface.OnClickListener>?,
        negativeButton: Pair<Int, DialogInterface.OnClickListener>?,
    ) {
        context.checkContext { activityContext ->
            AlertDialog.Builder(activityContext).apply {
                setTitle(title)
                setMessage(message)
                positiveButton?.let {
                    setPositiveButton(
                        context.getString(positiveButton.first),
                        positiveButton.second
                    )
                }
                negativeButton?.let {
                    setNegativeButton(
                        context.getString(negativeButton.first),
                        negativeButton.second
                    )
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
        positiveButton: Pair<Int, DialogInterface.OnClickListener> = CALLBACK_POSITION_BUTTON,
        negativeButton: Pair<Int, DialogInterface.OnClickListener> = CALLBACK_NEGATIVE_BUTTON,
    ) {
        showDialog(context, title, message, positiveButton, negativeButton)
    }

    fun showNotifyDialog(
        context: Context,
        title: String?, message: String?,
        positiveButton: Pair<Int, DialogInterface.OnClickListener> = NOTIFY_POSITION_BUTTON,
        negativeButton: Pair<Int, DialogInterface.OnClickListener>? = null,
    ) {
        showDialog(context, title, message, positiveButton, negativeButton)
    }

    fun showListDialog(
        context: Context,
        title: String?,
        list: Pair<Array<String>, DialogInterface.OnClickListener>,
        positiveButton: Pair<Int, DialogInterface.OnClickListener>? = CALLBACK_POSITION_BUTTON,
        negativeButton: Pair<Int, DialogInterface.OnClickListener>? = CALLBACK_NEGATIVE_BUTTON,
    ) {
        context.checkContext { context1 ->
            AlertDialog.Builder(context1).apply {
                setTitle(title)
                setItems(list.first, list.second)
                positiveButton?.let {
                    setPositiveButton(
                        context.getString(positiveButton.first),
                        positiveButton.second
                    )
                }
                negativeButton?.let {
                    setNegativeButton(
                        context.getString(negativeButton.first),
                        negativeButton.second
                    )
                }

                create().apply {
                    window?.let { window ->
                        window.attributes?.let {
                            it.windowAnimations = R.style.BaseDialog
                            it.gravity = Gravity.CENTER
                        }
                        window.setContentView(android.R.layout.select_dialog_item)
                        window.findViewById<TextView>(android.R.id.text1).apply {
                            maxLines = 1
                        }
                        window.setBackgroundDrawableResource(R.drawable.ripple_card_round)
                    }
                    show()
                }
            }
        }
    }


}