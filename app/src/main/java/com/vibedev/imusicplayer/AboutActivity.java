package com.vibedev.imusicplayer;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.BuildConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AboutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ConstraintLayout layout;
    private ArrayList<TextView> texts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        BasicActivity.applyWindowInsets(findViewById(R.id.about));

        layout = findViewById(R.id.about);
        toolbar = findViewById(R.id.aboutToolbar);
        setSupportActionBar(toolbar);

        texts.add(findViewById(R.id.appname));
        texts.add(findViewById(R.id.appversion));
        texts.add(findViewById(R.id.aboutapp));
        texts.add(findViewById(R.id.developer));
        texts.add(findViewById(R.id.greeting));
        texts.add(findViewById(R.id.gfoot));
        texts.add(findViewById(R.id.lastUpdate));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("About");
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String Version = "V " + BuildConfig.VERSION_NAME;
        texts.get(1).setText(Version);
        try {
            Date date = inputFormat.parse("2025-05-05 17:03:66");
            assert date != null;
            String formattedDate = "last update: " + outputFormat.format(date);
            texts.get(6).setText(formattedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
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
            } else {
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
            }
        } else if (themePre.equals("Dark")) {
            layout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_toolbar));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_toolbar));
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.dark_toolbar));
            for (TextView text: texts) {
                text.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
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