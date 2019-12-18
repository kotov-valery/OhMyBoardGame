package org.udacity.ohmyboardgame.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="items")
public class BoardGames {
    @ElementList(inline=true)
    public List<BoardGame> list;

    @Attribute(name="termsofuse")
    public String termsOfUse;
}


