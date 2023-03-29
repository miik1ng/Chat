package com.mik1ng.chat.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.Nullable;

public class LoadingView extends View {

    private RotateAnimation animation;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void start() {
        if (animation == null) {
            animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        animation.setDuration(1000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setInterpolator(interpolator);
        startAnimation(animation);
    }

    public void cancel() {
        if (animation != null) {
            animation.cancel();
            animation = null;
        }
    }
}
