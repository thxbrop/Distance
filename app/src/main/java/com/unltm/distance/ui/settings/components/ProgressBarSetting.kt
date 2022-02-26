package com.unltm.distance.ui.settings.components

import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.unltm.distance.R
import com.unltm.distance.base.contracts.setTextResource

internal class ProgressBarSetting(
    parent: LinearLayout,
    @StringRes title: Int,
    max: Int,
    current: Int,
    onPercentChangeCallback: (Int) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(
            R.layout.include_settings_progressbar,
            parent.findViewById(R.id.linear),
            false
        )
) {
    private val titleTextView: TextView = itemView.findViewById(R.id.title)
    private val percent: TextView = itemView.findViewById(R.id.percent)
    private val progressBar: SeekBar = itemView.findViewById(R.id.seekbar)
    //private val context = itemView.context

    init {
        parent.addView(itemView)
        titleTextView.setTextResource(title)
        progressBar.apply {
            this.max = max
            this.progress = current
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    percent.text = "$progress"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    onPercentChangeCallback.invoke(seekBar?.progress ?: 0)
                }
            })
        }
    }

    fun setProgress(current: Int) {
        progressBar.progress = current
        percent.text = "$current"
    }
}