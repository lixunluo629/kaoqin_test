package org.apache.xmlbeans.impl.jam.internal.elements;

import java.io.StringWriter;
import java.lang.reflect.Modifier;
import org.apache.xmlbeans.impl.jam.JParameter;
import org.apache.xmlbeans.impl.jam.mutable.MConstructor;
import org.apache.xmlbeans.impl.jam.visitor.JVisitor;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/ConstructorImpl.class */
public final class ConstructorImpl extends InvokableImpl implements MConstructor {
    ConstructorImpl(ClassImpl containingClass) {
        super(containingClass);
        setSimpleName(containingClass.getSimpleName());
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
        return sbuf.toString();
    }
}
