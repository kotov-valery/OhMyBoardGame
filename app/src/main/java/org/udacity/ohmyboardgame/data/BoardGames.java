package org.udacity.ohmyboardgame.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="items", strict = false)
public class BoardGames {
    @ElementList(inline = true)
    public List<BoardGame> list;
}


