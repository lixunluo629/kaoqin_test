package org.apache.xmlbeans.impl.jam.internal.elements;

import java.io.StringWriter;
import java.lang.reflect.Modifier;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JParameter;
import org.apache.xmlbeans.impl.jam.internal.classrefs.DirectJClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.QualifiedJClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.UnqualifiedJClassRef;
import org.apache.xmlbeans.impl.jam.mutable.MMethod;
import org.apache.xmlbeans.impl.jam.visitor.JVisitor;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/MethodImpl.class */
public final class MethodImpl extends InvokableImpl implements MMethod {
    private JClassRef mReturnTypeRef;

    MethodImpl(String simpleName, ClassImpl containingClass) {
        super(containingClass);
        this.mReturnTypeRef = null;
        setSimpleName(simpleName);
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MMethod
    public void setReturnType(String className) {
        this.mReturnTypeRef = QualifiedJClassRef.create(className, (ClassImpl) getContainingClass());
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MMethod
    public void setUnqualifiedReturnType(String unqualifiedTypeName) {
        this.mReturnTypeRef = UnqualifiedJClassRef.create(unqualifiedTypeName, (ClassImpl) getContainingClass());
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MMethod
    public void setReturnType(JClass c) {
        this.mReturnTypeRef = DirectJClassRef.create(c);
    }

    @Override // org.apache.xmlbeans.impl.jam.JMethod
    public JClass getReturnType() {
        if (this.mReturnTypeRef == null) {
            return getClassLoader().loadClass("void");
        }
        return this.mReturnTypeRef.getRefClass();
    }

    @Override // org.apache.xmlbeans.impl.jam.JMethod
    public boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.JMethod
    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.JMethod
    public boolean isAbstract() {
        return Modifier.isAbstract(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.JMethod
    public boolean isNative() {
        return Modifier.isNative(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.JMethod
    public boolean isSynchronized() {
        return Modifier.isSynchronized(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public void accept(MVisitor visitor) {
        visitor.visit(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public void accept(JVisitor visitor) {
        visitor.visit(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.InvokableImpl, org.apache.xmlbeans.impl.jam.JElement
    public String getQualifiedName() {
        StringWriter sbuf = new StringWriter();
        sbuf.write(Modifier.toString(getModifiers()));
        sbuf.write(32);
        JClass returnJClass = getReturnType();
        if (returnJClass == null) {
            sbuf.write("void ");
        } else {
            sbuf.write(returnJClass.getQualifiedName());
            sbuf.write(32);
        }
        sbuf.write(getSimpleName());
        sbuf.write(40);
        JParameter[] params = getParameters();
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                sbuf.write(params[i].getType().getQualifiedName());
                if (i < params.length - 1) {
                    sbuf.write(44);
                }
            }
        }
        sbuf.write(41);
        JClass[] thrown = getExceptionTypes();
        if (thrown != null && thrown.length > 0) {
            sbuf.write(" throws ");
            for (int i2 = 0; i2 < thrown.length; i2++) {
                sbuf.write(thrown[i2].getQualifiedName());
                if (i2 < thrown.length - 1) {
                    sbuf.write(44);
                }
            }
        }
        return sbuf.toString();
    }
}
