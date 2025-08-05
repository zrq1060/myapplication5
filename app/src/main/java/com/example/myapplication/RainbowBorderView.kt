package com.example.myapplication

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class RainbowBorderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH  // 边框宽度
    }
    private val rect = RectF()

    // 颜色数组
    private val rainbowColors = intArrayOf(
        Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED
    )

    // 可以指定透明背景
//    private val rainbowColors = intArrayOf(
//        Color.parseColor("#9aff0000"),
//        Color.parseColor("#9a00ff00"),
//        Color.parseColor("#9a0000ff"),
//        Color.parseColor("#9aff0000"),
//    )

    private var shader: SweepGradient? = null
    private val matrix = Matrix()
    // 动画
    private var animator: ValueAnimator? = null
    private var degreesOffset = 0f

    init {
        startAnimation()
    }

    private fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 4000L // 一圈的时间
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator() // 保持匀速，必须添加。
            addUpdateListener {
                // 旋转的角度偏移
                degreesOffset = it.animatedValue as Float
                // 重新绘制
                invalidate()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // 页面销毁，取消动画。
        animator?.cancel()
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // 下面的内容，放到这个位置，优化在onDraw内频繁设置，在此方法内可以获取到View的宽高。
        // 设置矩形的位置，去除线的宽度。
        rect.set(
            HALF_STROKE_WIDTH,
            HALF_STROKE_WIDTH,
            width - HALF_STROKE_WIDTH,
            height - HALF_STROKE_WIDTH
        )
        // 设置shader着色器
        shader = SweepGradient(
            width / 2f,
            height / 2f,
            rainbowColors,
            null
        )
        paint.shader = shader
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 旋转matrix来制造效果
        matrix.setRotate(degreesOffset % 360, width / 2f, height / 2f)
        shader?.setLocalMatrix(matrix)

        // 画圆角矩形
        canvas.drawRoundRect(rect, ROUND_RECT_RADIUS, ROUND_RECT_RADIUS, paint)
    }

    companion object {
        private const val STROKE_WIDTH = 130f // 线的宽度
        private const val ROUND_RECT_RADIUS = 10f // 圆角矩形半径

        private const val HALF_STROKE_WIDTH = STROKE_WIDTH / 2f // 一半线的宽度
    }
}