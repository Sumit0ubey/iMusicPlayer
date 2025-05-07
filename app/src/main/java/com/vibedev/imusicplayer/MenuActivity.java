package com.vibedev.imusicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ConstraintLayout layout;
    private ArrayList<ImageView> images = new ArrayList<>();
    private ArrayList<Integer> resources_dark = new ArrayList<>();
    private ArrayList<Integer> resources_light = new ArrayList<>();
    private ArrayList<TextView> texts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        BasicActivity.applyWindowInsets(findViewById(R.id.menu));

        layout = findViewById(R.id.menu);
        toolbar = findViewById(R.id.menuToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Menu");
        }

        images.add(findViewById(R.id.homeIcon));
        images.add(findViewById(R.id.localMusicIcon));
        images.add(findViewById(R.id.onlineMusicIcon));
        images.add(findViewById(R.id.downloadIcon));
        images.add(findViewById(R.id.settingIcon));
        images.add(findViewById(R.id.moreAppIcon));

        resources_dark.add(R.drawable.menu_home_dark);
        resources_dark.add(R.drawable.menu_localsong_darktheme);
        resources_dark.add(R.drawable.menu_onlinesong_darktheme);
        resources_dark.add(R.drawable.menu_download_dark);
        resources_dark.add(R.drawable.menu_setting_dark);
        resources_dark.add(R.drawable.menu_moreapp_dark);

        resources_light.add(R.drawable.menu_home_light);
        resources_light.add(R.drawable.menu_localsong_lighttheme);
        resources_light.add(R.drawable.menu_onlinesong_lighttheme);
        resources_light.add(R.drawable.menu_download_light);
        resources_light.add(R.drawable.menu_settings_light);
        resources_light.add(R.drawable.menu_moreapp_light);

        texts.add(findViewById(R.id.home));
        texts.add(findViewById(R.id.local));
        texts.add(findViewById(R.id.online));
        texts.add(findViewById(R.id.download));
        texts.add(findViewById(R.id.setting));
        texts.add(findViewById(R.id.more));

        String themePre = BasicActivity.userPreferenceTheme;
        String fontPre = BasicActivity.userPreferenceFontFamily;
        String colorPre = BasicActivity.userPreferenceTextColor;


        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        int nightModeFlag = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        if (themePre.equals("System")) {
            if (nightModeFlag == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
                toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
                layout.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
                for (int i = 0; i < images.size(); i++) {
                    images.get(i).setImageResource(resources_dark.get(i));
                }
            } else {
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
                for (int i = 0; i < images.size(); i++) {
                    images.get(i).setImageResource(resources_light.get(i));
                }
            }
        } else if (themePre.equals("Dark")) {
            layout.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
            for (int i = 0; i < images.size(); i++) {
                images.get(i).setImageResource(resources_dark.get(i));
            }
            for (TextView text: texts) {
                text.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
            for (int i = 0; i < images.size(); i++) {
                images.get(i).setImageResource(resources_light.get(i));
            }
            for (TextView text: texts) {
                text.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
        }

        if (!fontPre.equals("Default") || !colorPre.equals("Default")) {
            Typeface customTypeface = null;

            if (!fontPre.equals("Default")) {
                try {
                    String fontPath = "fonts/" + fontPre.toLowerCase().replace(" ", "_") + ".ttf";
                    customTypeface = Typeface.createFromAsset(getAssets(), fontPath);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }

            for (TextView text : texts) {
                if (customTypeface != null) {
                    text.setTypeface(customTypeface);
                }
                if (!colorPre.equals("Default")) {
                    text.setTextColor(BasicActivity.colorMap.get(colorPre));
                }
            }
        }

        images.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        images.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ListActivity.class));
            }
        });

        images.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://music.youtube.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }else{
                    Toast.makeText(MenuActivity.this, "No activity found to handle this intent", Toast.LENGTH_SHORT).show();
                }
            }
        });

        images.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://y2mate.nu/en-wl1i/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }else{
                    Toast.makeText(MenuActivity.this, "No activity found to handle this intent", Toast.LENGTH_SHORT).show();
                }
            }
        });

        images.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
            }
        });

        images.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/Sumit0ubey";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }else{
                    Toast.makeText(MenuActivity.this, "No activity found to handle this intent", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (BasicActivity.previous != null && BasicActivity.previous.equals("ListActivity")) {
            String path = BasicActivity.selectedSong;
            if (path != null) {
                Intent result = new Intent();
                result.putExtra("selected_song", path);
                setResult(RESULT_OK, result);
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
