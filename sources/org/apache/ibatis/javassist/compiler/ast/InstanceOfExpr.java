package org.apache.ibatis.javassist.compiler.ast;

import org.apache.ibatis.javassist.compiler.CompileError;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/ast/InstanceOfExpr.class */
public class InstanceOfExpr extends CastExpr {
    public InstanceOfExpr(ASTList className, int dim, ASTree expr) {
        super(className, dim, expr);
    }

    public InstanceOfExpr(int type, int dim, ASTree expr) {
        super(type, dim, expr);
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.CastExpr, org.apache.ibatis.javassist.compiler.ast.ASTree
    public String getTag() {
        return "instanceof:" + this.castType + ":" + this.arrayDim;
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.CastExpr, org.apache.ibatis.javassist.compiler.ast.ASTList, org.apache.ibatis.javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atInstanceOfExpr(this);
    }
}
