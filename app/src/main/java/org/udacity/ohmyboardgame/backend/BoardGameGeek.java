package org.udacity.ohmyboardgame.backend;

import android.util.Log;

import org.udacity.ohmyboardgame.data.BoardGame;
import org.udacity.ohmyboardgame.data.BoardGames;
import org.udacity.ohmyboardgame.ui.GamesViewAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class BoardGameGeek {
    private static final String TAG = BoardGameGeek.class.getSimpleName();

    private GamesViewAdapter gamesViewAdapter;

    public BoardGameGeek(GamesViewAdapter adapter) {
        gamesViewAdapter = adapter;
    }

    public void fetchHotGames() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BoardGameGeekService.API_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        BoardGameGeekService.BoardGameGeekAPI service =
                retrofit.create(BoardGameGeekService.BoardGameGeekAPI.class);

        Call<BoardGames> call = service.fetchHotGames();
        call.enqueue(new Callback<BoardGames>() {
            @Override
            public void onResponse(Call<BoardGames> call, Response<BoardGames> response) {
                try {
                    BoardGames games = response.body();
                    //Log.i(TAG, "Got response: " + games.list);
                    gamesViewAdapter.setNewGames(games);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BoardGames> call, Throwable t) {
                Log.e(TAG, "Failed to request movie db API: " + t.getMessage());
                t.getStackTrace();
            }
        });
    }
}
