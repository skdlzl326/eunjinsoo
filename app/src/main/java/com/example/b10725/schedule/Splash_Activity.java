package com.example.b10725.schedule;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by B10726 on 2016-11-15.
 */

public class Splash_Activity extends Activity {
    final String TAG = "AnimationTest";
    FrameLayout mFrame;
    ImageView mMan;
    int mScreenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mFrame = (FrameLayout)findViewById(R.id.activity_main);
    /*    mCountDown = (ImageView) findViewById(R.id.countdown);
        mFirework = (ImageView) findViewById(R.id.fire);*/
        mMan = (ImageView) findViewById(R.id.rocket);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenHeight = displaymetrics.heightPixels;

        startFireValuePropertyAnimation();

    }



    private void startFireValuePropertyAnimation() {

        mMan.setScaleX(0);
        mMan.setScaleY(0);

        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(0, 1.0f);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mMan.setScaleX(value);
                mMan.setScaleY(value);
            }
        });

        ValueAnimator RotateAnimator = ValueAnimator.ofFloat(0, 360);
        RotateAnimator.setDuration(2000);
        RotateAnimator.setStartDelay(3000);
        RotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mMan.setRotation(value);

            }
        });
        ValueAnimator AlphamanAnimator = ValueAnimator.ofFloat(1, 0);
        AlphamanAnimator.setDuration(2000);
        AlphamanAnimator.setStartDelay(5000);
        AlphamanAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mMan.setAlpha(value);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(scaleAnimator,RotateAnimator, AlphamanAnimator);
        animatorSet.setStartDelay(2000);
        animatorSet.setDuration(2000);
        animatorSet.start();
        animatorSet.addListener(animatorListener);


    }


    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            Log.i(TAG, "onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            Log.i(TAG, "onAnimationEnd");
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            Log.i(TAG, "onAnimationCancel");
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            Log.i(TAG, "onAnimationRepeat");
        }
    };



}