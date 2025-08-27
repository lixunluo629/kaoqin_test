package org.apache.xmlbeans.impl.jam.visitor;

import org.apache.xmlbeans.impl.jam.JAnnotatedElement;
import org.apache.xmlbeans.impl.jam.JAnnotation;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JComment;
import org.apache.xmlbeans.impl.jam.JConstructor;
import org.apache.xmlbeans.impl.jam.JField;
import org.apache.xmlbeans.impl.jam.JInvokable;
import org.apache.xmlbeans.impl.jam.JMethod;
import org.apache.xmlbeans.impl.jam.JPackage;
import org.apache.xmlbeans.impl.jam.JParameter;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/visitor/TraversingJVisitor.class */
public class TraversingJVisitor extends JVisitor {
    private JVisitor mDelegate;

    public TraversingJVisitor(JVisitor jv) {
        if (jv == null) {
            throw new IllegalArgumentException("null jv");
        }
        this.mDelegate = jv;
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.JVisitor
    public void visit(JPackage pkg) {
        pkg.accept(this.mDelegate);
        JClass[] c = pkg.getClasses();
        for (JClass jClass : c) {
            visit(jClass);
        }
        visitAnnotations(pkg);
        visitComment(pkg);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.JVisitor
    public void visit(JClass clazz) {
        clazz.accept(this.mDelegate);
        JField[] f = clazz.getDeclaredFields();
        for (JField jField : f) {
            visit(jField);
        }
        JConstructor[] c = clazz.getConstructors();
        for (JConstructor jConstructor : c) {
            visit(jConstructor);
        }
        JMethod[] m = clazz.getMethods();
        for (JMethod jMethod : m) {
            visit(jMethod);
        }
        visitAnnotations(clazz);
        visitComment(clazz);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.JVisitor
    public void visit(JField field) {
        field.accept(this.mDelegate);
        visitAnnotations(field);
        visitComment(field);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.JVisitor
    public void visit(JConstructor ctor) {
        ctor.accept(this.mDelegate);
        visitParameters(ctor);
        visitAnnotations(ctor);
        visitComment(ctor);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.JVisitor
    public void visit(JMethod method) {
        method.accept(this.mDelegate);
        visitParameters(method);
        visitAnnotations(method);
        visitComment(method);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.JVisitor
    public void visit(JParameter param) {
        param.accept(this.mDelegate);
        visitAnnotations(param);
        visitComment(param);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.JVisitor
    public void visit(JAnnotation ann) {
        ann.accept(this.mDelegate);
    }

    @Override // org.apache.xmlbeans.impl.jam.visitor.JVisitor
    public void visit(JComment comment) {
        comment.accept(this.mDelegate);
    }

    private void visitParameters(JInvokable iv) {
        JParameter[] p = iv.getParameters();
        for (JParameter jParameter : p) {
            visit(jParameter);
        }
    }

    private void visitAnnotations(JAnnotatedElement ae) {
        JAnnotation[] anns = ae.getAnnotations();
        for (JAnnotation jAnnotation : anns) {
            visit(jAnnotation);
        }
    }

    private void visitComment(JAnnotatedElement e) {
        JComment c = e.getComment();
        if (c != null) {
            visit(c);
        }
    }
}
