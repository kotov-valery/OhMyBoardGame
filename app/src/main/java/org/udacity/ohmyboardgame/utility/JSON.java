package org.udacity.ohmyboardgame.utility;

import com.google.gson.Gson;

import org.udacity.ohmyboardgame.data.BoardGame;
import org.udacity.ohmyboardgame.data.GameDetails;

public class JSON {
    public static String toJson(BoardGame game) {
        return (new Gson()).toJson(game);
    }

    public static BoardGame toBoardGame(String json) {
        return (new Gson()).fromJson(json, BoardGame.class);
    }

    public static String toJson(GameDetails details) {
        return (new Gson()).toJson(details);
    }

    public static GameDetails toGameDetails(String json) {
        return (new Gson()).fromJson(json, GameDetails.class);
    }
}
