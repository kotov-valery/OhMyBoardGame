package org.udacity.ohmyboardgame.persistency;

import androidx.room.TypeConverter;

import org.udacity.ohmyboardgame.data.Name;

public class NameConverter {
    @TypeConverter
    public static String toString(Name name) {
        return name == null ? null : name.value;
    }

    @TypeConverter
    public static Name toName(String value) {
        if (value == null)
            return null;

        Name name = new Name();
        name.value = value;
        return name;
    }
}
