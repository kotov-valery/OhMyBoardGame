package org.udacity.ohmyboardgame.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="items", strict=false)
public class GameDetailsList {
    @ElementList(inline = true)
    public List<GameDetails> list;
}

