package com.android.mig.simpletimeclock.view.utils;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;

import com.android.mig.simpletimeclock.R;

public class FlipAnimator {

    /**
     * Performs flip animation on two views
     */
    public static void flipView(Context context, final View back, final View front, boolean showFront) {
        AnimatorSet leftIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in);
        AnimatorSet rightOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_out);
        AnimatorSet leftOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_out);
        AnimatorSet rightIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_in);

        final AnimatorSet showFrontAnim = new AnimatorSet();
        final AnimatorSet showBackAnim = new AnimatorSet();

        leftIn.setTarget(back);
        rightOut.setTarget(front);
        showFrontAnim.playTogether(leftIn, rightOut);

        leftOut.setTarget(back);
        rightIn.setTarget(front);
        showBackAnim.playTogether(rightIn, leftOut);

        if (showFront) {
            showFrontAnim.start();
        } else {
            showBackAnim.start();
        }
    }
}
