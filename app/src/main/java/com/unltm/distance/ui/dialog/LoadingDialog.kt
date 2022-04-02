package com.unltm.distance.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.unltm.distance.R
import java.util.*

class LoadingDialog(context: Context) : Dialog(context, R.style.CustomDialog) {

    private lateinit var timer: Timer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        setContentView(R.layout.dialog_loading)
        timer = Timer()
        setOnShowListener {
//            timer.schedule(3000) {
//                if (isShowing) {
//                    val msg = context.getString(R.string.error_no_network)
//                    val resId = ContextCompat.getDrawable(context, R.drawable.live_state_warn)
//                    ToastUtils.make()
//                        .setMode(ToastUtils.MODE.DARK)
//                        .setDurationIsLong(true)
//                        .setLeftIcon(resId)
//                        .show(msg)
//                    dismiss()
//                    cancel()
//                }
//            }
        }
    }

}