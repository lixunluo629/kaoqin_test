package org.apache.ibatis.javassist.compiler.ast;

import org.apache.ibatis.javassist.compiler.CompileError;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/ast/Pair.class */
public class Pair extends ASTree {
    protected ASTree left;
    protected ASTree right;

    public Pair(ASTree _left, ASTree _right) {
        this.left = _left;
        this.right = _right;
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atPair(this);
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTree
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("(<Pair> ");
        sbuf.append(this.left == null ? "<null>" : this.left.toString());
        sbuf.append(" . ");
        sbuf.append(this.right == null ? "<null>" : this.right.toString());
        sbuf.append(')');
        return sbuf.toString();
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTree
    public ASTree getLeft() {
        return this.left;
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTree
    public ASTree getRight() {
        return this.right;
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTree
    public void setLeft(ASTree _left) {
        this.left = _left;
    }

    @Override // org.apache.ibatis.javassist.compiler.ast.ASTree
    public void setRight(ASTree _right) {
        this.right = _right;
    }
}
