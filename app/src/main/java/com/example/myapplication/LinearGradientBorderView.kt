package com.example.myapplication

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

// 这个效果不是
class LinearGradientBorderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 32f  // 边框宽度
    }

    private val rect = RectF()
    private var animator: ValueAnimator? = null
    private var translateX = 0f
    private var shaderWidth = 0f

    // 彩虹渐变颜色
    private val rainbowColors = intArrayOf(
        Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED
    )

    init {
        startAnimation()
    }

    private fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 3000L
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                val fraction = it.animatedFraction
                translateX = shaderWidth * fraction
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

        val border = borderPaint.strokeWidth / 2
        rect.set(border, border, width - border, height - border)

        shaderWidth = width.toFloat() * 2  // 渐变区域比控件宽，制造流动感

        val shader = LinearGradient(
            0f, 0f,
            shaderWidth, 0f,
            rainbowColors,
            null,
            Shader.TileMode.MIRROR  // 或 TileMode.CLAMP
        )

        val matrix = Matrix()
        matrix.setTranslate(translateX, 0f)
        shader.setLocalMatrix(matrix)

        borderPaint.shader = shader

        // 可选圆角，设为 0f 就是直角矩形
        canvas.drawRoundRect(rect, 40f, 40f, borderPaint)
    }
}
