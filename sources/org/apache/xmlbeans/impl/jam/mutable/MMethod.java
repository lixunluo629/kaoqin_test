package org.apache.xmlbeans.impl.jam.mutable;

import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JMethod;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/mutable/MMethod.class */
public interface MMethod extends JMethod, MInvokable {
    void setReturnType(String str);

    void setUnqualifiedReturnType(String str);

    void setReturnType(JClass jClass);
}
