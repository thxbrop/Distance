package com.unltm.distance.ui.background

import android.graphics.*
import androidx.core.graphics.withTranslation
import com.blankj.utilcode.util.ColorUtils.getColor
import com.unltm.distance.R
import com.unltm.distance.base.contracts.dp

class RecordStatusDrawable(createPaint: Boolean) : StatusDrawable() {
    private var isChat = false
    private var lastUpdateTime = 0L
    private var started = false
    private var rectF = RectF()
    private var progress = 0f
    var alpha0: Int = 255
    private var currentPaint: Paint? = null

    init {
        if (createPaint) {
            currentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
                strokeWidth = 2f.dp
            }
        }
    }

    override fun start() {
        lastUpdateTime = System.currentTimeMillis()
        started = true
        invalidateSelf()
    }

    override fun stop() {
        started = false
    }

    override fun setIsChat(value: Boolean) {
        isChat = value
    }

    override fun setColor(color: Int) {
        currentPaint?.color = color
    }

    override fun draw(canvas: Canvas) {
        val paint = if (currentPaint == null) Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            color = getColor(R.color.colorPrimary)
        } else currentPaint!!
        if (paint.strokeWidth != 2f.dp) {
            paint.strokeWidth = 2f.dp
        }
        canvas.withTranslation(
            0f,
            (intrinsicHeight / 2 + (if (isChat) 1f else 2f).dp)
        ) {
            for (i in 0 until 4) {
                when (i) {
                    0 -> paint.alpha = (alpha0 * progress).toInt()
                    3 -> paint.alpha = (alpha0 * (1.0f - progress)).toInt()
                    else -> paint.alpha = alpha0
                }
                val side = 4f.dp * i + 4f.dp * progress
                rectF.set(-side, -side, side, side)
                drawArc(rectF, -15f, 30f, false, paint)
            }
        }
        if (started) update()
    }

    override fun setAlpha(alpha: Int) {
        this.alpha0 = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int = PixelFormat.UNKNOWN

    private fun update() {
        val newTime = System.currentTimeMillis()
        var dt = newTime - lastUpdateTime
        lastUpdateTime = newTime
        if (dt > 50) {
            dt = 50
        }
        progress += dt / 800.0f
        while (progress > 1.0) {
            progress -= 1.0f
        }
        invalidateSelf()
    }

    override fun getIntrinsicWidth(): Int = 24.dp

    override fun getIntrinsicHeight(): Int = 18.dp
}