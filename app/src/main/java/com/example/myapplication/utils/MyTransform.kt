package com.example.myapplication.utils

import android.transition.Transition
import android.transition.TransitionValues
import android.util.Log
import android.widget.TextView

class MyTransform : Transition() {
    override fun captureStartValues(transitionValues: TransitionValues) {
//        sout("captureStartValues=$transitionValues")

        val view = transitionValues.view
//        transitionValues.values
        if (view is TextView) {
            sout("captureStartValues=${view.tag}=${view.textSize}")
        }
    }


    override fun captureEndValues(transitionValues: TransitionValues) {
//        sout("captureEndValues=$transitionValues")
        val view = transitionValues.view
        if (view is TextView) {
            sout("captureEndValues=${view.tag}=${view.textSize}")
        }
    }


    private fun sout(string: String) {
        Log.e("aaaaaaaaaaaa", string)
    }
}