package org.udacity.ohmyboardgame.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.udacity.ohmyboardgame.R;
import org.udacity.ohmyboardgame.data.BoardGame;
import org.udacity.ohmyboardgame.data.BoardGames;

public class GamesViewAdapter extends RecyclerView.Adapter<GamesViewAdapter.GameViewHolder> {

    private BoardGames games;

    public void setNewGames(BoardGames newValue) {
        games = newValue;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_view_item, parent, false);

        GameViewHolder vh = new GameViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        BoardGame game = games.list.get(position);
        holder.name.setText(game.name.value);
    }

    @Override
    public int getItemCount() {
        return games == null ? 0 : games.list.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public GameViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.game_name);
        }

    }
}
