package com.unltm.distance.ui.components.background;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ColorUtils;

public class Gradual extends View {

    private final Paint paint = new Paint();
    public ValueAnimator animator;
    LinearGradient backGradient;
    private int animatedValue;
    private int colorEnd;
    private int colorStart;

    public Gradual(Context context) {
        super(context);
        paint.setAntiAlias(true);
        init();
        requestLayout();
    }

    public Gradual(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        requestLayout();
    }

    public Gradual(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        requestLayout();
    }

    public void init() {
        postInvalidate();
        animator = ValueAnimator.ofInt(0, 255);
        animator.setDuration(10000);
        animator.addUpdateListener(animation -> {
            animatedValue = (int) animation.getAnimatedValue();
            if (animatedValue < 255) {
                colorStart = Color.rgb(255, animatedValue, 255 - animatedValue);
                colorEnd = Color.rgb(animatedValue, 0, 255 - animatedValue);
            } else if (animatedValue == 255) {
                ValueAnimator animator1 = ValueAnimator.ofInt(0, 255);
                animator1.setDuration(2500);
                animator1.addUpdateListener(animation1 -> {
                    animatedValue = (int) animation1.getAnimatedValue();
                    colorStart = Color.rgb(255 - animatedValue, 255 - animatedValue, animatedValue);
                    colorEnd = Color.rgb(255, 0, animatedValue);
                    if (animatedValue == 255) {
                        ValueAnimator animator2 = ValueAnimator.ofInt(0, 255);
                        animator2.setDuration(2500);
                        animator2.addUpdateListener(animation11 -> {
                            int animatedValue2 = (int) animation11.getAnimatedValue();
                            colorStart = Color.rgb(animatedValue2, 0, 255);
                            colorEnd = Color.rgb(255 - animatedValue2, 0, 255);
                            invalidate();
                        });
                        animator2.start();
                    }
                    invalidate();
                });
                animator1.start();
            }

            invalidate();
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        colorStart = ColorUtils.setAlphaComponent(colorStart, 0.4f);
        colorEnd = ColorUtils.setAlphaComponent(colorEnd, 0.4f);
        @SuppressLint("DrawAllocation")
        LinearGradient backGradient = new LinearGradient(width, 0, 0, height, new int[]{colorStart, colorEnd}, new float[]{0, 1f}, Shader.TileMode.CLAMP);
        paint.setShader(backGradient);

        canvas.drawRect(0, 0, width, height, paint);
        canvas.drawRect(0, 0, width, height, paint);
    }
}
