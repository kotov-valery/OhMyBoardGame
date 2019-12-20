package org.udacity.ohmyboardgame.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.udacity.ohmyboardgame.R;
import org.udacity.ohmyboardgame.backend.BoardGameGeek;
import org.udacity.ohmyboardgame.data.BoardGame;
import org.udacity.ohmyboardgame.data.GameDetails;
import org.udacity.ohmyboardgame.model.GameDetailsViewModel;
import org.udacity.ohmyboardgame.model.GameDetailsViewModelFactory;
import org.udacity.ohmyboardgame.persistency.BoardGameDao;
import org.udacity.ohmyboardgame.persistency.BoardGamesStorage;
import org.udacity.ohmyboardgame.utility.AppExecutors;
import org.udacity.ohmyboardgame.utility.ImageLoader;

public class GameDetailActivity extends AppCompatActivity {
    private static final String TAG = GameDetailActivity.class.getSimpleName();

    public static final String GAME_OBJECT = "game-object";

    private BoardGame game;
    private GameDetailsViewModel gameDetailsViewModel;

    private ThreeTwoImageView preview;
    private TextView title;
    private TextView description;
    private TextView avgRating;
    private TextView avgComplexity;
    private TextView players;
    private TextView playingTime;
    private FloatingActionButton saveToFavorites;

    private BoardGamesStorage storage;

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
        description = findViewById(R.id.game_description);
        avgRating = findViewById(R.id.game_average_rating);
        avgComplexity = findViewById(R.id.game_average_complexity);
        players = findViewById(R.id.game_players_count);
        playingTime = findViewById(R.id.game_playing_time);

        storage = BoardGamesStorage.getInstance(getApplicationContext());

        saveToFavorites = findViewById(R.id.add_to_favorites);
        saveToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.isFavorite = !game.isFavorite;
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        BoardGameDao boardGameDao = storage.boardGameDao();
                        if (game.isFavorite) {
                            Log.d(TAG, "Adding a game " + game.name + " to favorites");
                            boardGameDao.insertAGame(game);
                        } else {
                            Log.d(TAG, "Removing a game " + game.name + " from favorites");
                            boardGameDao.deleteAGame(game);
                        }
                    }
                });
                updateFavoriteButton();
            }
        });

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(GAME_OBJECT)) {
                game = toBoardGame(intent.getStringExtra(GAME_OBJECT));
            }
        } else if (savedInstanceState.containsKey(GAME_OBJECT)) {
            game = toBoardGame(savedInstanceState.getString(GAME_OBJECT));
        }

        Log.d(TAG, "Do we have an id? " + game.id);
        if (game != null) {
            int gameId = game.id;
            GameDetailsViewModelFactory factory = new GameDetailsViewModelFactory(gameId);
            gameDetailsViewModel = ViewModelProviders.of(this, factory).get(GameDetailsViewModel.class);
            gameDetailsViewModel.getGameDetails().observe(this, new Observer<GameDetails>() {
                @Override
                public void onChanged(GameDetails details) {
                    updateUI(details);
                }
            });

            ImageLoader.fetchImageIntoView(game.thumbnail.value, preview);
            title.setText(game.name.value + "(" + game.publishYear.value + ")");

            updateFavoriteButton();
        }
    }

    private static String toJson(BoardGame game) {
        return (new Gson()).toJson(game);
    }

    private static BoardGame toBoardGame(String json) {
        return (new Gson()).fromJson(json, BoardGame.class);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(GAME_OBJECT, toJson(game));
        super.onSaveInstanceState(outState);
    }

    private void updateFavoriteButton() {
        saveToFavorites.setImageResource(game.isFavorite ?
                R.drawable.ic_favorite_filled : R.drawable.ic_favorite_empty);
    }

    private void updateUI(GameDetails details) {
        if (details.image != null && details.image != "") {
            ImageLoader.fetchImageIntoView(details.image, preview);
        }

        description.setText(details.description);
        if (details.minplayers.value > 0 && details.maxplayers.value > 0) {
            players.setText(details.minplayers.value + " - " + details.maxplayers.value);
        } else {
            players.setText(getString(R.string.players_count_is_not_available));
        }

        if (details.playingTime.value > 0) {
            playingTime.setText(Integer.toString(details.playingTime.value));
        } else {
            playingTime.setText(getString(R.string.playing_time_is_not_available));
        }

        if (details.statistics != null && details.statistics.ratings != null &&
                details.statistics.ratings.averageRating != null &&
                details.statistics.ratings.averageRating.value != null &&
                details.statistics.ratings.averageRating.value.length() >= 4) {
            avgRating.setText(details.statistics.ratings.averageRating.value.substring(0, 3));
        } else {
            avgRating.setText(getString(R.string.game_average_rating_is_not_available));
        }

        if (details.statistics != null && details.statistics.ratings != null &&
                details.statistics.ratings.averageWeight!= null &&
                details.statistics.ratings.averageWeight.value != null &&
                details.statistics.ratings.averageWeight.value.length() >= 4) {
            avgComplexity.setText(details.statistics.ratings.averageWeight.value.substring(0, 3) + "/5");
        } else {
            avgComplexity.setText(getString(R.string.game_average_complexity_is_not_available));
        }
    }
}
