package org.apache.xmlbeans.impl.jam.mutable;

import org.apache.xmlbeans.impl.jam.JElement;
import org.apache.xmlbeans.impl.jam.JamClassLoader;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/mutable/MElement.class */
public interface MElement extends JElement {
    JamClassLoader getClassLoader();

    void setSimpleName(String str);

    MSourcePosition createSourcePosition();

    void removeSourcePosition();

    MSourcePosition getMutableSourcePosition();

    void accept(MVisitor mVisitor);

    void setArtifact(Object obj);
}
