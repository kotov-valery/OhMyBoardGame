package org.udacity.ohmyboardgame.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item")
public class BoardGame {
    @Attribute
    public int id;

    @Attribute
    public int rank;

    @Element
    public Thumbnail thumbnail;

    public boolean isHighResolution = false;

    @Element
    public Name name;

    @Element(name = "yearpublished")
    public YearPublished publishYear;
}
