package org.udacity.ohmyboardgame.data;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

@Root
public class Players {
    @Attribute
    public int value;
}
