package com.unltm.distance.base.contracts

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.zxing.common.CharacterSetECI
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.unltm.distance.R
import com.unltm.distance.application
import com.unltm.distance.base.jvm.QRCodeUtil
import com.unltm.distance.ui.components.ceil.TextDrawable

private val shapeBuilder = TextDrawable.builder().beginConfig().bold().endConfig()

fun buildTextBitmap(
    s: String,
    shape: TextBitmapShape = TextBitmapShape.Round,
    @ColorInt color: Int = ContextCompat.getColor(
        application.applicationContext,
        R.color.colorPrimary
    ),
    size: Int = 128
) = run {
    when (shape) {
        TextBitmapShape.Round ->
            shapeBuilder.buildRound((s.trim().uppercase().firstOrNull() ?: " ").toString(), color)
                .toBitmap(size, size)
        TextBitmapShape.Rect ->
            shapeBuilder.buildRect((s.trim().uppercase().firstOrNull() ?: " ").toString(), color)
                .toBitmap(size, size)
        TextBitmapShape.RoundRect ->
            shapeBuilder.buildRoundRect(
                (s.trim().uppercase().firstOrNull() ?: " ").toString(),
                color,
                size / 4
            ).toBitmap(size, size)
    }
}

sealed class TextBitmapShape {
    object Round : TextBitmapShape()
    object Rect : TextBitmapShape()
    object RoundRect : TextBitmapShape()
}

fun buildQRCodeBitmap(
    s: String,
    size: Int = 128,
    character_set: CharacterSetECI = CharacterSetECI.UTF8,
    error_correction: ErrorCorrectionLevel = ErrorCorrectionLevel.H,
    margin: Int = 2,
    color_black: Int = Color.BLACK,
    color_white: Int = Color.WHITE,
) = QRCodeUtil.createQRCodeBitmap(
    s,
    size,
    size,
    character_set.name,
    error_correction.name,
    margin.toString(),
    color_black,
    color_white
)!!
