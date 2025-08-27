package org.apache.xmlbeans.impl.jam.mutable;

import org.apache.xmlbeans.impl.jam.JAnnotation;
import org.apache.xmlbeans.impl.jam.JClass;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/mutable/MAnnotation.class */
public interface MAnnotation extends JAnnotation, MElement {
    void setAnnotationInstance(Object obj);

    void setSimpleValue(String str, Object obj, JClass jClass);

    MAnnotation createNestedValue(String str, String str2);

    MAnnotation[] createNestedValueArray(String str, String str2, int i);
}
