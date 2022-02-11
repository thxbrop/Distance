package com.unltm.distance.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.unltm.distance.base.contracts.dp
import com.unltm.distance.base.contracts.sp

class ScrollRuler @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val millimeterPixel = 8.dp

    private val cursorWidthPixel = 4f.dp

    private val rulerHeightPixel = 96.dp

    private val cursorHeightPixel = rulerHeightPixel / 2f

    private val strongGraduateHeightPixel = cursorHeightPixel - cursorWidthPixel

    private val thinGraduateHeightPixel = strongGraduateHeightPixel / 2

    private var max = 20.0f

    private var current: Float = 0.0f


    private val rulerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#f8f8f8")
        style = Paint.Style.FILL_AND_STROKE
    }

    private val cursorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#00adb5")
        strokeWidth = cursorWidthPixel
        strokeCap = Paint.Cap.ROUND
    }

    private val thinGraduatePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = cursorWidthPixel / 4
        strokeCap = Paint.Cap.ROUND
    }

    private val strongGraduatePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = cursorWidthPixel / 2
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 18.sp
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawRuler(it)
            drawGraduate(it)
            drawCursor(it)
        }
    }

    private fun drawGraduate(canvas: Canvas) {
        val x = measuredWidth / 2f
        val y = measuredHeight / 2f
        val times = measuredWidth / millimeterPixel / 2
        for (i in 0..times) {
            val currentX = x + i * millimeterPixel
            val currentXGraduation = current + i * 0.1f
            val reversedCurrentX = x - i * millimeterPixel
            val reversedCurrentXGraduation = current - i * 0.1f

            if (currentXGraduation.isInt()) {
                canvas.drawLine(
                    currentX,
                    0f,
                    currentX,
                    strongGraduateHeightPixel,
                    strongGraduatePaint
                )
                val text = String.format("%.1f", currentXGraduation)
                val offset = textPaint.measureText(text) / 2f
                canvas.drawText(
                    text,
                    currentX - offset,
                    y / 2 * 3,
                    textPaint
                )
            } else canvas.drawLine(
                currentX,
                0f,
                currentX,
                thinGraduateHeightPixel,
                thinGraduatePaint
            )

            if (reversedCurrentXGraduation.isInt()) {
                canvas.drawLine(
                    reversedCurrentX,
                    0f,
                    reversedCurrentX,
                    strongGraduateHeightPixel,
                    strongGraduatePaint
                )

                val text = String.format("%.1f", reversedCurrentXGraduation)
                val offset = textPaint.measureText(text) / 2f
                canvas.drawText(
                    text,
                    reversedCurrentX - offset,
                    y / 2 * 3,
                    textPaint
                )
            } else canvas.drawLine(
                reversedCurrentX,
                0f,
                reversedCurrentX,
                thinGraduateHeightPixel,
                thinGraduatePaint
            )
        }
    }

    private fun drawCursor(canvas: Canvas) {
        val x = measuredWidth / 2f
        canvas.drawLine(x, 0f, x, cursorHeightPixel, cursorPaint)
    }

    private fun drawRuler(canvas: Canvas) {
        val mWidth = measuredWidth.toFloat()
        val mHeight = measuredHeight.toFloat()
        canvas.drawRect(
            RectF(0f, 0f, mWidth, mHeight),
            rulerPaint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMeasureSpec0 =
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST)
        val heightMeasureSpec0 = MeasureSpec.makeMeasureSpec(rulerHeightPixel, MeasureSpec.EXACTLY)
        setMeasuredDimension(widthMeasureSpec0, heightMeasureSpec0)
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    private fun Float.isInt() = this == toInt().toFloat()


    private var actionDownDimen = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                actionDownDimen = event.x
            }
            MotionEvent.ACTION_MOVE -> {
//                val delta = ((actionDownDimen - event.x) / millimeterPixel) * 0.1f
//                if (delta != 0f) {
//                    current = UtilsBigDecimal.add(
//                        current.toDouble(),
//                        delta.toDouble()
//                    ).toFloat()
//                    invalidate()
//                }
//                actionDownDimen = event.x
            }
        }
        return true
    }

}