package org.apache.xmlbeans.impl.jam;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JField.class */
public interface JField extends JMember {
    JClass getType();

    boolean isFinal();

    boolean isStatic();

    boolean isVolatile();

    boolean isTransient();

    @Override // org.apache.xmlbeans.impl.jam.JElement
    String getQualifiedName();
}
