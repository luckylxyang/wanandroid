package com.lxy.basemodel.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Administrator on 2020/1/7.
 */

public class AnimatorUtils {

    public static void collect(View view, AnimatorCallback callback){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"scaleX",1f,0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view,"scaleX",0f,1f);
        animator.setDuration(300).start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                callback.onAnimationEnd();
                animator2.setDuration(300).start();
            }
        });
    }

    public interface AnimatorCallback{
        void onAnimationEnd();
    }
}
