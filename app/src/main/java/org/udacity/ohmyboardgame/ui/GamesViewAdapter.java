package org.udacity.ohmyboardgame.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.udacity.ohmyboardgame.R;
import org.udacity.ohmyboardgame.data.BoardGame;
import org.udacity.ohmyboardgame.data.BoardGames;
import org.udacity.ohmyboardgame.utility.ImageLoader;

public class GamesViewAdapter extends RecyclerView.Adapter<GamesViewAdapter.GameViewHolder> {
    private static final String TAG = GamesViewAdapter.class.getSimpleName();

    private BoardGames games;
    private OnGameClickListener gameClickListener;

    public void setNewGames(BoardGames newValue) {
        games = newValue;
        notifyDataSetChanged();
    }

    public interface OnGameClickListener {
        void onClick(BoardGame game);
    }

    public GamesViewAdapter(OnGameClickListener listener) {
        gameClickListener = listener;
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
        //Log.d(TAG, "Thumbnail: " + game.thumbnail.value);
        ImageLoader.fetchImageIntoView(game.thumbnail.value, holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return games == null ? 0 : games.list.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        public ImageView thumbnail;

        public GameViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.game_thumbnail);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (gameClickListener != null) {
                gameClickListener.onClick(games.list.get(pos));
            }
        }
    }
}
