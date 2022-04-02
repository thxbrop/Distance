package com.unltm.distance.ui.ceil.button

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction
import android.view.animation.DecelerateInterpolator
import com.unltm.distance.R
import com.unltm.distance.base.contracts.dp
import kotlinx.coroutines.*
import kotlin.math.abs

private const val LONG_PRESS_TIME = 800L

@SuppressLint("UseCompatLoadingForDrawables")
class ShutterButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var shadowDrawable: Drawable = resources.getDrawable(R.drawable.camera_btn, null)
    private val interpolator = DecelerateInterpolator()
    private var whitePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private var redPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#cd4747")
    }
    var delegate: ShutterButtonDelegate? = null
    var state: State? = null
    private var redProgress = 0f
    private var lastUpdateTime: Long = 0
    private var totalTime: Long = 0
    private var processRelease = false

    private val longPressed = Runnable {
        if (delegate != null) {
            if (!delegate!!.shutterLongPressed()) {
                processRelease = false
            }
        }
    }

    private var job: Job? = null

    init {
        state = State.DEFAULT
    }

    private var offsetX = 0f
    private var offsetY = 0f

    private fun setHighlighted(value: Boolean) {
        val animatorSet = AnimatorSet()
        if (value) {
            animatorSet.playTogether(
                ObjectAnimator.ofFloat(this, SCALE_X, 1.06f),
                ObjectAnimator.ofFloat(this, SCALE_Y, 1.06f)
            )
        } else {
            animatorSet.playTogether(
                ObjectAnimator.ofFloat(this, SCALE_X, 1.0f),
                ObjectAnimator.ofFloat(this, SCALE_Y, 1.0f)
            )
            animatorSet.startDelay = 40
        }
        animatorSet.duration = 120
        animatorSet.interpolator = interpolator
        animatorSet.start()
    }

    override fun setScaleX(scaleX: Float) {
        super.setScaleX(scaleX)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        val cx = measuredWidth / 2
        val cy = measuredHeight / 2
        canvas?.let {
            shadowDrawable.setBounds(
                cx - 36.dp,
                cy - 36.dp,
                cx + 36.dp,
                cy + 36.dp
            )
            shadowDrawable.draw(it)
            if (isPressed || scaleX != 1.0f) {
                val scale = (scaleX - 1.0f) / 0.06f
                whitePaint.alpha = (255 * scale).toInt()
                it.drawCircle(
                    cx.toFloat(),
                    cy.toFloat(),
                    26f.dp,
                    whitePaint
                )
                if (state == State.RECORDING) {
                    if (redProgress != 1.0f) {
                        var dt = abs(System.currentTimeMillis() - lastUpdateTime)
                        if (dt > 17) {
                            dt = 17
                        }
                        totalTime += dt
                        if (totalTime > 120) {
                            totalTime = 120
                        }
                        redProgress = interpolator.getInterpolation(totalTime / 120.0f)
                        invalidate()
                    }
                    it.drawCircle(
                        cx.toFloat() + offsetX,
                        cy.toFloat() + offsetY,
                        26.5f.dp * scale * redProgress,
                        redPaint
                    )
                } else if (redProgress != 0f) {
                    it.drawCircle(
                        cx.toFloat(),
                        cy.toFloat(),
                        26.5f.dp * scale,
                        redPaint
                    )
                }
            } else if (redProgress != 0f) {
                redProgress = 0f
            }
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(84.dp, 84.dp)
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        val x = motionEvent.x
        val y = motionEvent.y
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                job = MainScope().launch {
                    delay(LONG_PRESS_TIME)
                    longPressed.run()
                }
                isPressed = true
                processRelease = true
                setHighlighted(true)
            }
            MotionEvent.ACTION_UP -> {
                setHighlighted(false)
                job?.cancel()
                if (processRelease) {
                    delegate?.shutterReleased()
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                setHighlighted(false)
                isPressed = false
            }
        }
        performClick()
        return true
    }

    private fun Float.crossToRound(desert: Float): Float = run {
        when {
            this == 0f -> this
            this > 0 -> this.minus(desert)
            else -> this.plus(desert)
        }
    }

    fun setState(value: State, animated: Boolean) {
        if (state != value) {
            state = value
            if (animated) {
                lastUpdateTime = System.currentTimeMillis()
                totalTime = 0
                if (state != State.RECORDING) {
                    redProgress = 0.0f
                }
            } else {
                redProgress = (if (state == State.RECORDING) 1.0f else 0.0f)
            }
            invalidate()
        }
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = "android.widget.Button"
        info.isClickable = true
        info.isLongClickable = true
        info.addAction(
            AccessibilityAction(
                AccessibilityAction.ACTION_CLICK.id,
                "LocaleController.getString(\"AccActionTakePicture\", R.string.AccActionTakePicture)"
            )
        )
        info.addAction(
            AccessibilityAction(
                AccessibilityAction.ACTION_LONG_CLICK.id,
                "LocaleController.getString(\"AccActionRecordVideo\", R.string.AccActionRecordVideo)"
            )
        )
    }

    enum class State {
        DEFAULT, RECORDING
    }

    interface ShutterButtonDelegate {
        fun shutterLongPressed(): Boolean
        fun shutterReleased()
        fun shutterCancel()
        fun onTranslationChanged(x: Float, y: Float): Boolean
    }

}