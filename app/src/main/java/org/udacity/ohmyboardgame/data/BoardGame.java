package org.udacity.ohmyboardgame.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class BoardGame {
    @Attribute
    public int id;

    @Element
    public Thumbnail thumbnail;

    public boolean isHighResolution = false;

    @Element
    public Name name;

    @Element(name = "yearpublished")
    public YearPublished publishYear;
}
