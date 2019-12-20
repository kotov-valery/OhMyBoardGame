package org.udacity.ohmyboardgame.persistency;

import androidx.room.TypeConverter;

import org.udacity.ohmyboardgame.data.Thumbnail;

public class ThumbnailConverter {
    @TypeConverter
    public static String toString(Thumbnail thumbnail) {
        return thumbnail == null ? null : thumbnail.value;
    }

    @TypeConverter
    public static Thumbnail toThumbnail(String value) {
        if (value == null)
            return null;

        Thumbnail thumbnail = new Thumbnail();
        thumbnail.value = value;
        return thumbnail;
    }
}
