package org.udacity.ohmyboardgame.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.RemoteViews;

import org.udacity.ohmyboardgame.R;
import org.udacity.ohmyboardgame.data.BoardGame;
import org.udacity.ohmyboardgame.data.GameDetails;
import org.udacity.ohmyboardgame.ui.MainActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Implementation of App Widget functionality.
 */
public class GameDetailsWidget extends AppWidgetProvider {

    static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                 int[] appWidgetIds, BoardGame game) {
        for(int appWidgetId: appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, game);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, BoardGame game) {

        CharSequence widgetText;
        if (game == null) {
            widgetText = context.getString(R.string.appwidget_default_text);
        } else {
            widgetText = game.name.value;
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.game_details_widget);
        views.setTextViewText(R.id.appwidget_game_title, widgetText);

        try{
            if (!TextUtils.isEmpty(game.thumbnail.value)) {
                URL urlURL = new URL(game.thumbnail.value);
                HttpURLConnection con = (HttpURLConnection) urlURL.openConnection();
                InputStream is = con.getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(is);
                views.setImageViewBitmap(R.id.appwidget_preview_background, bmp);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_frame, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                 int[] appWidgetIds, GameDetails details) {
        for(int appWidgetId: appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, details);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, GameDetails details) {

        if (details != null) {
            CharSequence widgetText = details.statistics.ratings.averageRating.value;

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.game_details_widget);
            views.setTextViewText(R.id.appwidget_game_details, widgetText);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, (BoardGame) null);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

