package org.udacity.ohmyboardgame.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.udacity.ohmyboardgame.R;
import org.udacity.ohmyboardgame.backend.BoardGameGeek;

public class MainActivity extends AppCompatActivity {

    private RecyclerView boardGamesList;
    private BoardGameGeek boardGameGeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GamesViewAdapter adapter = new GamesViewAdapter();

        boardGameGeek = new BoardGameGeek(adapter);

        boardGamesList = findViewById(R.id.board_games_list_view);
        boardGamesList.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        boardGamesList.setLayoutManager(layoutManager);

        boardGameGeek.fetchHotGames();
    }
}
