package com.vibedev.imusicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        BasicActivity.applyWindowInsets(findViewById(R.id.list));
        BasicActivity.previous = "ListActivity";

        layout = findViewById(R.id.list);
        toolbar = findViewById(R.id.listToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Songs");
        }

        String themePre = BasicActivity.userPreferenceTheme;

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        int nightModeFlag = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;if (themePre.equals("System")) {
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
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        }

        listView = findViewById(R.id.songsList);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SongsList.loadSongs(this);
        ArrayList<File> songs = SongsList.songLists;

        SharedPreferences prefs = getSharedPreferences("player_prefs", MODE_PRIVATE);
        int lastIndex = prefs.getInt("last_index", -1);

        SongAdapter adapter = new SongAdapter(this, SongsList.songNames, lastIndex);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            File selectedSong = songs.get(position);
            BasicActivity.selectedSong = selectedSong.getAbsolutePath();
            Intent result = new Intent();
            result.putExtra("selected_song", selectedSong.getAbsolutePath());
            setResult(RESULT_OK, result);
            finish();
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

}