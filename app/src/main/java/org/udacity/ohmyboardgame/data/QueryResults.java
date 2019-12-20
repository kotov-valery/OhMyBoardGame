package org.udacity.ohmyboardgame.data;

import java.util.List;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.ElementList;


@Root(name="items", strict = false)
public class QueryResults {
    @ElementList(inline = true)
    public List<QueryResult> list;
}
