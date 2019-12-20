package org.udacity.ohmyboardgame.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Element;

import java.util.List;

@Root(name="item", strict = false)
public class GameDetails {
    @Attribute
    public int id;

    @Element
    public String description;

    @ElementList(inline = true)
    public List<Name> names;

    @Element
    public String image;

    @Element(name = "yearpublished")
    public YearPublished yearPublished;

    @Element
    public Players minplayers;

    @Element
    public Players maxplayers;

    @Element(name = "playingtime")
    public PlayingTime playingTime;

    @Element
    public Statistics statistics;
}
