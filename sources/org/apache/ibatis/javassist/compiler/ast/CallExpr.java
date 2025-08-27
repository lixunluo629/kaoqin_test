package org.apache.ibatis.javassist.compiler.ast;

import org.apache.ibatis.javassist.compiler.CompileError;
import org.apache.ibatis.javassist.compiler.MemberResolver;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/ast/CallExpr.class */
public class CallExpr extends Expr {
    private MemberResolver.Method method;

    private CallExpr(ASTree _head, ASTList _tail) {
        super(67, _head, _tail);
        this.method = null;
    }

    public void setMethod(MemberResolver.Method m) {
        this.method = m;
    }

    public MemberResolver.Method getMethod() {
        return this.method;
    }

    public static CallExpr makeCall(ASTree target, ASTree args) {
        return new CallExpr(target, new ASTList(args));
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.Expr, org.apache.ibatis.javassist.compiler.ast.ASTList, org.apache.ibatis.javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atCallExpr(this);
    }
}
