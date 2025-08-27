package org.apache.xmlbeans.impl.jam;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JAnnotation.class */
public interface JAnnotation extends JElement {
    public static final String SINGLE_VALUE_NAME = "value";

    @Override // org.apache.xmlbeans.impl.jam.JElement
    String getSimpleName();

    Object getProxy();

    JAnnotationValue[] getValues();

    JAnnotationValue getValue(String str);

    Object getAnnotationInstance();
}
