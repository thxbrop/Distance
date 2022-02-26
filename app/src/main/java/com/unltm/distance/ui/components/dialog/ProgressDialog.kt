package com.unltm.distance.ui.components.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.unltm.distance.R

class ProgressDialog(private val fragmentActivity: FragmentActivity) :
    Dialog(fragmentActivity, R.style.QRCodeDialog) {
    private val liveData = MutableLiveData(0)
    private lateinit var progressIndicator: LinearProgressIndicator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        setCancelable(false)
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        progressIndicator = findViewById(R.id.progress)

        liveData.observe(fragmentActivity) {
            progressIndicator.setProgress(it, true)
            if (it == 100) dismiss()
        }
    }

    fun submit(progress: Int) {
        liveData.postValue(progress)
    }

    override fun show() {
        if (!isShowing) super.show()
    }

    override fun dismiss() {
        if (isShowing) super.dismiss()
    }

}