package com.unltm.distance.ui.ceil

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import com.unltm.distance.R
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class LoadingProgress @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : View(context, attrs), Runnable {

    var progress = 0
    private var currentRadio = 0f
    private var radiusBig = 0f
    private var radiusSmall = 0f
    private var padding = 0f

    private var oval = RectF()
    private var ovalInset = RectF()

    private val basePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = radiusBig - radiusSmall
    }
    private val loadedPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.colorPrimary)
        style = Paint.Style.FILL
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radiusBig = width / 2f
        radiusSmall = radiusBig - width / 6f
        padding = width / 6f - width / 8f
        oval = RectF(0f, 0f, width.toFloat(), height.toFloat())
        ovalInset = RectF(
            width / 6f, width / 6f,
            width.toFloat() - width / 6f,
            height.toFloat() - width / 6f
        )
        basePaint.strokeWidth = radiusBig - radiusSmall
        Thread(this).start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        //0A 边角度
        val startAngle = currentRadio / 100f * 360
        //OB 边角度
        val scanAngle = progress / 100f * 360
        canvas?.let {
            it.drawColor(Color.TRANSPARENT)
            it.drawCircle(width / 2f, height / 2f, (radiusBig + radiusSmall) / 2, basePaint)
            it.drawArc(oval, startAngle, scanAngle, true, loadedPaint)
        }
        canvas?.withTranslation(width / 2f, height / 2f) {
            val pointF = angle2xy(startAngle, (radiusBig + radiusSmall) / 2)
            val x = pointF.x
            val y = pointF.y
            drawCircle(x, y, (radiusBig - radiusSmall) / 2, loadedPaint)
            val pointFEnd = angle2xy(startAngle + scanAngle, (radiusBig + radiusSmall) / 2)
            val xEnd = pointFEnd.x
            val yEnd = pointFEnd.y
            drawCircle(xEnd, yEnd, (radiusBig - radiusSmall) / 2, loadedPaint)
        }

    }

    private fun angle2xy(
        @FloatRange(from = 0.0, to = 360.0) angle: Float,
        radius: Float,
    ): PointF {
        val toRadians = Math.toRadians(angle.toDouble())
        val x = radius * cos(toRadians).toFloat()
        val y = radius * sin(toRadians).toFloat()
        return PointF(x, y)
    }

    override fun run() {
        if (progress < 100) {
            postInvalidate()
            postDelayed(this, 10)
            currentRadio += 1
        }
    }
}

