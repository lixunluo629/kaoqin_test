package org.apache.xmlbeans.impl.jam;

import java.net.URI;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JSourcePosition.class */
public interface JSourcePosition {
    int getColumn();

    int getLine();

    URI getSourceURI();
}
