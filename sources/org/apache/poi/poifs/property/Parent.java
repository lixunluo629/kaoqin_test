package org.apache.poi.poifs.property;

import java.io.IOException;
import java.util.Iterator;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/property/Parent.class */
public interface Parent extends Child, Iterable<Property> {
    Iterator<Property> getChildren();

    void addChild(Property property) throws IOException;

    @Override // org.apache.poi.poifs.property.Child
    void setPreviousChild(Child child);

    @Override // org.apache.poi.poifs.property.Child
    void setNextChild(Child child);
}
