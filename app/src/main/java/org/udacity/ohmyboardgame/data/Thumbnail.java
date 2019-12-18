package org.udacity.ohmyboardgame.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class Thumbnail {
    @Attribute
    public String value;
}
