package org.udacity.ohmyboardgame.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.udacity.ohmyboardgame.R;
import org.udacity.ohmyboardgame.backend.BoardGameGeek;
import org.udacity.ohmyboardgame.data.BoardGame;
import org.udacity.ohmyboardgame.data.BoardGames;
import org.udacity.ohmyboardgame.data.GameDetails;
import org.udacity.ohmyboardgame.model.ArticleListModel;

public class MainActivity extends AppCompatActivity {

    private RecyclerView boardGamesList;
    private GamesViewAdapter adapter;
    private ArticleListModel articleListModel;

    private static final int HIGHT_RESOLUTION_POSTER_PATH_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.search_for_a_game);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        adapter = new GamesViewAdapter(new GameClickedListener());

        boardGamesList = findViewById(R.id.board_games_list_view);
        boardGamesList.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.game_list_columns_count));
        boardGamesList.setLayoutManager(layoutManager);

        articleListModel = ViewModelProviders.of(this).get(ArticleListModel.class);
        articleListModel.getGames().observe(this, new Observer<BoardGames>() {
            @Override
            public void onChanged(BoardGames boardGames) {
                adapter.setNewGames(boardGames);
                fetchHightResolutionImages(boardGames);
            }
        });
    }

    private class GameClickedListener implements GamesViewAdapter.OnGameClickListener {
        @Override
        public void onClick(BoardGame game) {
            Intent gameDetailsIntent = new Intent(MainActivity.this, GameDetailActivity.class);
            gameDetailsIntent.putExtra(GameDetailActivity.GAME_OBJECT, (new Gson()).toJson(game));
            startActivity(gameDetailsIntent);
        }
    }

    public void fetchHightResolutionImages(final BoardGames games) {
        getSupportLoaderManager().initLoader(HIGHT_RESOLUTION_POSTER_PATH_LOADER, null,
                new LoaderManager.LoaderCallbacks<Void>() {
                    @Override
                    public Loader<Void> onCreateLoader(int id, Bundle args) {
                        return new HighResolutionPosterPathLoader(getApplicationContext(), games, articleListModel);
                    }

                    @Override
                    public void onLoadFinished(@NonNull Loader<Void> loader, Void data) {
                    }

                    @Override
                    public void onLoaderReset(Loader<Void> loader) {
                    }
                }).forceLoad();
    }

    private static class HighResolutionPosterPathLoader extends AsyncTaskLoader<Void>
            implements BoardGameGeek.GameDetailsPublisher
    {
        private BoardGames games;
        private ArticleListModel model;

        public HighResolutionPosterPathLoader(Context context, BoardGames games, ArticleListModel model) {
            super(context);
            this.games = games;
            this.model = model;
        }

        @Nullable
        @Override
        public Void loadInBackground() {
            for (BoardGame game: games.list) {
                if (!game.isHighResolution) {
                    BoardGameGeek.fetchGameDetails(game, this);
                }
            }
            return null;
        }

        @Override
        public void publishGameDetails(GameDetails details) {
            for (BoardGame game: games.list) {
                if (game.id == details.id) {
                    game.thumbnail.value = details.image;
                    game.isHighResolution = true;
                    break;
                }
            }
            model.getGames().setValue(games);
        }
    }
}
