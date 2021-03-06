package com.unltm.distance.activity.settings.components

import android.view.LayoutInflater
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.unltm.distance.R
import com.unltm.distance.application
import com.unltm.distance.base.contracts.transitionColor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Integer.max

internal class ThemeSpanSetting(
    private val lifecycleOwner: LifecycleOwner,
    parent: LinearLayout,
    items: Array<Int>,
    listener: (Int) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.include_settings_theme_span, parent, false)
) {
    private var checkedPosition = MutableLiveData(-1)
    private var lastPosition = 0
    private var job: Job? = null

    init {
        parent.addView(itemView)
        val linearLayout = itemView.findViewById<LinearLayoutCompat>(R.id.linear)

        items.forEachIndexed { index, color ->
            val child = Item(color, linearLayout) {
                checkedPosition.postValue(index)
            }.itemView
            linearLayout.addView(child)
        }

        checkedPosition.observe(lifecycleOwner) { position ->
            if (position in items.indices) {
                listener.invoke(position)
                itemView.findViewById<HorizontalScrollView>(R.id.horizontal_scrollView).also {
                    if (job != null && job!!.isActive) job!!.cancel()
                    job = lifecycleOwner.lifecycleScope.launch {
                        val startColor = application.getColor(items[max(lastPosition, 0)])
                        val endColor = application.getColor(items[position])
                        startColor.transitionColor(endColor) { color ->
                            it.setBackgroundColor(color)
                        }
                    }
                }
            }
            repeat(items.size) { time ->
                linearLayout.getChildAt(time).findViewById<ImageView>(R.id.checked).visibility =
                    if (time != position) View.INVISIBLE else View.VISIBLE
            }
            lastPosition = position
        }
    }

}
