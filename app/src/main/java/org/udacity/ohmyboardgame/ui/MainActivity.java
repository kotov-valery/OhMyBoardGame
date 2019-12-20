package org.udacity.ohmyboardgame.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.udacity.ohmyboardgame.R;
import org.udacity.ohmyboardgame.appwidget.UpdateGameDetailsWidgetService;
import org.udacity.ohmyboardgame.backend.BoardGameGeek;
import org.udacity.ohmyboardgame.data.BoardGame;
import org.udacity.ohmyboardgame.data.BoardGames;
import org.udacity.ohmyboardgame.data.GameDetails;
import org.udacity.ohmyboardgame.data.Name;
import org.udacity.ohmyboardgame.data.QueryResult;
import org.udacity.ohmyboardgame.data.QueryResults;
import org.udacity.ohmyboardgame.data.Thumbnail;
import org.udacity.ohmyboardgame.model.ArticleListModel;
import org.udacity.ohmyboardgame.persistency.BoardGamesStorage;
import org.udacity.ohmyboardgame.utility.AppExecutors;
import org.udacity.ohmyboardgame.utility.JSON;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String FILTER_CATEGORY = "filter-category";
    private int filterCategory;

    private static final int FILTER_BY_WHAT_IS_HOT = 0;
    private static final int FILTER_BY_MY_FAVORITES = 1;

    private RecyclerView boardGamesList;
    private GamesViewAdapter adapter;
    private ArticleListModel articleListModel;
    private QueriedGameFoundListener queriedGameFoundListener;
    BoardGamesStorage storage;

    private static final int HIGH_RESOLUTION_POSTER_PATH_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queriedGameFoundListener = new QueriedGameFoundListener();

        storage = BoardGamesStorage.getInstance(getApplicationContext());

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

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        filterCategory = preferences.getInt(FILTER_CATEGORY, FILTER_BY_WHAT_IS_HOT);

        updateUI();
    }

    private void updateUI() {
        if (filterCategory == FILTER_BY_WHAT_IS_HOT) {
            articleListModel = ViewModelProviders.of(this).get(ArticleListModel.class);
            articleListModel.getGames().observe(this, new Observer<BoardGames>() {
                @Override
                public void onChanged(BoardGames boardGames) {
                    adapter.setNewGames(boardGames);
                    //fetchHighResolutionImages(boardGames);
                }
            });
        } else if (filterCategory == FILTER_BY_MY_FAVORITES) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final LiveData<List<BoardGame>> games = storage.boardGameDao().getAll();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            games.observe(MainActivity.this, new Observer<List<BoardGame>>() {
                                @Override
                                public void onChanged(List<BoardGame> boardGames) {
                                    BoardGames games = new BoardGames();
                                    games.list = boardGames;
                                    adapter.setNewGames(games);
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void createDialog() {
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
            UpdateGameDetailsWidgetService.startActionUpdateGameInfo(getApplicationContext(), game);

            Intent gameDetailsIntent = new Intent(MainActivity.this, GameDetailActivity.class);
            gameDetailsIntent.putExtra(GameDetailActivity.GAME_OBJECT, JSON.toJson(game));
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
                BoardGameGeek.fetchGameDetails(topResult.id, this);
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
                    BoardGameGeek.fetchGameDetails(game.id, this);
                }
            }
            return null;
        }

        @Override
        public void publishGameDetails(GameDetails details) {
            for (BoardGame game: games.list) {
                if (game.id == details.id) {
                    game.image = details.image;
                    game.isHighResolution = true;
                    break;
                }
            }
            model.getGames().setValue(games);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem activeItem;
        if (filterCategory == FILTER_BY_MY_FAVORITES) {
            activeItem = menu.findItem(R.id.show_my_favorites);
        } else {
            activeItem = menu.findItem(R.id.show_whats_hot);
        }
        activeItem.setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.show_whats_hot && !item.isChecked()) {
            item.setChecked(true);
            saveUserPreferences(FILTER_BY_WHAT_IS_HOT);
            updateUI();
            return true;
        } else if (id == R.id.show_my_favorites && !item.isChecked()) {
            item.setChecked(true);
            saveUserPreferences(FILTER_BY_MY_FAVORITES);
            updateUI();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveUserPreferences(int filterCategory) {
        this.filterCategory = filterCategory;
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(FILTER_CATEGORY, filterCategory);
        editor.commit();
    }
}
