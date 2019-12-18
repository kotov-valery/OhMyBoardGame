package org.udacity.ohmyboardgame.data;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Element;

@Root(name="item", strict = false)
public class GameDetails {
    @Element
    public String description;

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
