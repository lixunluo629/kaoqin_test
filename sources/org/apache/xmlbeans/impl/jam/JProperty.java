package org.apache.xmlbeans.impl.jam;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JProperty.class */
public interface JProperty extends JAnnotatedElement {
    JClass getType();

    JMethod getSetter();

    JMethod getGetter();
}
