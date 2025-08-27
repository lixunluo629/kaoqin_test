package org.apache.ibatis.javassist.expr;

import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtBehavior;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.bytecode.BadBytecode;
import org.apache.ibatis.javassist.bytecode.Bytecode;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.CodeIterator;
import org.apache.ibatis.javassist.bytecode.ConstPool;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.apache.ibatis.javassist.compiler.CompileError;
import org.apache.ibatis.javassist.compiler.Javac;
import org.apache.ibatis.javassist.compiler.JvstCodeGen;
import org.apache.ibatis.javassist.compiler.JvstTypeChecker;
import org.apache.ibatis.javassist.compiler.ProceedHandler;
import org.apache.ibatis.javassist.compiler.ast.ASTList;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/expr/Cast.class */
public class Cast extends Expr {
    protected Cast(int pos, CodeIterator i, CtClass declaring, MethodInfo m) {
        super(pos, i, declaring, m);
    }

    @Override // org.apache.ibatis.javassist.expr.Expr
    public CtBehavior where() {
        return super.where();
    }

    @Override // org.apache.ibatis.javassist.expr.Expr
    public int getLineNumber() {
        return super.getLineNumber();
    }

    @Override // org.apache.ibatis.javassist.expr.Expr
    public String getFileName() {
        return super.getFileName();
    }

    public CtClass getType() throws NotFoundException {
        ConstPool cp = getConstPool();
        int pos = this.currentPos;
        int index = this.iterator.u16bitAt(pos + 1);
        String name = cp.getClassInfo(index);
        return this.thisClass.getClassPool().getCtClass(name);
    }

    @Override // org.apache.ibatis.javassist.expr.Expr
    public CtClass[] mayThrow() {
        return super.mayThrow();
    }

    @Override // org.apache.ibatis.javassist.expr.Expr
    public void replace(String statement) throws RuntimeException, CannotCompileException {
        this.thisClass.getClassFile();
        getConstPool();
        int pos = this.currentPos;
        int index = this.iterator.u16bitAt(pos + 1);
        Javac jc = new Javac(this.thisClass);
        ClassPool cp = this.thisClass.getClassPool();
        CodeAttribute ca = this.iterator.get();
        try {
            CtClass[] params = {cp.get("java.lang.Object")};
            CtClass retType = getType();
            int paramVar = ca.getMaxLocals();
            jc.recordParams("java.lang.Object", params, true, paramVar, withinStatic());
            int retVar = jc.recordReturnType(retType, true);
            jc.recordProceed(new ProceedForCast(index, retType));
            checkResultValue(retType, statement);
            Bytecode bytecode = jc.getBytecode();
            storeStack(params, true, paramVar, bytecode);
            jc.recordLocalVariables(ca, pos);
            bytecode.addConstZero(retType);
            bytecode.addStore(retVar, retType);
            jc.compileStmnt(statement);
            bytecode.addLoad(retVar, retType);
            replace0(pos, bytecode, 3);
        } catch (NotFoundException e) {
            throw new CannotCompileException(e);
        } catch (BadBytecode e2) {
            throw new CannotCompileException("broken method");
        } catch (CompileError e3) {
            throw new CannotCompileException(e3);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/expr/Cast$ProceedForCast.class */
    static class ProceedForCast implements ProceedHandler {
        int index;
        CtClass retType;

        ProceedForCast(int i, CtClass t) {
            this.index = i;
            this.retType = t;
        }

        @Override // org.apache.ibatis.javassist.compiler.ProceedHandler
        public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
            if (gen.getMethodArgsLength(args) != 1) {
                throw new CompileError("$proceed() cannot take more than one parameter for cast");
            }
            gen.atMethodArgs(args, new int[1], new int[1], new String[1]);
            bytecode.addOpcode(192);
            bytecode.addIndex(this.index);
            gen.setType(this.retType);
        }

        @Override // org.apache.ibatis.javassist.compiler.ProceedHandler
        public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
            c.atMethodArgs(args, new int[1], new int[1], new String[1]);
            c.setType(this.retType);
        }
    }
}
