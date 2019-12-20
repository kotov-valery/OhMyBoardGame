package org.udacity.ohmyboardgame.backend;

import org.udacity.ohmyboardgame.data.BoardGames;
import org.udacity.ohmyboardgame.data.GameDetailsList;
import org.udacity.ohmyboardgame.data.QueryResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class BoardGameGeekService {

    public static final String API_BASE_URL = "https://www.boardgamegeek.com/xmlapi2/";
    public static final String API_QUERY_BY_ID = "id";
    public static final String API_QUERY_STATS = "stats";
    public static final String API_QUERY_BY_NAME = "query";

    public static final int QUERY_STATS = 1;
    public static final int DO_NOT_QUERY_STATS = 0;

    public interface BoardGameGeekAPI {
        @GET("hot")
        Call<BoardGames> fetchHotGames();

        @GET("thing")
        Call<GameDetailsList> fetchGameDetails(
                @Query(API_QUERY_BY_ID) int id,
                @Query(API_QUERY_STATS) int queryStats);

        @GET("search")
        Call<QueryResults> findByQuery(
                @Query(API_QUERY_BY_NAME) String query);
    }
}
