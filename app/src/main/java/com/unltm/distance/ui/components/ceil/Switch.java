package com.unltm.distance.ui.components.ceil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;

/**
 * @author 来自 Telegram 开源项目
 * 王志堃 加以修改：样式修改
 */
@SuppressWarnings("unused")
public class Switch extends View {

    private final int[] pressedState = new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed};
    private RectF rectF;
    private float progress;
    private ObjectAnimator checkAnimator;
    private ObjectAnimator iconAnimator;
    private boolean attachedToWindow;
    private boolean isChecked;
    private Paint paint;
    private Paint paint2;
    private int drawIconType;
    private float iconProgress = 1.0f;
    private OnCheckedChangeListener onCheckedChangeListener;
    private Drawable iconDrawable;
    private int lastIconColor;
    private boolean drawRipple;
    private RippleDrawable rippleDrawable;
    private int colorSet;

    private boolean bitmapsCreated;
    private Bitmap[] overlayBitmap;
    private Canvas[] overlayCanvas;
    private Bitmap overlayMaskBitmap;
    private Canvas overlayMaskCanvas;
    private float overlayCx;
    private float overlayCy;
    private float overlayRad;
    private Paint overlayEraserPaint;
    private Paint overlayMaskPaint;

    private int overrideColorProgress;

    private Context context;

    public Switch(Context context) {
        super(context);
        init(context);
    }

    public Switch(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Switch(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Switch(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        rectF = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStrokeWidth(SizeUtils.dp2px(2));

    }

    @Keep
    public float getProgress() {
        return progress;
    }

    @Keep
    public void setProgress(float value) {
        if (Float.valueOf(progress).equals(value)) {
            return;
        }
        progress = value;
        invalidate();
    }

    @Keep
    public float getIconProgress() {
        return iconProgress;
    }

    @Keep
    public void setIconProgress(float value) {
        if (Float.valueOf(iconProgress).equals(value)) {
            return;
        }
        iconProgress = value;
        invalidate();
    }

    private void cancelCheckAnimator() {
        if (checkAnimator != null) {
            checkAnimator.cancel();
            checkAnimator = null;
        }
    }

    private void cancelIconAnimator() {
        if (iconAnimator != null) {
            iconAnimator.cancel();
            iconAnimator = null;
        }
    }

    public void setDrawIconType(int type) {
        drawIconType = type;
    }

    public void setDrawRipple(boolean value) {
        if (value == drawRipple) {
            return;
        }
        drawRipple = value;

        if (rippleDrawable == null) {
            Paint ripplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ripplePaint.setColor(0xffffffff);
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{StateSet.WILD_CARD},
                    new int[]{0}
            );
            rippleDrawable = new RippleDrawable(colorStateList, null, null);
            rippleDrawable.setRadius(SizeUtils.dp2px(18));
            rippleDrawable.setCallback(this);
        }
        if (isChecked && colorSet != 2 || !isChecked && colorSet != 1) {
            int color = isChecked ? 0x0f000000 : 0x17404a53;
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{StateSet.WILD_CARD},
                    new int[]{color}
            );
            rippleDrawable.setColor(colorStateList);
            colorSet = isChecked ? 2 : 1;
        }
        if (Build.VERSION.SDK_INT >= 28 && value) {
            rippleDrawable.setHotspot(isChecked ? 0 : SizeUtils.dp2px(100), SizeUtils.dp2px(18));
        }
        rippleDrawable.setState(value ? pressedState : StateSet.NOTHING);
        invalidate();
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || rippleDrawable != null && who == rippleDrawable;
    }

    private void animateToCheckedState(boolean newCheckedState) {
        checkAnimator = ObjectAnimator.ofFloat(this, "progress", newCheckedState ? 1 : 0);
        checkAnimator.setDuration(250);
        checkAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                checkAnimator = null;
            }
        });
        checkAnimator.start();
    }

    private void animateIcon(boolean newCheckedState) {
        iconAnimator = ObjectAnimator.ofFloat(this, "iconProgress", newCheckedState ? 1 : 0);
        iconAnimator.setDuration(250);
        iconAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                iconAnimator = null;
            }
        });
        iconAnimator.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        attachedToWindow = false;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        onCheckedChangeListener = listener;
    }

    public void setChecked(boolean checked, boolean animated) {
        setChecked(checked, drawIconType, animated);
    }

    public void setChecked(boolean checked, int iconType, boolean animated) {
        if (checked != isChecked) {
            isChecked = checked;
            if (attachedToWindow && animated) {
                animateToCheckedState(checked);
            } else {
                cancelCheckAnimator();
                setProgress(checked ? 1.0f : 0.0f);
            }
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onCheckedChanged(this, checked);
            }
        }
        if (drawIconType != iconType) {
            drawIconType = iconType;
            if (attachedToWindow && animated) {
                animateIcon(iconType == 0);
            } else {
                cancelIconAnimator();
                setIconProgress(iconType == 0 ? 1.0f : 0.0f);
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setIcon(int icon) {
        if (icon != 0) {
            iconDrawable = context.getDrawable(icon).mutate();
            if (iconDrawable != null) {
                iconDrawable.setColorFilter(new PorterDuffColorFilter(lastIconColor = isChecked ? 0xff52ade9 : 0xfff57e7e, PorterDuff.Mode.SRC_IN));
            }
        } else {
            iconDrawable = null;
        }
    }

    public boolean hasIcon() {
        return iconDrawable != null;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setOverrideColor(int override) {
        if (overrideColorProgress == override) {
            return;
        }
        if (overlayBitmap == null) {
            try {
                overlayBitmap = new Bitmap[2];
                overlayCanvas = new Canvas[2];
                for (int a = 0; a < 2; a++) {
                    overlayBitmap[a] = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                    overlayCanvas[a] = new Canvas(overlayBitmap[a]);
                }
                overlayMaskBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                overlayMaskCanvas = new Canvas(overlayMaskBitmap);

                overlayEraserPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                overlayEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

                overlayMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                overlayMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                bitmapsCreated = true;
            } catch (Throwable e) {
                return;
            }
        }
        if (!bitmapsCreated) {
            return;
        }
        overrideColorProgress = override;
        overlayCx = 0;
        overlayCy = 0;
        overlayRad = 0;
        invalidate();
    }

    public void setOverrideColorProgress(float cx, float cy, float rad) {
        overlayCx = cx;
        overlayCy = cy;
        overlayRad = rad;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getVisibility() != VISIBLE) {
            return;
        }

        int width = SizeUtils.dp2px(31);
        int x = (getMeasuredWidth() - width) / 2;
        float y = (getMeasuredHeight() - SizeUtils.dp2px(14)) >> 1;
        int tx = x + SizeUtils.dp2px(7) + (int) (SizeUtils.dp2px(17) * progress);
        int ty = getMeasuredHeight() / 2;


        int color1;
        int color2;
        float colorProgress;
        int r1;
        int r2;
        int g1;
        int g2;
        int b1;
        int b2;
        int a1;
        int a2;
        int red;
        int green;
        int blue;
        int alpha;
        int color;

        for (int a = 0; a < 2; a++) {
            if (a == 1 && overrideColorProgress == 0) {
                continue;
            }
            Canvas canvasToDraw = a == 0 ? canvas : overlayCanvas[0];

            if (a == 1) {
                overlayBitmap[0].eraseColor(0);
                paint.setColor(0xff000000);
                overlayMaskCanvas.drawRect(0, 0, overlayMaskBitmap.getWidth(), overlayMaskBitmap.getHeight(), paint);
                overlayMaskCanvas.drawCircle(overlayCx - getX(), overlayCy - getY(), overlayRad, overlayEraserPaint);
            }
            if (overrideColorProgress == 1) {
                colorProgress = a == 0 ? 0 : 1;
            } else if (overrideColorProgress == 2) {
                colorProgress = a == 0 ? 1 : 0;
            } else {
                colorProgress = progress;
            }

            color1 = 0xfff57e7e;
            color2 = 0xff52ade9;
            if (a == 0 && iconDrawable != null && lastIconColor != (isChecked ? color2 : color1)) {
                iconDrawable.setColorFilter(new PorterDuffColorFilter(lastIconColor = (isChecked ? color2 : color1), PorterDuff.Mode.SRC_IN));
            }

            r1 = Color.red(color1);
            r2 = Color.red(color2);
            g1 = Color.green(color1);
            g2 = Color.green(color2);
            b1 = Color.blue(color1);
            b2 = Color.blue(color2);
            a1 = Color.alpha(color1);
            a2 = Color.alpha(color2);

            red = (int) (r1 + (r2 - r1) * colorProgress);
            green = (int) (g1 + (g2 - g1) * colorProgress);
            blue = (int) (b1 + (b2 - b1) * colorProgress);
            alpha = (int) (a1 + (a2 - a1) * colorProgress);
            color = ((alpha & 0xff) << 24) | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);
            paint.setColor(color);
            paint2.setColor(color);

            rectF.set(x, y, x + width, y + SizeUtils.dp2px(14));
            canvasToDraw.drawRoundRect(rectF, SizeUtils.dp2px(7), SizeUtils.dp2px(7), paint);
            canvasToDraw.drawCircle(tx, ty, SizeUtils.dp2px(10), paint);

            if (a == 0 && rippleDrawable != null) {
                rippleDrawable.setBounds(tx - SizeUtils.dp2px(18), ty - SizeUtils.dp2px(18), tx + SizeUtils.dp2px(18), ty + SizeUtils.dp2px(18));
                rippleDrawable.draw(canvasToDraw);
            } else if (a == 1) {
                canvasToDraw.drawBitmap(overlayMaskBitmap, 0, 0, overlayMaskPaint);
            }
        }
        if (overrideColorProgress != 0) {
            canvas.drawBitmap(overlayBitmap[0], 0, 0, null);
        }

        for (int a = 0; a < 2; a++) {
            if (a == 1 && overrideColorProgress == 0) {
                continue;
            }
            Canvas canvasToDraw = a == 0 ? canvas : overlayCanvas[1];

            if (a == 1) {
                overlayBitmap[1].eraseColor(0);
            }
            if (overrideColorProgress == 1) {
                colorProgress = a == 0 ? 0 : 1;
            } else if (overrideColorProgress == 2) {
                colorProgress = a == 0 ? 1 : 0;
            } else {
                colorProgress = progress;
            }

            color1 = 0xffffffff;
            color2 = 0xffffffff;
            r1 = Color.red(color1);
            r2 = Color.red(color2);
            g1 = Color.green(color1);
            g2 = Color.green(color2);
            b1 = Color.blue(color1);
            b2 = Color.blue(color2);
            a1 = Color.alpha(color1);
            a2 = Color.alpha(color2);

            red = (int) (r1 + (r2 - r1) * colorProgress);
            green = (int) (g1 + (g2 - g1) * colorProgress);
            blue = (int) (b1 + (b2 - b1) * colorProgress);
            alpha = (int) (a1 + (a2 - a1) * colorProgress);
            paint.setColor(((alpha & 0xff) << 24) | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff));

            canvasToDraw.drawCircle(tx, ty, SizeUtils.dp2px(8), paint);

            if (a == 0) {
                if (iconDrawable != null) {
                    iconDrawable.setBounds(tx - iconDrawable.getIntrinsicWidth() / 2, ty - iconDrawable.getIntrinsicHeight() / 2, tx + iconDrawable.getIntrinsicWidth() / 2, ty + iconDrawable.getIntrinsicHeight() / 2);
                    iconDrawable.draw(canvasToDraw);
                } else if (drawIconType == 1) {
                    tx -= SizeUtils.dp2px(10.8f) - SizeUtils.dp2px(1.3f) * progress;
                    ty -= SizeUtils.dp2px(8.5f) - SizeUtils.dp2px(0.5f) * progress;
                    int startX2 = SizeUtils.dp2px(4.6f) + tx;
                    int startY2 = SizeUtils.dp2px(9.5f) + ty;
                    int endX2 = startX2 + SizeUtils.dp2px(2);
                    int endY2 = startY2 + SizeUtils.dp2px(2);

                    int startX = SizeUtils.dp2px(7.5f) + tx;
                    int startY = SizeUtils.dp2px(5.4f) + ty;
                    int endX = startX + SizeUtils.dp2px(7);
                    int endY = startY + SizeUtils.dp2px(7);

                    startX = (int) (startX + (startX2 - startX) * progress);
                    startY = (int) (startY + (startY2 - startY) * progress);
                    endX = (int) (endX + (endX2 - endX) * progress);
                    endY = (int) (endY + (endY2 - endY) * progress);
                    canvasToDraw.drawLine(startX, startY, endX, endY, paint2);

                    startX = SizeUtils.dp2px(7.5f) + tx;
                    startY = SizeUtils.dp2px(12.5f) + ty;
                    endX = startX + SizeUtils.dp2px(7);
                    endY = startY - SizeUtils.dp2px(7);
                    canvasToDraw.drawLine(startX, startY, endX, endY, paint2);
                } else if (drawIconType == 2 || iconAnimator != null) {
                    paint2.setAlpha((int) (255 * (1.0f - iconProgress)));
                    canvasToDraw.drawLine(tx, ty, tx, ty - SizeUtils.dp2px(5), paint2);
                    canvasToDraw.save();
                    canvasToDraw.rotate(-90 * iconProgress, tx, ty);
                    canvasToDraw.drawLine(tx, ty, tx + SizeUtils.dp2px(4), ty, paint2);
                    canvasToDraw.restore();
                }
            }
            if (a == 1) {
                canvasToDraw.drawBitmap(overlayMaskBitmap, 0, 0, overlayMaskPaint);
            }
        }
        if (overrideColorProgress != 0) {
            canvas.drawBitmap(overlayBitmap[1], 0, 0, null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = Math.min(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(size, size);
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName("android.widget.Switch");
        info.setCheckable(true);
        info.setChecked(isChecked);
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(Switch view, boolean isChecked);
    }
}