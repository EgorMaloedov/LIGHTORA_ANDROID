package com.example.lightora.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.lightora.R;

public class PreloaderActivity extends AppCompatActivity {

    private LottieAnimationView anim = null;

    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            Intent intent = new Intent(PreloaderActivity.this, FirstStartActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescreen_act);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        anim = findViewById(R.id.lottieAnim);
        anim.playAnimation();
        anim.addAnimatorListener(animatorListener);
    }
}