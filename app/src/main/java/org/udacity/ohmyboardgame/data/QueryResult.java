package org.udacity.ohmyboardgame.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class QueryResult {
    @Attribute
    public int id;
}
