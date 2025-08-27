package org.apache.xmlbeans.impl.jam;

import org.apache.xmlbeans.impl.jam.visitor.JVisitor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JElement.class */
public interface JElement {
    JElement getParent();

    String getSimpleName();

    String getQualifiedName();

    JSourcePosition getSourcePosition();

    void accept(JVisitor jVisitor);

    Object getArtifact();

    String toString();
}
