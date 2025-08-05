package com.example.myapplication.views

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class BlinkingBorderView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var borderPaint: Paint? = null
    private var animator: ValueAnimator? = null
    private var alpha = 255 // 初始不透明

    init {
        init()
    }

    private fun init() {
        borderPaint = Paint()
        borderPaint!!.setColor(Color.RED)
        borderPaint!!.setStrokeWidth(50f)
        borderPaint!!.setStyle(Paint.Style.STROKE)
        borderPaint!!.setAntiAlias(true)

        animator = ValueAnimator.ofInt(255, 50, 255) // 闪烁透明度
        animator!!.setDuration(1000)
        animator!!.setRepeatCount(ValueAnimator.INFINITE)
        animator!!.setRepeatMode(ValueAnimator.REVERSE)
        animator!!.addUpdateListener(AnimatorUpdateListener { animation: ValueAnimator? ->
            alpha = animation!!.getAnimatedValue() as Int
            invalidate()
        })
        animator!!.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        borderPaint!!.setAlpha(alpha)
        val rect = Rect(0, 0, getWidth(), getHeight())
        canvas.drawRect(rect, borderPaint!!)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (animator != null) {
            animator!!.cancel()
        }
    }
}
