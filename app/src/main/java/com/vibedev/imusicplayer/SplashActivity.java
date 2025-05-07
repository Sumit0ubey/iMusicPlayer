package com.vibedev.imusicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        BasicActivity.applyWindowInsets(findViewById(R.id.splash));

        SharedPreferences userPreference = getSharedPreferences("settings", MODE_PRIVATE);

        String themePre = "System";
        String fontPre = "Default";
        String colorPre = "Default";

        if(userPreference.contains("themeChoice") && userPreference.contains("fontFamily") && userPreference.contains("textColor")) {
            themePre = userPreference.getString("themeChoice", "System");
            fontPre = userPreference.getString("fontFamily", "Default");
            colorPre = userPreference.getString("textColor", "Default");
        }

        BasicActivity.userPreferenceFontFamily = fontPre;
        BasicActivity.userPreferenceTheme = themePre;
        BasicActivity.userPreferenceTextColor = colorPre;

        ImageView logo = findViewById(R.id.logoImage);

        logo.setAlpha(0f);
        logo.animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(100)
                .start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1500);
    }
}