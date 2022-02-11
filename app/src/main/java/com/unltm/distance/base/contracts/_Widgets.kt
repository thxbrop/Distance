package com.unltm.distance.base.contracts

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import coil.ImageLoader
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import kotlin.math.max

fun RecyclerView.closeRecyclerViewAnimation() {
    (itemAnimator?.apply {
        addDuration = 0L
        changeDuration = 0L
        moveDuration = 0L
        removeDuration = 0L
    } as SimpleItemAnimator).supportsChangeAnimations = false
}

fun RecyclerView.setMaxFlingVelocity(velocity: Int = 8000) {
    try {
        val field = javaClass.getDeclaredField("mMaxFlingVelocity")
        field.isAccessible = true
        field.set(this, velocity)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

inline fun RecyclerView.Adapter<*>.onChanged(crossinline block: () -> Unit) {
    registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            block.invoke()
        }
    })
}

inline fun RecyclerView.Adapter<*>.onInserted(crossinline block: (Int, Int) -> Unit) {
    registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            block.invoke(positionStart, itemCount)
        }
    })
}

fun ImageView.loadHTTPS(
    uri: String?,
    imageLoader: ImageLoader = context.imageLoader,
    builder: ImageRequest.Builder.() -> Unit = {}
) = load(uri.toHttps(), imageLoader, builder)


fun TextView.setTextResource(@StringRes resId: Int) {
    text = context.getString(resId)
}

fun TextView.setTextColorResource(@ColorRes resId: Int) {
    setTextColor(ContextCompat.getColor(context, resId))
}

fun TextView.setTime(time: Long) {
    getFriendlyTimeSpanByNow(max(time, 0)).also { formatted ->
        formatted.split('-').also {
            text = if (it.size == 3) formatted.drop(5)
            else formatted
        }
    }
}

fun TextView.measureCurrentText(): Pair<Int, Int> = run {
    val rect = Rect()
    paint.getTextBounds(text.toString(), 0, text.length - 1, rect)
    rect.width() to rect.height()
}

inline fun <reified T : ViewGroup.LayoutParams> View.editLayoutParams(block: (T) -> Unit) {
    val params = layoutParams
    block.invoke(params as T)
    layoutParams = params
}

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

val Int.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        toFloat(),
        Resources.getSystem().displayMetrics
    )
