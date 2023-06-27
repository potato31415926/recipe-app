package com.yuzule.recipe.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.yuzule.recipe.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private final int countdownDuration = 5;
    private MaterialButton skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences prefs = getSharedPreferences("Recipe", MODE_PRIVATE);
        int theme = prefs.getInt("theme", 0); // 0为亮色主题，1为暗色主题
        if (theme == 0) {
            setTheme(R.style.Theme_Recipe);
        } else {
            setTheme(R.style.Theme_Recipe_Dark);
        }
        setContentView(R.layout.activity_splash);
        // 获取Logo和底部文字对象
        ImageView launchImage = findViewById(R.id.launch_image);
        TextView launchText = findViewById(R.id.launch_text);
        // 加载两段动画
        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.splash_image);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.splash_text);
        // 启动动画
        launchImage.startAnimation(anim1);
        launchText.startAnimation(anim2);
        skip = findViewById(R.id.skip_button);
        timer.start();
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startToActivity();
            }

        });
    }

    private void startToActivity() {
        timer.cancel();
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private final CountDownTimer timer = new CountDownTimer(countdownDuration * 1000, 1000) {
        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            skip.setText("跳过(" + (millisUntilFinished / 1000) + ")");
        }

        @Override
        public void onFinish() {
            startToActivity();
        }
    };


}
