package com.vibedev.imusicplayer;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

public class SongsList {

    private static final String[] audioExtensions = {
            ".mp3", ".wav", ".m4a", ".aac", ".flac", ".ogg", ".3gp"
    };
    public static ArrayList<File> songLists = new ArrayList<>();
    public static ArrayList<String> songNames = new ArrayList<>();
    public static ArrayList<String> getSongNames(ArrayList<File> files) {
        ArrayList<String> songName = new ArrayList<>();

        for (File f : files) {
            String name = f.getName().toLowerCase();
            for (String ext : audioExtensions) {
                if (name.endsWith(ext)) {
                    songName.add(name.substring(0, name.length() - ext.length()));
                    break;
                }
            }
        }

        return songName;
    }


    public static void loadSongs(Context context) {
        songLists.clear();
        songNames.clear();
        String[] projection = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE};
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Audio.Media.TITLE + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(0);
                String title = cursor.getString(1);
                songLists.add(new File(path));
                songNames.add(title);
            }
            cursor.close();
        }

    }
}
