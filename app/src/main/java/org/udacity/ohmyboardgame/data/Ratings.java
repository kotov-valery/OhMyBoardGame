package org.udacity.ohmyboardgame.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Ratings {
    @Element(name = "average")
    public AverageRating averageRating;

    @Element(name = "averageweight")
    public AverageWeight averageWeight;
}
