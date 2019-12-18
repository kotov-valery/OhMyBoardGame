package org.udacity.ohmyboardgame.data;

/*
 * <item id="295770" rank="1">
 * <thumbnail value="https://cf.geekdo-images.com/thumb/img/P1hbRu9Lc3dMVS9xbLx7Z3zovEE=/fit-in/200x150/pic5092291.png"/>
 * <name value="Frosthaven"/>
 * <yearpublished value="2021"/>
 * </item>
 */

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="item")
public class BoardGame {
    @Attribute
    public int id;

    @Attribute
    public int rank;

    @Element
    public Thumbnail thumbnail;

    @Element
    public Name name;

    @Element(name = "yearpublished")
    public YearPublished publishYear;
}
