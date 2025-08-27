package org.apache.xmlbeans.impl.jam.internal.elements;

import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.internal.classrefs.DirectJClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.QualifiedJClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.UnqualifiedJClassRef;
import org.apache.xmlbeans.impl.jam.mutable.MParameter;
import org.apache.xmlbeans.impl.jam.visitor.JVisitor;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/ParameterImpl.class */
public class ParameterImpl extends MemberImpl implements MParameter {
    private JClassRef mTypeClassRef;

    ParameterImpl(String simpleName, InvokableImpl containingMember, String typeName) {
        super(containingMember);
        setSimpleName(simpleName);
        setType(typeName);
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public String getQualifiedName() {
        return getSimpleName();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MParameter
    public void setType(String qcname) {
        if (qcname == null) {
            throw new IllegalArgumentException("null typename");
        }
        this.mTypeClassRef = QualifiedJClassRef.create(qcname, (ClassImpl) getContainingClass());
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MParameter
    public void setType(JClass qcname) {
        if (qcname == null) {
            throw new IllegalArgumentException("null qcname");
        }
        this.mTypeClassRef = DirectJClassRef.create(qcname);
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MParameter
    public void setUnqualifiedType(String ucname) {
        if (ucname == null) {
            throw new IllegalArgumentException("null ucname");
        }
        this.mTypeClassRef = UnqualifiedJClassRef.create(ucname, (ClassImpl) getContainingClass());
    }

    @Override // org.apache.xmlbeans.impl.jam.JParameter
    public JClass getType() {
        return this.mTypeClassRef.getRefClass();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public void accept(MVisitor visitor) {
        visitor.visit(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public void accept(JVisitor visitor) {
        visitor.visit(this);
    }
}
