package com.vibedev.imusicplayer;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class SongAdapter extends ArrayAdapter<String> {
    private final int currentIndex;

    public SongAdapter(Context context, List<String> songs, int currentIndex) {
        super(context, android.R.layout.simple_list_item_1, songs);
        this.currentIndex = currentIndex;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = view.findViewById(android.R.id.text1);

        Typeface customTypeface = null;
        String fontPre = BasicActivity.userPreferenceFontFamily;
        String colorPre = BasicActivity.userPreferenceTextColor;

        if (!"Default".equals(fontPre)) {
            try {
                String fontPath = "fonts/" + fontPre.toLowerCase().replace(" ", "_") + ".ttf";
                customTypeface = Typeface.createFromAsset(getContext().getAssets(), fontPath);
                textView.setTypeface(customTypeface);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        if (!"Default".equals(colorPre)) {
            textView.setTextColor(BasicActivity.colorMap.get(colorPre));
        }

        if (position == currentIndex) {
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.currentSong));
        }

        return view;
    }
}
