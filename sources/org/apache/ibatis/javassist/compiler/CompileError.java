package org.apache.ibatis.javassist.compiler;

import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.NotFoundException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/CompileError.class */
public class CompileError extends Exception {
    private Lex lex;
    private String reason;

    public CompileError(String s, Lex l) {
        this.reason = s;
        this.lex = l;
    }

    public CompileError(String s) {
        this.reason = s;
        this.lex = null;
    }

    public CompileError(CannotCompileException e) {
        this(e.getReason());
    }

    public CompileError(NotFoundException e) {
        this("cannot find " + e.getMessage());
    }

    public Lex getLex() {
        return this.lex;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.reason;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "compile error: " + this.reason;
    }
}
