package org.apache.xmlbeans.impl.jam;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JComment.class */
public interface JComment extends JElement {
    String getText();

    @Override // org.apache.xmlbeans.impl.jam.JElement
    JSourcePosition getSourcePosition();
}
