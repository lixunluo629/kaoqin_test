package org.apache.ibatis.javassist.compiler.ast;

import org.apache.ibatis.javassist.compiler.CompileError;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/ast/Variable.class */
public class Variable extends Symbol {
    protected Declarator declarator;

    public Variable(String sym, Declarator d) {
        super(sym);
        this.declarator = d;
    }

    public Declarator getDeclarator() {
        return this.declarator;
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.Symbol, org.apache.ibatis.javassist.compiler.ast.ASTree
    public String toString() {
        return this.identifier + ":" + this.declarator.getType();
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.Symbol, org.apache.ibatis.javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atVariable(this);
    }
}
