package org.apache.xmlbeans.impl.jam.mutable;

import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JField;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/mutable/MField.class */
public interface MField extends JField, MMember {
    void setType(String str);

    void setUnqualifiedType(String str);

    void setType(JClass jClass);
}
