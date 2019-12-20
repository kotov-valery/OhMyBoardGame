package org.udacity.ohmyboardgame.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.udacity.ohmyboardgame.R;
import org.udacity.ohmyboardgame.backend.BoardGameGeek;
import org.udacity.ohmyboardgame.data.BoardGame;
import org.udacity.ohmyboardgame.data.BoardGames;
import org.udacity.ohmyboardgame.data.GameDetails;
import org.udacity.ohmyboardgame.data.Name;
import org.udacity.ohmyboardgame.data.QueryResult;
import org.udacity.ohmyboardgame.data.QueryResults;
import org.udacity.ohmyboardgame.data.Thumbnail;
import org.udacity.ohmyboardgame.model.ArticleListModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView boardGamesList;
    private GamesViewAdapter adapter;
    private ArticleListModel articleListModel;
    private QueriedGameFoundListener queriedGameFoundListener;

    private static final int HIGH_RESOLUTION_POSTER_PATH_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queriedGameFoundListener = new QueriedGameFoundListener();

        FloatingActionButton fab = findViewById(R.id.search_for_a_game);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
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

    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_search, null);
        final EditText inputQuery = dialogView.findViewById(R.id.board_game_to_find);

        builder.setView(dialogView)
                .setPositiveButton(R.string.find_a_game_search, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String searchQuery = inputQuery.getText().toString();
                        BoardGameGeek.findByQuery(searchQuery, queriedGameFoundListener);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.find_a_game_canel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.show();
    }

    private class GameClickedListener implements GamesViewAdapter.OnGameClickListener {
        @Override
        public void onClick(BoardGame game) {
            Intent gameDetailsIntent = new Intent(MainActivity.this, GameDetailActivity.class);
            gameDetailsIntent.putExtra(GameDetailActivity.GAME_OBJECT, (new Gson()).toJson(game));
            startActivity(gameDetailsIntent);
        }
    }

    private class QueriedGameFoundListener implements BoardGameGeek.GameFoundListener,
            BoardGameGeek.GameDetailsPublisher
    {
        @Override
        public void onGameFound(QueryResults results) {
            if (results.list.size() > 0) {
                QueryResult topResult = results.list.get(0);
                BoardGame game = new BoardGame();
                game.id = topResult.id;
                BoardGameGeek.fetchGameDetails(game, this);
            }
        }

        @Override
        public void publishGameDetails(GameDetails details) {
            BoardGame game = new BoardGame();
            game.id = details.id;
            game.name = new Name();
            game.name.value = details.names.get(0).value;
            game.publishYear = details.yearPublished;
            game.thumbnail = new Thumbnail();
            game.thumbnail.value = details.image;

            Intent gameDetailsIntent = new Intent(MainActivity.this, GameDetailActivity.class);
            gameDetailsIntent.putExtra(GameDetailActivity.GAME_OBJECT, (new Gson()).toJson(game));
            startActivity(gameDetailsIntent);
        }
    }

    public void fetchHightResolutionImages(final BoardGames games) {
        getSupportLoaderManager().initLoader(HIGH_RESOLUTION_POSTER_PATH_LOADER, null,
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
