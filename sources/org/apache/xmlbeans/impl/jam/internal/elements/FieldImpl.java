package org.apache.xmlbeans.impl.jam.internal.elements;

import java.io.StringWriter;
import java.lang.reflect.Modifier;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.internal.classrefs.DirectJClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.QualifiedJClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.UnqualifiedJClassRef;
import org.apache.xmlbeans.impl.jam.mutable.MField;
import org.apache.xmlbeans.impl.jam.visitor.JVisitor;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/FieldImpl.class */
public final class FieldImpl extends MemberImpl implements MField {
    private JClassRef mTypeClassRef;

    FieldImpl(String simpleName, ClassImpl containingClass, String qualifiedTypeClassName) {
        super(containingClass);
        super.setSimpleName(simpleName);
        this.mTypeClassRef = QualifiedJClassRef.create(qualifiedTypeClassName, containingClass);
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MField
    public void setType(JClass type) {
        if (type == null) {
            throw new IllegalArgumentException("null type");
        }
        this.mTypeClassRef = DirectJClassRef.create(type);
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MField
    public void setType(String qcname) {
        if (qcname == null) {
            throw new IllegalArgumentException("null qcname");
        }
        this.mTypeClassRef = QualifiedJClassRef.create(qcname, (ClassImpl) getContainingClass());
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MField
    public void setUnqualifiedType(String ucname) {
        if (ucname == null) {
            throw new IllegalArgumentException("null ucname");
        }
        this.mTypeClassRef = UnqualifiedJClassRef.create(ucname, (ClassImpl) getContainingClass());
    }

    @Override // org.apache.xmlbeans.impl.jam.JField
    public JClass getType() {
        if (this.mTypeClassRef == null) {
            throw new IllegalStateException();
        }
        return this.mTypeClassRef.getRefClass();
    }

    @Override // org.apache.xmlbeans.impl.jam.JField
    public boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.JField
    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.JField
    public boolean isVolatile() {
        return Modifier.isVolatile(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.JField
    public boolean isTransient() {
        return Modifier.isTransient(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public void accept(MVisitor visitor) {
        visitor.visit(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public void accept(JVisitor visitor) {
        visitor.visit(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public String getQualifiedName() {
        StringWriter sbuf = new StringWriter();
        sbuf.write(Modifier.toString(getModifiers()));
        sbuf.write(32);
        sbuf.write(getType().getQualifiedName());
        sbuf.write(32);
        sbuf.write(getContainingClass().getQualifiedName());
        sbuf.write(46);
        sbuf.write(getSimpleName());
        return sbuf.toString();
    }
}
