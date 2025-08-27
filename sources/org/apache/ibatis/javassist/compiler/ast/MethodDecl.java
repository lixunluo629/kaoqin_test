package org.apache.ibatis.javassist.compiler.ast;

import org.apache.ibatis.javassist.compiler.CompileError;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/ast/MethodDecl.class */
public class MethodDecl extends ASTList {
    public static final String initName = "<init>";

    public MethodDecl(ASTree _head, ASTList _tail) {
        super(_head, _tail);
    }

    public boolean isConstructor() {
        Symbol sym = getReturn().getVariable();
        return sym != null && "<init>".equals(sym.get());
    }

    public ASTList getModifiers() {
        return (ASTList) getLeft();
    }

    public Declarator getReturn() {
        return (Declarator) tail().head();
    }

    public ASTList getParams() {
        return (ASTList) sublist(2).head();
    }

    public ASTList getThrows() {
        return (ASTList) sublist(3).head();
    }

    public Stmnt getBody() {
        return (Stmnt) sublist(4).head();
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTList, org.apache.ibatis.javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atMethodDecl(this);
    }
}
