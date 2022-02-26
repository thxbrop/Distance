package com.unltm.distance.base.contracts

import android.graphics.Typeface
import android.widget.TextView
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.StringRes
import com.blankj.utilcode.constant.TimeConstants
import com.unltm.distance.R
import com.unltm.distance.application
import com.unltm.distance.databinding.IncludeHeaderBinding
import java.util.*
import kotlin.math.max

fun getFriendlyTimeSpanByNow(millis: Long): String {
    val now = System.currentTimeMillis()
    val span = max(now - millis, 0)
    return when {
        span < 1000 -> "刚刚"
        span < TimeConstants.MIN -> String.format(
            Locale.getDefault(),
            "%d秒前",
            span / TimeConstants.SEC
        )
        span < TimeConstants.HOUR -> String.format(
            Locale.getDefault(),
            "%d分钟前",
            span / TimeConstants.MIN
        )
        else -> {
            val wee = getWeeOfToday()
            when {
                millis >= wee -> String.format("%tR", millis)
                millis >= wee - TimeConstants.DAY -> String.format("昨天%tR", millis)
                else -> String.format("%tF", millis)
            }
        }
    }
}

fun getWeeOfToday(): Long {
    val cal = Calendar.getInstance()
    cal[Calendar.HOUR_OF_DAY] = 0
    cal[Calendar.SECOND] = 0
    cal[Calendar.MINUTE] = 0
    cal[Calendar.MILLISECOND] = 0
    return cal.timeInMillis
}

internal fun IncludeHeaderBinding.initAppBar(
    @StringRes title: Int,
    onBackPressedDispatcher: OnBackPressedDispatcher,
) {
    toolbar.setNavigationOnClickListener {
        onBackPressedDispatcher.onBackPressed()
    }

    textSwitcher.setFactory {
        TextView(this.root.context).also {
            it.typeface = Typeface.DEFAULT_BOLD
            it.setTextColorResource(R.color.toolbar_text)
            it.textSize = 18f
        }
    }
    textSwitcher.setCurrentText(application.getString(title))
}
