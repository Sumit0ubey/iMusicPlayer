package com.vibedev.imusicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ConstraintLayout layout;
    private SwitchCompat switchTheme;
    private Spinner changeFont, changeColor;
    private Button buttonReset, save;
    private CheckBox checkBox;
    private ImageView privacy, appInfo;
    private String[] fontFamily, textColor;
    private String light;
    private String dark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        BasicActivity.applyWindowInsets(findViewById(R.id.setting));

        layout = findViewById(R.id.setting);
        changeFont = findViewById(R.id.changeFont);
        changeColor = findViewById(R.id.changeColor);
        switchTheme = findViewById(R.id.switchTheme);
        buttonReset = findViewById(R.id.buttonReset);
        checkBox = findViewById(R.id.checkBox);
        save = findViewById(R.id.save);
        privacy = findViewById(R.id.privacy);
        appInfo = findViewById(R.id.setting_appInfo);

        toolbar = findViewById(R.id.settingToolbar);
        setSupportActionBar(toolbar);

        light = getString(R.string.light);
        dark = getString(R.string.dark);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }

        String themePre = BasicActivity.userPreferenceTheme;
        String fontPre = BasicActivity.userPreferenceFontFamily;
        String colorPre = BasicActivity.userPreferenceTextColor;

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        int nightModeFlag = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        if (themePre.equals("System")) {
            if (nightModeFlag == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_toolbar));
                toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.dark_toolbar));
                switchTheme.setText(dark);
                switchTheme.setChecked(true);
            } else {
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
            }
        } else if (themePre.equals("Dark")) {
            layout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_toolbar));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_toolbar));
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.dark_toolbar));
            switchTheme.setText(dark);
            switchTheme.setChecked(true);
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        }

        fontFamily = getResources().getStringArray(R.array.fontFamily);
        ArrayAdapter<String> font = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fontFamily);
        font.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        changeFont.setAdapter(font);

        textColor = getResources().getStringArray(R.array.textColor);
        ArrayAdapter<String> color = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, textColor);
        color.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        changeColor.setAdapter(color);

        int fontIndex = 0;
        for (String family: fontFamily){
            if(family.equals(fontPre)){
                break;
            }
            fontIndex++;
        }

        int colorIndex = 0;
        for (String colorValue: textColor){
            if(colorValue.equals(colorPre)){
                break;
            }
            colorIndex++;
        }
        if (fontIndex >= fontFamily.length) fontIndex = 0;
        if (colorIndex >= textColor.length) colorIndex = 0;

        changeFont.setSelection(fontIndex);
        changeColor.setSelection(colorIndex);

        setupListeners();
    }
    private void setupListeners() {
        switchTheme.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(switchTheme.getText() != dark){
                switchTheme.setText(dark);
            }else{
                switchTheme.setText(light);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(intent);
            }
        });

        appInfo.setOnClickListener(v ->
                startActivity(new Intent(this, AboutActivity.class))
        );

        save.setOnClickListener(v -> {
            if(checkBox.isChecked()){
                SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
                preferences.edit().putString("textColor", changeColor.getSelectedItem().toString()).commit();
                preferences.edit().putString("fontFamily", changeFont.getSelectedItem().toString()).commit();
                preferences.edit().putString("themeChoice", switchTheme.getText().toString()).commit();
                Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
                restartApp();
            }else{
                Toast.makeText(this, "Please accept changes", Toast.LENGTH_SHORT).show();
            }
        });

        buttonReset.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
            preferences.edit().clear().apply();
            Toast.makeText(this, "Settings reset", Toast.LENGTH_SHORT).show();
            restartApp();
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void restartApp() {
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Runtime.getRuntime().exit(0);
    }

}