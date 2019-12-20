package org.udacity.ohmyboardgame.model;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class GameDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final int gameId;

    public GameDetailsViewModelFactory(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new GameDetailsViewModel(gameId);
    }
}
