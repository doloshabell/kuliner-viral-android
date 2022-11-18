package com.example.newsapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsapplication.R;

public class SplashActivity extends AppCompatActivity {
    Animation animation;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fade_in);
        ImageView imageView = findViewById(R.id.imageIcon);
        TextView textView = findViewById(R.id.appName);
        imageView.setVisibility(View.VISIBLE);
        imageView.startAnimation(animation);
        textView.setVisibility(View.VISIBLE);
        textView.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 3000);
    }
}