package org.udacity.ohmyboardgame.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;

import org.udacity.ohmyboardgame.R;
import org.udacity.ohmyboardgame.backend.BoardGameGeek;
import org.udacity.ohmyboardgame.data.BoardGame;

public class MainActivity extends AppCompatActivity {

    private RecyclerView boardGamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GamesViewAdapter adapter = new GamesViewAdapter(new GameClickedListener());

        boardGamesList = findViewById(R.id.board_games_list_view);
        boardGamesList.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.game_list_columns_count));
        boardGamesList.setLayoutManager(layoutManager);

        BoardGameGeek.fetchHotGames(adapter);
    }

    private class GameClickedListener implements GamesViewAdapter.OnGameClickListener {
        @Override
        public void onClick(BoardGame game) {
            Intent gameDetailsIntent = new Intent(MainActivity.this, GameDetailActivity.class);
            gameDetailsIntent.putExtra(GameDetailActivity.GAME_OBJECT, (new Gson()).toJson(game));
            startActivity(gameDetailsIntent);
        }
    }
}
