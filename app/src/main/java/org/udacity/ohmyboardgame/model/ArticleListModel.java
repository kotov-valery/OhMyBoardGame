package org.udacity.ohmyboardgame.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.udacity.ohmyboardgame.backend.BoardGameGeek;
import org.udacity.ohmyboardgame.data.BoardGames;

public class ArticleListModel extends ViewModel
    implements BoardGameGeek.GameListLoadedListener
{
    private MutableLiveData<BoardGames> games;

    public MutableLiveData<BoardGames> getGames() {
        if (games == null) {
            games = new MutableLiveData<>();
            fetchGames();
        }
        return games;
    }

    private void fetchGames() {
        BoardGameGeek.fetchHotGames(this);
    }

    @Override
    public void onLoadingCompleted(BoardGames games) {
        this.games.setValue(games);
    }
}
