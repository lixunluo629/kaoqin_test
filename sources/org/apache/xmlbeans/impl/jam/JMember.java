package org.apache.xmlbeans.impl.jam;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JMember.class */
public interface JMember extends JAnnotatedElement {
    JClass getContainingClass();

    int getModifiers();

    boolean isPackagePrivate();

    boolean isPrivate();

    boolean isProtected();

    boolean isPublic();
}
