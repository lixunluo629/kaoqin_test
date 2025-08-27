package org.aspectj.weaver.ast;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ast/IExprVisitor.class */
public interface IExprVisitor {
    void visit(Var var);

    void visit(FieldGet fieldGet);

    void visit(CallExpr callExpr);
}
