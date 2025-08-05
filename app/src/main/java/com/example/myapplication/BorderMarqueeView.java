package com.example.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class BorderMarqueeView extends View {
    // 绘制参数
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint spotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF borderRect = new RectF();  // 边框区域

    // 动画参数
    private ValueAnimator animator;
    private float progress = 0f;  // 动画进度 (0.0 - 1.0)

    // 自定义属性 (可在XML中设置)
    private int borderColor = Color.parseColor("#FF4081");  // 边框颜色
    private int spotColor = Color.parseColor("#FFFFFF");    // 光点颜色
    private float borderWidth = 8f;    // 边框宽度 (像素)
    private float spotRadius = 12f;    // 光点半径
    private int duration = 2000;       // 一圈动画时长(ms)

    public BorderMarqueeView(Context context) {
        super(context);
        init();
    }

    public BorderMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 边框画笔设置
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStrokeCap(Paint.Cap.ROUND);

        // 光点画笔设置
        spotPaint.setStyle(Paint.Style.FILL);
        spotPaint.setColor(spotColor);

        // 初始化动画
        setupAnimator();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 计算边框区域（考虑画笔宽度）
        float halfStroke = borderWidth / 2;
        borderRect.set(
                halfStroke,
                halfStroke,
                w - halfStroke,
                h - halfStroke
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. 绘制边框
        canvas.drawRect(borderRect, borderPaint);

        // 2. 计算光点位置
        float spotX = 0, spotY = 0;
        float totalLength = 2 * (borderRect.width() + borderRect.height());
        float currentPos = progress * totalLength;

        // 根据当前位置计算坐标
        if (currentPos < borderRect.width()) {  // 上边框
            spotX = borderRect.left + currentPos;
            spotY = borderRect.top;
        } else if (currentPos < borderRect.width() + borderRect.height()) {  // 右边框
            spotX = borderRect.right;
            spotY = borderRect.top + (currentPos - borderRect.width());
        } else if (currentPos < 2 * borderRect.width() + borderRect.height()) {  // 下边框
            spotX = borderRect.right - (currentPos - borderRect.width() - borderRect.height());
            spotY = borderRect.bottom;
        } else {  // 左边框
            spotX = borderRect.left;
            spotY = borderRect.bottom - (currentPos - 2 * borderRect.width() - borderRect.height());
        }

        // 3. 绘制光点
        canvas.drawCircle(spotX, spotY, spotRadius, spotPaint);
    }

    private void setupAnimator() {
        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(duration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            progress = (float) animation.getAnimatedValue();
            invalidate();  // 触发重绘
        });
    }

    // 开始动画
    public void startAnimation() {
        if (animator != null && !animator.isRunning()) {
            animator.start();
        }
    }

    // 停止动画
    public void stopAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    // 属性设置方法 (可选)
    public void setBorderColor(int color) {
        borderPaint.setColor(color);
        invalidate();
    }

    public void setSpotColor(int color) {
        spotPaint.setColor(color);
        invalidate();
    }

    public void setBorderWidth(float width) {
        borderPaint.setStrokeWidth(width);
        invalidate();
    }
}