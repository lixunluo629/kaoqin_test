package org.apache.ibatis.javassist.compiler.ast;

import org.apache.ibatis.javassist.compiler.CompileError;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/ast/CondExpr.class */
public class CondExpr extends ASTList {
    public CondExpr(ASTree cond, ASTree thenp, ASTree elsep) {
        super(cond, new ASTList(thenp, new ASTList(elsep)));
    }

    public ASTree condExpr() {
        return head();
    }

    public void setCond(ASTree t) {
        setHead(t);
    }

    public ASTree thenExpr() {
        return tail().head();
    }

    public void setThen(ASTree t) {
        tail().setHead(t);
    }

    public ASTree elseExpr() {
        return tail().tail().head();
    }

    public void setElse(ASTree t) {
        tail().tail().setHead(t);
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTree
    public String getTag() {
        return "?:";
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTList, org.apache.ibatis.javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atCondExpr(this);
    }
}
