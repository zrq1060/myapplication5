package com.example.myapplication.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View

// 目前的这个效果很接近需求了。
class RainbowBorderView2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 100f  // 边框宽度
    }

    private val rect = RectF()
    private var animator: ValueAnimator? = null
    private var offset = 0f

    // 彩虹颜色数组
    private val rainbowColors = intArrayOf(
        Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED
    )
    private val shader = SweepGradient(
        width / 2f,
        height / 2f,
        rainbowColors,
        null
    )

    private val matrix = Matrix()

    init {
        startAnimation()
    }

    private fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            ValueAnimator.setDuration = 3000L
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                offset = it.animatedFraction
                invalidate()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val borderWidth = borderPaint.strokeWidth
        rect.set(
            borderWidth / 2,
            borderWidth / 2,
            width - borderWidth / 2,
            height - borderWidth / 2
        )


        // 平移 matrix 来制造滚动的效果
        matrix.setRotate(offset * 360, width / 2f, height / 2f)
        shader.setLocalMatrix(matrix)

        borderPaint.shader = shader

        // 画圆角矩形边框
        canvas.drawRoundRect(rect, 40f, 40f, borderPaint)
    }
}
