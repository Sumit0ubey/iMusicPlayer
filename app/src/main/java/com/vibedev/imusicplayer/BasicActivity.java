package com.vibedev.imusicplayer;

import android.view.View;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class BasicActivity{
    protected static String previous = null;
    protected static String selectedSong = null;

    protected static String userPreferenceFontFamily = "Default";
    protected static String userPreferenceTheme = "System";
    protected static String userPreferenceTextColor = "Default";

    protected static final Map<String, Integer> colorMap = Map.of(
            "Teal", R.color.teal,
            "Coral", R.color.coral,
            "Indigo", R.color.indigo,
            "Slat Gray", R.color.slat_gray
    );
    protected static void applyWindowInsets(View view) {
    ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
    });
}
}