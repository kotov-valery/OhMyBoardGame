package org.udacity.ohmyboardgame.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Statistics {
    @Element
    public Ratings ratings;
}
