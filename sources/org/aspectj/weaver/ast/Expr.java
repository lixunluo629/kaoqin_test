package org.aspectj.weaver.ast;

import org.aspectj.weaver.Member;
import org.aspectj.weaver.ResolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ast/Expr.class */
public abstract class Expr extends ASTNode {
    public static final Expr[] NONE = new Expr[0];

    public abstract void accept(IExprVisitor iExprVisitor);

    public abstract ResolvedType getType();

    public static CallExpr makeCallExpr(Member member, Expr[] exprs, ResolvedType returnType) {
        return new CallExpr(member, exprs, returnType);
    }
}
