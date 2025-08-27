package org.apache.xmlbeans.impl.jam.internal.elements;

import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JPackage;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/UnresolvedClassImpl.class */
public final class UnresolvedClassImpl extends BuiltinClassImpl {
    private String mPackageName;

    public UnresolvedClassImpl(String packageName, String simpleName, ElementContext ctx) {
        super(ctx);
        if (packageName == null) {
            throw new IllegalArgumentException("null pkg");
        }
        this.mPackageName = packageName;
        reallySetSimpleName(simpleName);
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JElement
    public String getQualifiedName() {
        return (this.mPackageName.length() > 0 ? this.mPackageName + '.' : "") + this.mSimpleName;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JClass
    public String getFieldDescriptor() {
        return getQualifiedName();
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JClass
    public JPackage getContainingPackage() {
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isAssignableFrom(JClass c) {
        return false;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JClass
    public boolean isUnresolvedType() {
        return true;
    }
}
