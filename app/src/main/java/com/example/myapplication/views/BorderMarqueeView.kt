package com.example.myapplication.views

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class BorderMarqueeView : View {
    // 绘制参数
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val spotPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderRect = RectF() // 边框区域

    // 动画参数
    private var animator: ValueAnimator? = null
    private var progress = 0f // 动画进度 (0.0 - 1.0)

    // 自定义属性 (可在XML中设置)
    private val borderColor = Color.parseColor("#FF4081") // 边框颜色
    private val spotColor = Color.parseColor("#FFFFFF") // 光点颜色
    private val borderWidth = 8f // 边框宽度 (像素)
    private val spotRadius = 12f // 光点半径
    private val duration = 2000 // 一圈动画时长(ms)

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        // 边框画笔设置
        borderPaint.setStyle(Paint.Style.STROKE)
        borderPaint.setColor(borderColor)
        borderPaint.setStrokeWidth(borderWidth)
        borderPaint.setStrokeCap(Paint.Cap.ROUND)

        // 光点画笔设置
        spotPaint.setStyle(Paint.Style.FILL)
        spotPaint.setColor(spotColor)

        // 初始化动画
        setupAnimator()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 计算边框区域（考虑画笔宽度）
        val halfStroke = borderWidth / 2
        borderRect.set(
            halfStroke,
            halfStroke,
            w - halfStroke,
            h - halfStroke
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 1. 绘制边框
        canvas.drawRect(borderRect, borderPaint)

        // 2. 计算光点位置
        var spotX = 0f
        var spotY = 0f
        val totalLength = 2 * (borderRect.width() + borderRect.height())
        val currentPos = progress * totalLength

        // 根据当前位置计算坐标
        if (currentPos < borderRect.width()) {  // 上边框
            spotX = borderRect.left + currentPos
            spotY = borderRect.top
        } else if (currentPos < borderRect.width() + borderRect.height()) {  // 右边框
            spotX = borderRect.right
            spotY = borderRect.top + (currentPos - borderRect.width())
        } else if (currentPos < 2 * borderRect.width() + borderRect.height()) {  // 下边框
            spotX = borderRect.right - (currentPos - borderRect.width() - borderRect.height())
            spotY = borderRect.bottom
        } else {  // 左边框
            spotX = borderRect.left
            spotY = borderRect.bottom - (currentPos - 2 * borderRect.width() - borderRect.height())
        }

        // 3. 绘制光点
        canvas.drawCircle(spotX, spotY, spotRadius, spotPaint)
    }

    private fun setupAnimator() {
        animator = ValueAnimator.ofFloat(0f, 1f)
        animator!!.setDuration(duration.toLong())
        animator!!.setRepeatCount(ValueAnimator.INFINITE)
        animator!!.setInterpolator(LinearInterpolator())
        animator!!.addUpdateListener(AnimatorUpdateListener { animation: ValueAnimator? ->
            progress = animation!!.getAnimatedValue() as Float
            invalidate() // 触发重绘
        })
    }

    // 开始动画
    fun startAnimation() {
        if (animator != null && !animator!!.isRunning()) {
            animator!!.start()
        }
    }

    // 停止动画
    fun stopAnimation() {
        if (animator != null && animator!!.isRunning()) {
            animator!!.cancel()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    // 属性设置方法 (可选)
    fun setBorderColor(color: Int) {
        borderPaint.setColor(color)
        invalidate()
    }

    fun setSpotColor(color: Int) {
        spotPaint.setColor(color)
        invalidate()
    }

    fun setBorderWidth(width: Float) {
        borderPaint.setStrokeWidth(width)
        invalidate()
    }
}