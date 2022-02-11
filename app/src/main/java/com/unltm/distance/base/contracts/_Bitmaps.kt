package com.unltm.distance.base.contracts

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.unltm.distance.R
import com.unltm.distance.ui.components.ceil.TextDrawable

private val shapeBuilder = TextDrawable.builder().beginConfig().bold().endConfig()

fun buildRoundTextBitmap(
    context: Context, s: String,
    @ColorInt color: Int = ContextCompat.getColor(context, R.color.colorPrimary),
): Bitmap =
    shapeBuilder.buildRound(s.trim().firstOrNull().toString(), color).toBitmap(120, 120)

fun buildRectTextBitmap(
    context: Context, s: String,
    @ColorInt color: Int = ContextCompat.getColor(context, R.color.colorPrimary),
    size: Int = 256
): Bitmap =
    shapeBuilder.buildRect(s.trim().firstOrNull().toString(), color).toBitmap(size, size)
