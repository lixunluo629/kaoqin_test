package org.apache.poi.poifs.dev;

import java.util.Iterator;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/dev/POIFSViewable.class */
public interface POIFSViewable {
    Object[] getViewableArray();

    Iterator<Object> getViewableIterator();

    boolean preferArray();

    String getShortDescription();
}
