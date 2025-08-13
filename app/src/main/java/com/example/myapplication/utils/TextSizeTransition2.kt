package com.example.myapplication.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.transition.Transition
import android.transition.TransitionValues
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView

class TextSizeTransition2 : Transition() {

    override fun captureStartValues(values: TransitionValues) {
        captureValues(values)
    }

    override fun captureEndValues(values: TransitionValues) {
        captureValues(values)
    }

    private fun captureValues(values: TransitionValues) {
        if (values.view is TextView) {
            values.values.put(PROP_TEXT_SIZE, (values.view as TextView).textSize)
        }
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        if (startValues == null || endValues == null) return null
        val view = endValues.view as? TextView ?: return null

        val startSize = 60f
        val endSize = 120f


//        val startSize = startValues.values[PROP_TEXT_SIZE] as Float
//        val endSize = endValues.values[PROP_TEXT_SIZE] as Float

        val animator = ValueAnimator.ofFloat(startSize, endSize)
        animator.addUpdateListener { animation: ValueAnimator ->
            Log.e("aaaaaa","TextSizeTransition-addUpdateListener=${animation.animatedValue}")
            view.setTextSize(
                TypedValue.COMPLEX_UNIT_PX, animation.animatedValue as Float
            )
        }
        return animator
    }

    companion object {
        private const val PROP_TEXT_SIZE = "custom:text_size"
    }
}


///**/ 使用自定义 Transition */
// TransitionSet set = new TransitionSet()
//        .addTransition(new ChangeBounds())
//        .addTransition(new TextSizeTransition()); // 添加自定义字体过渡
//getWindow().setSharedElementEnterTransition(set);

