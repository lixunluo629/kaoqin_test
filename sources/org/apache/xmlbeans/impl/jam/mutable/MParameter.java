package org.apache.xmlbeans.impl.jam.mutable;

import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JParameter;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/mutable/MParameter.class */
public interface MParameter extends JParameter, MMember {
    void setType(String str);

    void setType(JClass jClass);

    void setUnqualifiedType(String str);
}
