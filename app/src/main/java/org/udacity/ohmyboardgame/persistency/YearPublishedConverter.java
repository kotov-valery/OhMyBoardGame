package org.udacity.ohmyboardgame.persistency;

import androidx.room.TypeConverter;

import org.udacity.ohmyboardgame.data.YearPublished;

public class YearPublishedConverter {
    @TypeConverter
    public static String toString(YearPublished yearPublished) {
        return yearPublished == null ? null : yearPublished.value;
    }

    @TypeConverter
    public static YearPublished toYearPublished(String value) {
        if (value == null)
            return null;

        YearPublished yearPublished = new YearPublished();
        yearPublished.value = value;
        return yearPublished;
    }

}
