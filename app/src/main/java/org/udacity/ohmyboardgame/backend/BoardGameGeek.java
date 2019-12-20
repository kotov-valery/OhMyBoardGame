package org.udacity.ohmyboardgame.backend;

import android.util.Log;

import org.udacity.ohmyboardgame.data.BoardGame;
import org.udacity.ohmyboardgame.data.BoardGames;
import org.udacity.ohmyboardgame.data.GameDetails;
import org.udacity.ohmyboardgame.data.GameDetailsList;
import org.udacity.ohmyboardgame.data.QueryResult;
import org.udacity.ohmyboardgame.data.QueryResults;
import org.udacity.ohmyboardgame.ui.GamesViewAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class BoardGameGeek {
    private static final String TAG = BoardGameGeek.class.getSimpleName();

    public interface GameDetailsPublisher {
        void publishGameDetails(GameDetails details);
    }

    public interface GameListLoadedListener {
        void onLoadingCompleted(BoardGames games);
    }

    public interface GameFoundListener {
        void onGameFound(QueryResults results);
    }

    public static void fetchHotGames(final GameListLoadedListener listener) {
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
                    listener.onLoadingCompleted(games);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BoardGames> call, Throwable t) {
                Log.e(TAG, "Failed to fetch board games list: " + t.getMessage());
                t.getStackTrace();
            }
        });
    }

    public static void fetchGameDetails(BoardGame game, final GameDetailsPublisher publisher) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BoardGameGeekService.API_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        BoardGameGeekService.BoardGameGeekAPI service =
                retrofit.create(BoardGameGeekService.BoardGameGeekAPI.class);

        Call<GameDetailsList> call = service.fetchGameDetails(
                game.id, BoardGameGeekService.QUERY_STATS);
        call.enqueue(new Callback<GameDetailsList>() {
            @Override
            public void onResponse(Call<GameDetailsList> call, Response<GameDetailsList> response) {
                try {
                    GameDetailsList list = response.body();
                    if (list != null && list.list != null &&
                            list.list.size() > 0) {
                        publisher.publishGameDetails(list.list.get(0));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GameDetailsList> call, Throwable t) {
                Log.e(TAG, "Failed to get game details: " + t.getMessage());
                t.getStackTrace();
            }
        });
    }


    public static void findByQuery(String query, final GameFoundListener listener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BoardGameGeekService.API_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        BoardGameGeekService.BoardGameGeekAPI service =
                retrofit.create(BoardGameGeekService.BoardGameGeekAPI.class);

        Call<QueryResults> call = service.findByQuery(query);
        call.enqueue(new Callback<QueryResults>() {
            @Override
            public void onResponse(Call<QueryResults> call, Response<QueryResults> response) {
                try {
                    QueryResults results = response.body();
                    listener.onGameFound(results);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<QueryResults> call, Throwable t) {
                Log.e(TAG, "Failed to query a game: " + t.getMessage());
                t.getStackTrace();
            }
        });
    }
}
