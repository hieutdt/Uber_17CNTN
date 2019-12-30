package com.example.cntn_grab.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.cntn_grab.Helpers.GIFImageView;
import com.example.cntn_grab.R;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 6000;
    private GIFImageView mGifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mGifImageView = findViewById(R.id.splash_screen_gif_image_view);
        mGifImageView.setGifImageResource(R.drawable.scooter_driving);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
