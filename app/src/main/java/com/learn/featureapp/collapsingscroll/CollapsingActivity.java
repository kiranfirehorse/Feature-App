package com.learn.featureapp.collapsingscroll;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.learn.featureapp.R;

public class CollapsingActivity extends AppCompatActivity {
    private CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setActionTextColor(ContextCompat
                                .getColor(getApplicationContext(), R.color.gray))
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Replace with your action
                            }
                        })
                        .show();
            }
        });
        Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                applyPaletteColor(palette);
            }
        };

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bmw);
        if (bitmap != null && !bitmap.isRecycled()) {
            Palette.from(bitmap).generate(paletteListener);

        } else {
            Log.d("Collapsing ", "Couldn't set status color");
        }

    }

    private void applyPaletteColor(Palette palette) {
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
        int vibrant = palette.getVibrantColor(colorPrimary);
        int vibrantLight = palette.getLightVibrantColor(colorPrimary);
        int vibrantDark = palette.getDarkVibrantColor(colorPrimary);
        int muted = palette.getMutedColor(colorPrimary);
        int mutedLight = palette.getLightMutedColor(colorPrimary);
        int mutedDark = palette.getDarkMutedColor(colorPrimary);

        toolbarLayout.setContentScrimColor(muted);
        toolbarLayout.setStatusBarScrimColor(mutedDark);
    }
}
