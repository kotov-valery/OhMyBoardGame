package org.udacity.ohmyboardgame.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Name {
    @Attribute
    public String value;
}