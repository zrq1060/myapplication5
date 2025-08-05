// MarqueeBorderView.kt
package com.example.myapplication.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class MarqueeBorderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    // 边框宽度
    private val strokeWidth = 8f.dp
    // 跑马灯渐变色数组
    private val colors = intArrayOf(
        Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = this@MarqueeBorderView.strokeWidth
    }

    // 渐变着色器
    private lateinit var shader: LinearGradient
    private val shaderMatrix = Matrix()

    // 动画进度 [0f, gradientLength)
    private var translate = 0f

    private lateinit var animator: ValueAnimator

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 渐变长度取为边框周长的一半，便于循环
        val gradientLength = (w + h) / 2f
        shader = LinearGradient(
            0f, 0f, gradientLength, 0f,
            colors, null, Shader.TileMode.MIRROR
        )
        paint.shader = shader

        // 创建一个无限循环的平移动画
        animator = ValueAnimator.ofFloat(0f, gradientLength).apply {
            duration = 2000L              // 整体跑一圈耗时
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                translate = it.animatedValue as Float
                invalidate()              // 触发重绘
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 更新 shaderMatrix，使得渐变不断往右移动
        shaderMatrix.setTranslate(translate, 0f)
        shader.setLocalMatrix(shaderMatrix)

        // 绘制四边框
        val half = strokeWidth / 2
        canvas.drawRect(
            half, half,
            width - half, height - half,
            paint
        )
    }

    // dp 扩展
    private val Float.dp: Float
        get() = this * context.resources.displayMetrics.density
}
