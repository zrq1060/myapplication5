package com.example.myapplication.utils

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.drawable.ColorDrawable
import android.transition.Transition
import android.transition.TransitionValues
import android.util.Log
import android.view.ViewGroup

class ChangeBgColorTransition : Transition() {
    companion object {
        const val PROPNAME_BG_COLOR = "lx:ChangeBgColorTransition:bgColor"
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        sout("captureStartValues=${(transitionValues.view.background as? ColorDrawable)?.color}")

        transitionValues.values.put(
            PROPNAME_BG_COLOR,
            (transitionValues.view.background as? ColorDrawable)?.color
        )
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        sout("captureEndValues=${(transitionValues.view.background as? ColorDrawable)?.color}")

        transitionValues.values.put(
            PROPNAME_BG_COLOR,
            (transitionValues.view.background as? ColorDrawable)?.color
        )
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        val view = endValues?.view ?: return null
        val startColor = startValues?.values?.get(PROPNAME_BG_COLOR) as? Int ?: return null
        val endColor = endValues.values?.get(PROPNAME_BG_COLOR) as? Int ?: return null
        // 创建背景颜色变化的动画
        return ObjectAnimator.ofObject(
            view,
            "backgroundColor",
            ArgbEvaluator(),
            startColor,
            endColor
        )
    }

    private fun sout(string: String) {
        Log.e("aaaaaaaaaaaa", string)
    }
}