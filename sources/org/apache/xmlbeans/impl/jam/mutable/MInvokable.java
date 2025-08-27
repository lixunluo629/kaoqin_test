package org.apache.xmlbeans.impl.jam.mutable;

import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JInvokable;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/mutable/MInvokable.class */
public interface MInvokable extends JInvokable, MMember {
    void addException(String str);

    void addException(JClass jClass);

    void removeException(String str);

    void removeException(JClass jClass);

    MParameter addNewParameter();

    void removeParameter(MParameter mParameter);

    MParameter[] getMutableParameters();
}
