package com.c2m.storyviewer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public final class PausableProgressBar extends FrameLayout {

    private static final int DEFAULT_PROGRESS_DURATION = 4000;

    private View frontProgressView;
    private View maxProgressView;

    private PausableScaleAnimation animation;
    private long duration = DEFAULT_PROGRESS_DURATION;
    private Callback callback;
    private boolean isStarted = false;

    public PausableProgressBar(Context context) {
        this(context, null);
    }

    public PausableProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PausableProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.pausable_progress, this);
        frontProgressView = findViewById(R.id.front_progress);
        maxProgressView = findViewById(R.id.max_progress);
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setCallback(@NonNull Callback callback) {
        this.callback = callback;
    }

    void setMax() {
        finishProgress(true);
    }

    void setMin() {
        finishProgress(false);
    }

    void setMinWithoutCallback() {
        maxProgressView.setBackgroundResource(R.color.progress_secondary);

        maxProgressView.setVisibility(VISIBLE);
        if (animation != null) {
            animation.setAnimationListener(null);
            animation.cancel();
        }
    }

    void setMaxWithoutCallback() {
        maxProgressView.setBackgroundResource(R.color.progress_max_active);

        maxProgressView.setVisibility(VISIBLE);
        if (animation != null) {
            animation.setAnimationListener(null);
            animation.cancel();
        }
    }

    private void finishProgress(boolean isMax) {
        if (isMax) maxProgressView.setBackgroundResource(R.color.progress_max_active);
        maxProgressView.setVisibility(isMax ? VISIBLE : GONE);
        if (animation != null) {
            animation.setAnimationListener(null);
            animation.cancel();
            if (callback != null) {
                callback.onFinishProgress();
            }
        }
    }

    public void startProgress() {
        maxProgressView.setVisibility(GONE);
        if (duration <= 0) duration = 4000;
        animation = new PausableScaleAnimation(0, 1, 1, 1, Animation.ABSOLUTE, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(duration);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (isStarted) {
                    return;
                }
                isStarted = true;
                frontProgressView.setVisibility(View.VISIBLE);
                if (callback != null) callback.onStartProgress();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isStarted = false;
                if (callback != null) callback.onFinishProgress();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //NO-OP
            }
        });
        animation.setFillAfter(true);
        frontProgressView.startAnimation(animation);
    }

    public void pauseProgress() {
        if (animation != null) {
            animation.pause();
        }
    }

    public void resumeProgress() {
        if (animation != null) {
            animation.resume();
        }
    }

    void clear() {
        if (animation != null) {
            animation.setAnimationListener(null);
            animation.cancel();
            animation = null;
        }
    }

    interface Callback {
        void onStartProgress();

        void onFinishProgress();
    }

    private class PausableScaleAnimation extends ScaleAnimation {

        private long elapsedAtPause = 0;
        private boolean isPaused = false;

        PausableScaleAnimation(float fromX, float toX, float fromY,
                               float toY, int pivotXType, float pivotXValue, int pivotYType,
                               float pivotYValue) {
            super(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType,
                    pivotYValue);
        }

        @Override
        public boolean getTransformation(long currentTime, Transformation outTransformation, float scale) {
            if (isPaused && elapsedAtPause == 0) {
                elapsedAtPause = currentTime - getStartTime();
            }
            if (isPaused) {
                setStartTime(currentTime - elapsedAtPause);
            }
            return super.getTransformation(currentTime, outTransformation, scale);
        }

        void pause() {
            if (isPaused) return;
            elapsedAtPause = 0;
            isPaused = true;
        }

        void resume() {
            isPaused = false;
        }
    }
}
