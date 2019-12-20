package org.udacity.ohmyboardgame.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.udacity.ohmyboardgame.persistency.NameConverter;
import org.udacity.ohmyboardgame.persistency.ThumbnailConverter;
import org.udacity.ohmyboardgame.persistency.YearPublishedConverter;

@Entity(tableName = "board_game")
@Root(name = "item", strict = false)
public class BoardGame {
    @PrimaryKey
    @Attribute
    public int id;

    @TypeConverters(ThumbnailConverter.class)
    @Element
    public Thumbnail thumbnail;

    @ColumnInfo(name = "is_high_resolution")
    public boolean isHighResolution = false;

    @Ignore
    public boolean isFavorite = false;

    @TypeConverters(NameConverter.class)
    @Element
    public Name name;

    @TypeConverters(YearPublishedConverter.class)
    @ColumnInfo(name = "year_published")
    @Element(name = "yearpublished")
    public YearPublished publishYear;
}
