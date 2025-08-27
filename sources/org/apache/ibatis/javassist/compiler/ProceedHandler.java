package org.apache.ibatis.javassist.compiler;

import org.apache.ibatis.javassist.bytecode.Bytecode;
import org.apache.ibatis.javassist.compiler.ast.ASTList;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/ProceedHandler.class */
public interface ProceedHandler {
    void doit(JvstCodeGen jvstCodeGen, Bytecode bytecode, ASTList aSTList) throws CompileError;

    void setReturnType(JvstTypeChecker jvstTypeChecker, ASTList aSTList) throws CompileError;
}
