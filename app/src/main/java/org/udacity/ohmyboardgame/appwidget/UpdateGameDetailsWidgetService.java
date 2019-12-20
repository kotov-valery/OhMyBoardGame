package org.udacity.ohmyboardgame.appwidget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import org.udacity.ohmyboardgame.data.BoardGame;
import org.udacity.ohmyboardgame.data.GameDetails;
import org.udacity.ohmyboardgame.utility.JSON;


public class UpdateGameDetailsWidgetService extends IntentService {
    public static final String BOARD_GAME_OBJECT = "board-game-object";
    public static final String GAME_DETAILS_OBJECT = "game-details-object";
    public static final String ACTION_UPDATE_GAME_INFO
            = "org.udacity.ohmyboardgame.action.update_game_info";
    public static final String ACTION_UPDATE_GAME_DETAILS
            = "org.udacity.ohmyboardgame.action.update_game_details";


    public static void startActionUpdateGameInfo(Context context, BoardGame game) {
        Intent intent = new Intent(context, UpdateGameDetailsWidgetService.class);
        intent.setAction(ACTION_UPDATE_GAME_INFO);
        intent.putExtra(BOARD_GAME_OBJECT, JSON.toJson(game));
        context.startService(intent);
    }

    public static void startActionUpdateGameDetails(Context context, GameDetails details) {
        Intent intent = new Intent(context, UpdateGameDetailsWidgetService.class);
        intent.setAction(ACTION_UPDATE_GAME_DETAILS);
        intent.putExtra(GAME_DETAILS_OBJECT, JSON.toJson(details));
        context.startService(intent);
    }


    public UpdateGameDetailsWidgetService() {
        super("UpdateGameDetailsWidgetService");
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        final String action = intent.getAction();
        if (action.equals(ACTION_UPDATE_GAME_INFO)) {
            BoardGame game = null;
            if (intent.hasExtra(BOARD_GAME_OBJECT)) {
                game = JSON.toBoardGame(intent.getStringExtra(BOARD_GAME_OBJECT));
            }
            handleUpdateBoardGameInfo(game);
        } else if (action.endsWith(ACTION_UPDATE_GAME_DETAILS)) {
            GameDetails details = null;
            if (intent.hasExtra(GAME_DETAILS_OBJECT)) {
                details = JSON.toGameDetails(intent.getStringExtra(GAME_DETAILS_OBJECT));
            }
            handleUpdateGameDetails(details);
        }
    }

    private void handleUpdateBoardGameInfo(BoardGame game) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, GameDetailsWidget.class));
        GameDetailsWidget.updateAppWidgets(this, appWidgetManager, appWidgetIds, game);
    }

    private void handleUpdateGameDetails(GameDetails details) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, GameDetailsWidget.class));
        GameDetailsWidget.updateAppWidgets(this, appWidgetManager, appWidgetIds, details);
    }
}
