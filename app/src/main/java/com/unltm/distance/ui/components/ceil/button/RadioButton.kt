package com.unltm.distance.ui.components.ceil.button

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.unltm.distance.base.contracts.dp

const val TAG = "RadioButton"

class RadioButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var bitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null
    private var paint: Paint? = null
    private var eraser: Paint? = null
    private var checkedPaint: Paint? = null

    private var checkedColor = 0
    private var color = 0

    var progress = 0f
        set(value) {
            if (field == value) return
            field = value
            invalidate()
        }

    private var checkAnimator: ObjectAnimator? = null

    private var attachedToWindow = false
    var isChecked = false
    var size: Int = 16.dp
        set(value) {
            if (field == value) return
            field = value
        }

    init {
        if (paint == null) {
            paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                strokeWidth = 2f.dp
                style = Paint.Style.STROKE
            }
            checkedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            eraser = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = 0
                xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            }
        }
        try {
            bitmap = Bitmap.createBitmap(size.dp, size.dp, Bitmap.Config.ARGB_8888)
            bitmapCanvas = Canvas(bitmap!!)
        } catch (throwable: Throwable) {
            Log.e(TAG, "init: ", throwable)
        }
    }

    fun setColor(color1: Int, color2: Int) {
        color = color1
        checkedColor = color2
        invalidate()
    }

    override fun setBackgroundColor(color1: Int) {
        color = color1
        invalidate()
    }

    fun setCheckedColor(color2: Int) {
        checkedColor = color2
        invalidate()
    }

    private fun cancelCheckAnimator() {
        checkAnimator?.cancel()
    }

    private fun animateToCheckedState(newCheckedState: Boolean) {
        checkAnimator =
            ObjectAnimator.ofFloat(this, "progress", if (newCheckedState) 1f else 0f).apply {
                duration = 200
                start()
            }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        attachedToWindow = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        attachedToWindow = false
    }

    fun setChecked(checked: Boolean, animated: Boolean) {
        if (checked == isChecked) return
        isChecked = checked
        if (attachedToWindow && animated) {
            animateToCheckedState(checked)
        } else {
            cancelCheckAnimator()
            progress = if (checked) 1f else 0f
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (bitmap == null || bitmap?.width != measuredWidth) {
            bitmap?.recycle()
            bitmap = null
            try {
                bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
                bitmapCanvas = Canvas(bitmap!!)
            } catch (throwable: Throwable) {
                Log.e(TAG, "onDraw: ", throwable)
            }
        }
        val circleProgress: Float
        if (progress <= 0.5f) {
            paint?.color = color
            checkedPaint?.color = color
            circleProgress = progress / 0.5f
        } else {
            circleProgress = 2f - progress / 0.5f
            val r1 = Color.red(color)
            val rD = ((Color.red(checkedColor) - r1) * (1f - circleProgress)).toInt()
            val g1 = Color.green(color)
            val gD = ((Color.green(checkedColor) - r1) * (1f - circleProgress)).toInt()
            val b1 = Color.blue(color)
            val bD = ((Color.blue(checkedColor) - r1) * (1f - circleProgress)).toInt()
            val c = Color.rgb(r1 + rD, g1 + gD, b1 + bD)
            paint?.color = c
            checkedPaint?.color = c
        }
        bitmap?.let {
            it.eraseColor(0)
            val rad = size / 2 - (1 + circleProgress) * ScreenUtils.getScreenDensity()
            bitmapCanvas?.drawCircle(measuredWidth / 2f, measuredHeight / 2f, rad, paint!!)
            if (progress <= 0.5f) {
                bitmapCanvas?.drawCircle(
                    measuredWidth / 2f,
                    measuredHeight / 2f,
                    (rad - 1.dp),
                    checkedPaint!!
                )
                bitmapCanvas?.drawCircle(
                    measuredWidth / 2f,
                    measuredHeight / 2f,
                    (rad - 1.dp) * (1f - circleProgress),
                    eraser!!
                )
            } else {
                bitmapCanvas?.drawCircle(
                    measuredWidth / 2f,
                    measuredHeight / 2f,
                    size / 4 + (rad - 1.dp - size / 4) * circleProgress,
                    checkedPaint!!
                )
            }

            canvas?.drawBitmap(bitmap!!, 0f, 0f, null)
        }
    }

}