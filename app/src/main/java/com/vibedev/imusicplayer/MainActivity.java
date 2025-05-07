package com.vibedev.imusicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> songPickerLauncher;

    private final ArrayList<Button> buttons = new ArrayList<>();
    private SeekBar seekBar;
    private ConstraintLayout layout;
    private TextView currentSongName, footer;

    private ArrayList<File> songs;
    private ArrayList<String> songName;
    private MediaPlayer mediaPlayer;
    private int currentIndex = 0;

    private final Handler handler = new Handler();
    private Runnable updateSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        BasicActivity.applyWindowInsets(findViewById(R.id.main));

        songPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String path = result.getData().getStringExtra("selected_song");
                        if (path != null) {
                            for (int i = 0; i < songs.size(); i++) {
                                if (songs.get(i).getAbsolutePath().equals(path)) {
                                    currentIndex = i;
                                    playSong(currentIndex);
                                    break;
                                }
                            }
                        }
                    }
                });

        layout = findViewById(R.id.main);
        ImageView image = findViewById(R.id.imageView);
        footer = findViewById(R.id.footer);
        buttons.add(findViewById(R.id.playpause));
        buttons.add(findViewById(R.id.prev));
        buttons.add(findViewById(R.id.next));
        Toolbar toolbar = findViewById(R.id.toolbar);
        seekBar = findViewById(R.id.musicbar);

        setSupportActionBar(toolbar);

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
                image.setImageResource(R.drawable.musicicon);
                for (Button button : buttons) {
                    button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
                }
            } else {
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
                image.setImageResource(R.drawable.musiciicon);
            }
        } else if (themePre.equals("Dark")) {
            layout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_toolbar));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_toolbar));
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.dark_toolbar));
            image.setImageResource(R.drawable.musicicon);
            screenThemeformat(ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.black));

        } else {
            layout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
            image.setImageResource(R.drawable.musiciicon);
            screenThemeformat(ContextCompat.getColor(this, R.color.dark_toolbar), ContextCompat.getColor(this, R.color.white));
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
            if (customTypeface != null) {
                currentSongName.setTypeface(customTypeface);
                footer.setTypeface(customTypeface);
            }
            if (!colorPre.equals("Default")) {
                currentSongName.setTextColor(BasicActivity.colorMap.get(colorPre));
                footer.setTextColor(BasicActivity.colorMap.get(colorPre));
            }
        }

        SongsList.loadSongs(this);
        songs = SongsList.songLists;
        songName = SongsList.songNames;
        SharedPreferences prefs = getSharedPreferences("player_prefs", MODE_PRIVATE);
        currentIndex = prefs.getInt("last_index", 0);

        setupListeners();
        if (!songs.isEmpty()) {
            playSong(currentIndex);
        }
    }

    private void setupListeners() {
        buttons.get(0).setOnClickListener(v -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    buttons.get(0).setText(R.string.play);
                } else {
                    mediaPlayer.start();
                    buttons.get(0).setText(R.string.pause);
                }
            }
        });

        buttons.get(1).setOnClickListener(v -> {
            if (songs.isEmpty()) return;
            currentIndex = (currentIndex - 1 + songs.size()) % songs.size();
            playSong(currentIndex);
        });

        buttons.get(2).setOnClickListener(v -> {
            if (songs.isEmpty()) return;
            currentIndex = (currentIndex + 1) % songs.size();
            playSong(currentIndex);
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void playSong(int index) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            handler.removeCallbacks(updateSeek);
        }
        currentSongName = findViewById(R.id.songname);
        mediaPlayer = MediaPlayer.create(this, android.net.Uri.fromFile(songs.get(index)));
        currentSongName.setText(songName.get(index));
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        buttons.get(0).setText(R.string.pause);
        updateSeek = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 500);
                }
            }
        };
        handler.post(updateSeek);

        mediaPlayer.setOnCompletionListener(mp -> {
            currentIndex = (currentIndex + 1) % songs.size();
            playSong(currentIndex);
        });

        SharedPreferences.Editor editor = getSharedPreferences("player_prefs", MODE_PRIVATE).edit();
        editor.putInt("last_index", currentIndex);
        editor.apply();
    }
    private void screenThemeformat(int backbroundColor, int textColor){
        currentSongName = findViewById(R.id.songname);
        currentSongName.setTextColor(backbroundColor);
        footer.setTextColor(backbroundColor);
        for (Button button : buttons) {
            button.setBackgroundColor(backbroundColor);
            button.setTextColor(textColor);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        BasicActivity.previous = null;
        BasicActivity.selectedSong = null;
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        handler.removeCallbacks(updateSeek);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_menu) {
            startActivity(new Intent(this, MenuActivity.class));
        } else if (id == R.id.action_list) {
            Intent intent = new Intent(this, ListActivity.class);
            songPickerLauncher.launch(intent);
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
