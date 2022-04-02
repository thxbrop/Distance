package com.unltm.distance.ui.ceil

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import com.unltm.distance.R
import com.unltm.distance.base.contracts.dp
import com.unltm.distance.base.contracts.sp
import kotlin.math.max
import kotlin.math.min


class RatioRegulator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mHeight = 56.dp
    private val radius = 8f.dp

    private val red = ContextCompat.getColor(context, R.color.red)
    private val yellow = ContextCompat.getColor(context, R.color.yellow)
    private val green = ContextCompat.getColor(context, R.color.green)
    private val blue = ContextCompat.getColor(context, R.color.blue)

    private val background = context.getColor(R.color.widget_ratio_regulator_dialog)

    private val textColor = context.getColor(R.color.widget_ratio_regulator_text)

    @SuppressLint("UseCompatLoadingForDrawables")
    private var shadowDrawable: Drawable =
        resources.getDrawable(
            R.drawable.ic_round_volume_up_24,
            null
        ).apply {
            setTint(textColor)
        }

    private val dialogPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = background
        style = Paint.Style.FILL_AND_STROKE
    }

    private val redPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = red
        style = Paint.Style.FILL_AND_STROKE
    }
    private val yellowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = yellow
        style = Paint.Style.FILL_AND_STROKE
    }
    private val greenPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = green
        style = Paint.Style.FILL_AND_STROKE
    }
    private val bluePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = blue
        style = Paint.Style.FILL_AND_STROKE
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textColor
        typeface = Typeface.DEFAULT_BOLD
        textSize = 16.sp
    }

    @FloatRange(from = 0.0, to = 100.0)
    var progress: Float = 40f
        set(value) {
            if (field == value) return
            field = value
            invalidate()
        }

    private var listener: ((Float) -> Unit)? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawDialog(it)
            drawProgress(it)
            drawText(it)
        }
    }

    private fun drawText(canvas: Canvas) {
        shadowDrawable.bounds = Rect(
            measuredHeight / 2 - 14.dp,
            measuredHeight / 2 - 14.dp,
            measuredHeight / 2 + 14.dp,
            measuredHeight / 2 + 14.dp
        )
        shadowDrawable.setTint(textColor)
        shadowDrawable.draw(canvas)
        val s = "${progress.toInt()}%"
        val rect = Rect()
        textPaint.getTextBounds(s, 0, s.length, rect)
        val height = rect.height()
        canvas.drawText(
            s,
            measuredHeight / 2f + 24.dp,
            measuredHeight / 2f + height / 2,
            textPaint
        )
    }

    private fun drawProgress(canvas: Canvas) {
        val w = progress / 100f * measuredWidth
        canvas.drawRoundRect(
            0f,
            0f,
            w,
            measuredHeight.toFloat(),
            radius,
            radius,
            when {
                progress >= 75 -> bluePaint
                progress >= 50 -> greenPaint
                progress >= 25 -> yellowPaint
                else -> redPaint
            }
        )
    }

    private fun drawDialog(canvas: Canvas) {
        canvas.drawRoundRect(
            0f,
            0f,
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            radius,
            radius,
            dialogPaint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMeasureSpec0 =
            MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.UNSPECIFIED
            )
        val heightMeasureSpec0 = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY)
        setMeasuredDimension(widthMeasureSpec0, heightMeasureSpec0)
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val mX: Float = event?.x ?: 0f
        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                progress = min(max((mX / measuredWidth.toFloat() * 100), 0f), 100f)
                listener?.invoke(progress)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun setOnProgressChangeListener(listener: (Float) -> Unit) {
        this.listener = listener
    }
}