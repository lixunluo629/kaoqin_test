package org.apache.xmlbeans.impl.jam.visitor;

import org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotation;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.mutable.MComment;
import org.apache.xmlbeans.impl.jam.mutable.MConstructor;
import org.apache.xmlbeans.impl.jam.mutable.MField;
import org.apache.xmlbeans.impl.jam.mutable.MInvokable;
import org.apache.xmlbeans.impl.jam.mutable.MMethod;
import org.apache.xmlbeans.impl.jam.mutable.MPackage;
import org.apache.xmlbeans.impl.jam.mutable.MParameter;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/visitor/TraversingMVisitor.class */
public class TraversingMVisitor extends MVisitor {
    private MVisitor mDelegate;

    public TraversingMVisitor(MVisitor jv) {
        if (jv == null) {
            throw new IllegalArgumentException("null jv");
        }
        this.mDelegate = jv;
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.MVisitor
    public void visit(MPackage pkg) {
        pkg.accept(this.mDelegate);
        MClass[] c = pkg.getMutableClasses();
        for (MClass mClass : c) {
            visit(mClass);
        }
        visitAnnotations(pkg);
        visitComment(pkg);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.MVisitor
    public void visit(MClass clazz) {
        clazz.accept(this.mDelegate);
        MField[] f = clazz.getMutableFields();
        for (MField mField : f) {
            visit(mField);
        }
        MConstructor[] c = clazz.getMutableConstructors();
        for (MConstructor mConstructor : c) {
            visit(mConstructor);
        }
        MMethod[] m = clazz.getMutableMethods();
        for (MMethod mMethod : m) {
            visit(mMethod);
        }
        visitAnnotations(clazz);
        visitComment(clazz);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.MVisitor
    public void visit(MField field) {
        field.accept(this.mDelegate);
        visitAnnotations(field);
        visitComment(field);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.MVisitor
    public void visit(MConstructor ctor) {
        ctor.accept(this.mDelegate);
        visitParameters(ctor);
        visitAnnotations(ctor);
        visitComment(ctor);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.MVisitor
    public void visit(MMethod method) {
        method.accept(this.mDelegate);
        visitParameters(method);
        visitAnnotations(method);
        visitComment(method);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.MVisitor
    public void visit(MParameter param) {
        param.accept(this.mDelegate);
        visitAnnotations(param);
        visitComment(param);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.MVisitor
    public void visit(MAnnotation ann) {
        ann.accept(this.mDelegate);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.MVisitor
    public void visit(MComment comment) {
        comment.accept(this.mDelegate);
    }

    private void visitParameters(MInvokable iv) {
        MParameter[] p = iv.getMutableParameters();
        for (MParameter mParameter : p) {
            visit(mParameter);
        }
    }

    private void visitAnnotations(MAnnotatedElement ae) {
        MAnnotation[] anns = ae.getMutableAnnotations();
        for (MAnnotation mAnnotation : anns) {
            visit(mAnnotation);
        }
    }

    private void visitComment(MAnnotatedElement e) {
        MComment c = e.getMutableComment();
        if (c != null) {
            visit(c);
        }
    }
}
