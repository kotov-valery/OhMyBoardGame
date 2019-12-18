package org.udacity.ohmyboardgame.utility;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.udacity.ohmyboardgame.R;

public class ImageLoader {

    public static void fetchImageIntoView(String imageUrl, ImageView view) {
        if (isUrlValid(imageUrl)) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_loading_failed)
                    .into(view);
        }
    }

    private static boolean isUrlValid(String url) {
        return url != null && url != "" &&
                url.contains("http");
    }
}
