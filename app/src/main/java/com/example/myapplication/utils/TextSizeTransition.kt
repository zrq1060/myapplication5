package com.example.myapplication.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.transition.Transition
import android.transition.TransitionValues
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView

class TextSizeTransition : Transition() {
    private val PROPNAME_TEXT_SIZE = "customtransition:textsize"

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(values: TransitionValues) {
        val view = values.view
        if (view is TextView) {
            values.values[PROPNAME_TEXT_SIZE] = view.textSize
        }
    }

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues?): Animator? {
        if (startValues == null || endValues == null) return null

        val view = endValues.view as? TextView ?: return null
//        val startSize = startValues.values[PROPNAME_TEXT_SIZE] as Float
//        val endSize = endValues.values[PROPNAME_TEXT_SIZE] as Float
        val startSize = 120f
        val endSize = 60f

        if (startSize == endSize) return null

        return ValueAnimator.ofFloat(startSize, endSize).apply {
            addUpdateListener {
                val size = it.animatedValue as Float
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
            }
        }
    }
}
