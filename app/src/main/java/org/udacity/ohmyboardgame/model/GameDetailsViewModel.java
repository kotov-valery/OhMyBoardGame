package org.udacity.ohmyboardgame.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.udacity.ohmyboardgame.backend.BoardGameGeek;
import org.udacity.ohmyboardgame.data.GameDetails;

public class GameDetailsViewModel extends ViewModel
    implements BoardGameGeek.GameDetailsPublisher
{
    private MutableLiveData<GameDetails> gameDetails = new MutableLiveData<>();

    public GameDetailsViewModel(int gameId) {
        fetchGameDetails(gameId);
    }

    public MutableLiveData<GameDetails> getGameDetails() {
        return gameDetails;
    }

    private void fetchGameDetails(int gameId) {
        BoardGameGeek.fetchGameDetails(gameId, this);
    }

    @Override
    public void publishGameDetails(GameDetails details) {
        gameDetails.setValue(details);
    }
}
