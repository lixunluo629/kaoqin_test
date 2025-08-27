package org.apache.ibatis.javassist.compiler.ast;

import org.apache.ibatis.javassist.compiler.CompileError;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/ast/ArrayInit.class */
public class ArrayInit extends ASTList {
    public ArrayInit(ASTree firstElement) {
        super(firstElement);
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTList, org.apache.ibatis.javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atArrayInit(this);
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTree
    public String getTag() {
        return "array";
    }
}
