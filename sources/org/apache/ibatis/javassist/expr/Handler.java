package org.apache.ibatis.javassist.expr;

import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.CtBehavior;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.bytecode.Bytecode;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.CodeIterator;
import org.apache.ibatis.javassist.bytecode.ConstPool;
import org.apache.ibatis.javassist.bytecode.ExceptionTable;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.apache.ibatis.javassist.compiler.CompileError;
import org.apache.ibatis.javassist.compiler.Javac;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/expr/Handler.class */
public class Handler extends Expr {
    private static String EXCEPTION_NAME = "$1";
    private ExceptionTable etable;
    private int index;

    protected Handler(ExceptionTable et, int nth, CodeIterator it, CtClass declaring, MethodInfo m) {
        super(et.handlerPc(nth), it, declaring, m);
        this.etable = et;
        this.index = nth;
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

    @Override // org.apache.ibatis.javassist.expr.Expr
    public CtClass[] mayThrow() {
        return super.mayThrow();
    }

    public CtClass getType() throws NotFoundException {
        int type = this.etable.catchType(this.index);
        if (type == 0) {
            return null;
        }
        ConstPool cp = getConstPool();
        String name = cp.getClassInfo(type);
        return this.thisClass.getClassPool().getCtClass(name);
    }

    public boolean isFinally() {
        return this.etable.catchType(this.index) == 0;
    }

    @Override // org.apache.ibatis.javassist.expr.Expr
    public void replace(String statement) throws CannotCompileException {
        throw new RuntimeException("not implemented yet");
    }

    public void insertBefore(String src) throws CannotCompileException {
        this.edited = true;
        getConstPool();
        CodeAttribute ca = this.iterator.get();
        Javac jv = new Javac(this.thisClass);
        Bytecode b = jv.getBytecode();
        b.setStackDepth(1);
        b.setMaxLocals(ca.getMaxLocals());
        try {
            CtClass type = getType();
            int var = jv.recordVariable(type, EXCEPTION_NAME);
            jv.recordReturnType(type, false);
            b.addAstore(var);
            jv.compileStmnt(src);
            b.addAload(var);
            int oldHandler = this.etable.handlerPc(this.index);
            b.addOpcode(167);
            b.addIndex(((oldHandler - this.iterator.getCodeLength()) - b.currentPc()) + 1);
            this.maxStack = b.getMaxStack();
            this.maxLocals = b.getMaxLocals();
            int pos = this.iterator.append(b.get());
            this.iterator.append(b.getExceptionTable(), pos);
            this.etable.setHandlerPc(this.index, pos);
        } catch (NotFoundException e) {
            throw new CannotCompileException(e);
        } catch (CompileError e2) {
            throw new CannotCompileException(e2);
        }
    }
}
