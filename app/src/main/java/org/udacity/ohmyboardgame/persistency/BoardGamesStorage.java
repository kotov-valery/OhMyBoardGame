package org.udacity.ohmyboardgame.persistency;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.udacity.ohmyboardgame.data.BoardGame;

@Database(entities = {BoardGame.class}, version = 1, exportSchema = false)
public abstract class BoardGamesStorage extends RoomDatabase {
    private static final String TAG = BoardGamesStorage.class.getSimpleName();
    private static BoardGamesStorage sInstance;
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorite-board-games-db";

    public static BoardGamesStorage getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        BoardGamesStorage.class, BoardGamesStorage.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract BoardGameDao boardGameDao();
}
