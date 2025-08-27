package org.apache.ibatis.javassist.compiler.ast;

import org.apache.ibatis.javassist.compiler.CompileError;
import org.apache.ibatis.javassist.compiler.TokenId;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/ast/CastExpr.class */
public class CastExpr extends ASTList implements TokenId {
    protected int castType;
    protected int arrayDim;

    public CastExpr(ASTList className, int dim, ASTree expr) {
        super(className, new ASTList(expr));
        this.castType = 307;
        this.arrayDim = dim;
    }

    public CastExpr(int type, int dim, ASTree expr) {
        super(null, new ASTList(expr));
        this.castType = type;
        this.arrayDim = dim;
    }

    public int getType() {
        return this.castType;
    }

    public int getArrayDim() {
        return this.arrayDim;
    }

    public ASTList getClassName() {
        return (ASTList) getLeft();
    }

    public ASTree getOprand() {
        return getRight().getLeft();
    }

    public void setOprand(ASTree t) {
        getRight().setLeft(t);
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTree
    public String getTag() {
        return "cast:" + this.castType + ":" + this.arrayDim;
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTList, org.apache.ibatis.javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atCastExpr(this);
    }
}
