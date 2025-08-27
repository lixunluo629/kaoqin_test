package org.apache.xmlbeans.impl.jam.internal.classrefs;

import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JamClassLoader;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/classrefs/QualifiedJClassRef.class */
public class QualifiedJClassRef implements JClassRef {
    private String mQualifiedClassname;
    private JamClassLoader mClassLoader;

    public static JClassRef create(JClass clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("null clazz");
        }
        return new QualifiedJClassRef(clazz.getFieldDescriptor(), clazz.getClassLoader());
    }

    public static JClassRef create(String qcname, JClassRefContext ctx) {
        if (qcname == null) {
            throw new IllegalArgumentException("null qcname");
        }
        if (ctx == null) {
            throw new IllegalArgumentException("null ctx");
        }
        return create(qcname, ctx.getClassLoader());
    }

    public static JClassRef create(String qcname, JamClassLoader cl) {
        if (qcname == null) {
            throw new IllegalArgumentException("null qcname");
        }
        if (cl == null) {
            throw new IllegalArgumentException("null classloader");
        }
        return new QualifiedJClassRef(qcname, cl);
    }

    private QualifiedJClassRef(String qcname, JamClassLoader cl) {
        this.mClassLoader = cl;
        this.mQualifiedClassname = qcname;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef
    public JClass getRefClass() {
        return this.mClassLoader.loadClass(this.mQualifiedClassname);
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef
    public String getQualifiedName() {
        return this.mQualifiedClassname;
    }

    public String toString() {
        return "(QualifiedJClassRef '" + this.mQualifiedClassname + "')";
    }
}
