package org.apache.xmlbeans.impl.jam.internal.elements;

import org.apache.xmlbeans.impl.jam.JClass;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/VoidClassImpl.class */
public final class VoidClassImpl extends BuiltinClassImpl {
    private static final String SIMPLE_NAME = "void";

    public static boolean isVoid(String fd) {
        return fd.equals(SIMPLE_NAME);
    }

    public VoidClassImpl(ElementContext ctx) {
        super(ctx);
        super.reallySetSimpleName(SIMPLE_NAME);
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.BuiltinClassImpl, org.apache.xmlbeans.impl.jam.JClass
    public boolean isVoidType() {
        return true;
    }

    @Override // org.apache.xmlbeans.impl.jam.JClass
    public boolean isAssignableFrom(JClass c) {
        return false;
    }
}
