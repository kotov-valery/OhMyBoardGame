package org.udacity.ohmyboardgame.persistency;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import org.udacity.ohmyboardgame.data.BoardGame;

import java.util.List;

@Dao
public interface BoardGameDao {
    @Query("SELECT * FROM board_game")
    LiveData<List<BoardGame>> getAll();

    @Query("SELECT * FROM board_game WHERE id LIKE :id LIMIT 1")
    LiveData<BoardGame> findAGameById(int id);

    @Insert
    void insertAGame(BoardGame game);

    @Delete
    void deleteAGame(BoardGame game);
}
