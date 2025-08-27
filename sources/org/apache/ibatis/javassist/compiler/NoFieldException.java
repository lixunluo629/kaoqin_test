package org.apache.ibatis.javassist.compiler;

import org.apache.ibatis.javassist.compiler.ast.ASTree;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/NoFieldException.class */
public class NoFieldException extends CompileError {
    private String fieldName;
    private ASTree expr;

    public NoFieldException(String name, ASTree e) {
        super("no such field: " + name);
        this.fieldName = name;
        this.expr = e;
    }

    public String getField() {
        return this.fieldName;
    }

    public ASTree getExpr() {
        return this.expr;
    }
}
