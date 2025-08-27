package org.apache.xmlbeans.impl.jam.mutable;

import java.net.URI;
import org.apache.xmlbeans.impl.jam.JSourcePosition;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/mutable/MSourcePosition.class */
public interface MSourcePosition extends JSourcePosition {
    void setColumn(int i);

    void setLine(int i);

    void setSourceURI(URI uri);
}
