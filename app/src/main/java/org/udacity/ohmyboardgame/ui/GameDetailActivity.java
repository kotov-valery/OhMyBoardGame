package org.udacity.ohmyboardgame.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import org.udacity.ohmyboardgame.R;
import org.udacity.ohmyboardgame.data.BoardGame;
import org.udacity.ohmyboardgame.utility.ImageLoader;

public class GameDetailActivity extends AppCompatActivity {
    private static final String TAG = GameDetailActivity.class.getSimpleName();

    public static final String GAME_OBJECT = "game-object";

    private BoardGame game;
    private ThreeTwoImageView preview;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setTitle("");
        toolbar.setTitle("");

        preview = findViewById(R.id.preview_background);
        title = findViewById(R.id.game_title);

        FloatingActionButton fab = findViewById(R.id.add_to_favorites);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(GAME_OBJECT)) {
                String movieJSON = intent.getStringExtra(GAME_OBJECT);
                game = (new Gson()).fromJson(movieJSON, BoardGame.class);
            }
        }

        if (game != null) {
            ImageLoader.fetchImageIntoView(game.thumbnail.value, preview);
            title.setText(game.name.value + "(" + game.publishYear.value + ")");
        }
    }

}
