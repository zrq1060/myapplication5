package com.example.myapplication.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

public class TextSizeTransition3 extends Transition {

    private static final String PROPNAME_TEXT_SIZE = "com.example:TextSizeTransition:textSize";
    private static final String[] TRANSITION_PROPERTIES = {PROPNAME_TEXT_SIZE};

    public TextSizeTransition3() {
    }

    public TextSizeTransition3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public String[] getTransitionProperties() {
        return TRANSITION_PROPERTIES;
    }

    // 捕获起始值
    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    // 捕获结束值
    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues values) {
        if (values.view instanceof TextView) {
            TextView textView = (TextView) values.view;
            // 保存当前字体大小（单位：px）
            values.values.put(PROPNAME_TEXT_SIZE, textView.getTextSize());
        }
    }

    // 创建动画
    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        Log.e("aaaaaa", "createAnimator==startSize=" );
        if (startValues == null || endValues == null) return null;

        // 确保是同一个 TextView
        if (startValues.view != endValues.view || !(startValues.view instanceof TextView)) {
            return null;
        }

        TextView textView = (TextView) endValues.view;
        float startSize = (float) startValues.values.get(PROPNAME_TEXT_SIZE);
        float endSize = (float) endValues.values.get(PROPNAME_TEXT_SIZE);

        Log.e("aaaaaa", "createAnimator==startSize=" + startSize + ",endSize=" + endSize);


        // 如果大小未变化，不创建动画
        if (startSize == endSize) return null;

        // 关键：使用 PropertyValuesHolder 动画字体大小
        PropertyValuesHolder textSizeHolder = PropertyValuesHolder.ofFloat(
                "TextSize", startSize, endSize
        );
        return ObjectAnimator.ofPropertyValuesHolder(textView, textSizeHolder);
    }
}