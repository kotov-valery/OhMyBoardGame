package org.udacity.ohmyboardgame.backend;

import org.udacity.ohmyboardgame.data.BoardGames;

import retrofit2.Call;
import retrofit2.http.GET;

public class BoardGameGeekService {

    public static final String API_BASE_URL = "https://www.boardgamegeek.com/xmlapi2/";

    public interface BoardGameGeekAPI {
        @GET("hot")
        Call<BoardGames> fetchHotGames();
    }
}
