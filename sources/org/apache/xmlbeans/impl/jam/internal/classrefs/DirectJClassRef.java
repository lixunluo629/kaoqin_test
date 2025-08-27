package org.apache.xmlbeans.impl.jam.internal.classrefs;

import org.apache.xmlbeans.impl.jam.JClass;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/classrefs/DirectJClassRef.class */
public class DirectJClassRef implements JClassRef {
    private JClass mClass;

    public static JClassRef create(JClass clazz) {
        return clazz instanceof JClassRef ? (JClassRef) clazz : new DirectJClassRef(clazz);
    }

    private DirectJClassRef(JClass clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("null clazz");
        }
        this.mClass = clazz;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef
    public JClass getRefClass() {
        return this.mClass;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef
    public String getQualifiedName() {
        return this.mClass.getQualifiedName();
    }
}
