package com.unltm.distance.base.jvm;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class QRCodeUtil {

    /**
     * 创建二维码位图
     *
     * @param content 字符串内容(支持中文)
     * @param width   位图宽度(单位:px)
     * @param height  位图高度(单位:px)
     * @return 二维码位图
     */
    @Nullable
    public static Bitmap createQRCodeBitmap(String content, int width, int height) {
        return createQRCodeBitmap(content, width, height, "UTF-8", "H", "2", Color.BLACK, Color.WHITE);
    }

    /**
     * 创建二维码位图 (支持自定义配置和自定义样式)
     *
     * @param content          字符串内容
     * @param width            位图宽度,要求>=0(单位:px)
     * @param height           位图高度,要求>=0(单位:px)
     * @param character_set    字符集/字符转码格式 (支持格式:{@link CharacterSetECI })。传null时,zxing源码默认使用 "ISO-8859-1"
     * @param error_correction 容错级别 (支持级别:{@link ErrorCorrectionLevel })。传null时,zxing源码默认使用 "L"
     * @param margin           空白边距 (可修改,要求:整型且>=0), 传null时,zxing源码默认使用"4"。
     * @param color_black      黑色色块的自定义颜色值
     * @param color_white      白色色块的自定义颜色值
     * @return 二维码位图
     */
    @Nullable
    public static Bitmap createQRCodeBitmap(String content, int width, int height,
                                            @Nullable String character_set, @Nullable String error_correction, @Nullable String margin,
                                            @ColorInt int color_black, @ColorInt int color_white) {

        if (TextUtils.isEmpty(content)) { // 字符串内容判空
            return null;
        }

        if (width < 0 || height < 0) { // 宽和高都需要>=0
            return null;
        }

        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();

            if (!TextUtils.isEmpty(character_set)) {
                hints.put(EncodeHintType.CHARACTER_SET, character_set); // 字符转码格式设置
            }

            if (!TextUtils.isEmpty(error_correction)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, error_correction); // 容错级别设置
            }

            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin); // 空白边距设置
            }
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = color_black; // 黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white; // 白色色块像素设置
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }

}