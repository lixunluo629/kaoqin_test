package org.apache.xmlbeans.impl.jam;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JMethod.class */
public interface JMethod extends JInvokable {
    JClass getReturnType();

    boolean isFinal();

    boolean isStatic();

    boolean isAbstract();

    boolean isNative();

    boolean isSynchronized();

    @Override // org.apache.xmlbeans.impl.jam.JElement
    String getQualifiedName();
}
