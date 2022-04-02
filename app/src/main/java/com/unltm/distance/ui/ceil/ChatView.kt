package com.unltm.distance.ui.ceil

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import com.blankj.utilcode.util.SizeUtils
import com.unltm.distance.R
import com.unltm.distance.base.contracts.dp
import kotlin.math.max

/**
 * @author 王志堃
 */
@SuppressWarnings("unused")
class ChatView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var isAnother = true
        set(value) {
            field = value
            if (value) {
                bubblePaint.color = ContextCompat.getColor(context, R.color.backgroundSecondary)
                textPaint.color = ContextCompat.getColor(context, R.color.textColor)
            } else {
                bubblePaint.color = ContextCompat.getColor(context, R.color.colorPrimary)
                textPaint.color = Color.WHITE
            }

            invalidate()
        }
    var text = "Hello Kotlin!"
        set(value) {
            field = value
            invalidate()
        }

    var time = 0L
        set(value) {
            field = value
            invalidate()
        }
    private var boxWidth = 0f
    private var radius = 12f.dp
    private var bubblePaint = Paint().apply {
        color = if (isAnother) {
            ContextCompat.getColor(context, R.color.backgroundSecondary)
        } else {
            ContextCompat.getColor(context, R.color.colorPrimary)
        }
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val whitePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
    }

    private val textPaint = Paint().apply {
        color = if (isAnother) ContextCompat.getColor(context, R.color.textColor)
        else Color.WHITE
        isAntiAlias = true
        textSize = SizeUtils.sp2px(14f).toFloat()
    }

    private val secondaryTextPaint = Paint(textPaint).apply {
        color = if (isAnother) Color.GRAY else ContextCompat.getColor(context, R.color.white)
        textSize = SizeUtils.sp2px(10f).toFloat()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        canvas?.drawColor(Color.TRANSPARENT)
        canvas?.withTranslation(0f, height.toFloat()) {
            drawBubble(this)
        }
        canvas?.let {
            drawText(it)
        }
    }

    private fun drawText(canvas: Canvas) {
        val mx = if (isAnother) radius * 3 else width - boxWidth + radius * 2
        canvas.drawText(
            text,
            mx,
            height / 2f + (textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent) / 2 - textPaint.fontMetrics.descent,
            textPaint
        )
//        canvas.drawText(
//            DateUtils.getRelativeTimeSpanString(time).toString(),
//            radius * 4 + textPaint.measureText(text),
//            height / 2f + (secondaryTextPaint.fontMetrics.descent - secondaryTextPaint.fontMetrics.ascent) / 2 - secondaryTextPaint.fontMetrics.descent,
//            secondaryTextPaint
//        )
    }

    private fun drawBubble(canvas: Canvas) {
        canvas.apply {
            if (isAnother) {
                drawRoundRect(
                    2 * radius,
                    -height.toFloat() + radius,
                    boxWidth - radius,
                    -radius,
                    radius,
                    radius,
                    bubblePaint
                )
                drawRect(radius, -2 * radius, 3 * radius, -radius, bubblePaint)

                drawCircle(radius, -radius * 2, radius, whitePaint)

            } else {
                drawRoundRect(
                    width - boxWidth + radius,
                    -height.toFloat() + radius,
                    width - boxWidth + boxWidth - radius * 2,
                    -radius,
                    radius,
                    radius,
                    bubblePaint
                )

                drawRect(
                    width - boxWidth + boxWidth - 3 * radius,
                    -radius * 2,
                    width - radius,
                    -radius,
                    bubblePaint
                )

                drawCircle(width - radius, -radius * 2, radius, whitePaint)

            }

        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        boxWidth = radius * 5 + max(radius * 2, textPaint.measureText(text))
        val height = radius * 4 + textPaint.measureText("你")
        val mHeightMeasureSpec =
            MeasureSpec.makeMeasureSpec(height.toInt(), MeasureSpec.getMode(heightMeasureSpec))
        setMeasuredDimension(widthMeasureSpec, mHeightMeasureSpec)
    }
}