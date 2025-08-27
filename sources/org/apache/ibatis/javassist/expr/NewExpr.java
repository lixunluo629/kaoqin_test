package org.apache.ibatis.javassist.expr;

import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtBehavior;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.bytecode.BadBytecode;
import org.apache.ibatis.javassist.bytecode.Bytecode;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.CodeIterator;
import org.apache.ibatis.javassist.bytecode.ConstPool;
import org.apache.ibatis.javassist.bytecode.Descriptor;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.apache.ibatis.javassist.compiler.CompileError;
import org.apache.ibatis.javassist.compiler.Javac;
import org.apache.ibatis.javassist.compiler.JvstCodeGen;
import org.apache.ibatis.javassist.compiler.JvstTypeChecker;
import org.apache.ibatis.javassist.compiler.ProceedHandler;
import org.apache.ibatis.javassist.compiler.ast.ASTList;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/expr/NewExpr.class */
public class NewExpr extends Expr {
    String newTypeName;
    int newPos;

    protected NewExpr(int pos, CodeIterator i, CtClass declaring, MethodInfo m, String type, int np) {
        super(pos, i, declaring, m);
        this.newTypeName = type;
        this.newPos = np;
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

    private CtClass getCtClass() throws NotFoundException {
        return this.thisClass.getClassPool().get(this.newTypeName);
    }

    public String getClassName() {
        return this.newTypeName;
    }

    public String getSignature() {
        ConstPool constPool = getConstPool();
        int methodIndex = this.iterator.u16bitAt(this.currentPos + 1);
        return constPool.getMethodrefType(methodIndex);
    }

    public CtConstructor getConstructor() throws NotFoundException {
        ConstPool cp = getConstPool();
        int index = this.iterator.u16bitAt(this.currentPos + 1);
        String desc = cp.getMethodrefType(index);
        return getCtClass().getConstructor(desc);
    }

    @Override // org.apache.ibatis.javassist.expr.Expr
    public CtClass[] mayThrow() {
        return super.mayThrow();
    }

    private int canReplace() throws CannotCompileException {
        int op = this.iterator.byteAt(this.newPos + 3);
        if (op == 89) {
            return (this.iterator.byteAt(this.newPos + 4) == 94 && this.iterator.byteAt(this.newPos + 5) == 88) ? 6 : 4;
        }
        if (op == 90 && this.iterator.byteAt(this.newPos + 4) == 95) {
            return 5;
        }
        return 3;
    }

    @Override // org.apache.ibatis.javassist.expr.Expr
    public void replace(String statement) throws RuntimeException, CannotCompileException {
        this.thisClass.getClassFile();
        int pos = this.newPos;
        int newIndex = this.iterator.u16bitAt(pos + 1);
        int codeSize = canReplace();
        int end = pos + codeSize;
        for (int i = pos; i < end; i++) {
            this.iterator.writeByte(0, i);
        }
        ConstPool constPool = getConstPool();
        int pos2 = this.currentPos;
        int methodIndex = this.iterator.u16bitAt(pos2 + 1);
        String signature = constPool.getMethodrefType(methodIndex);
        Javac jc = new Javac(this.thisClass);
        ClassPool cp = this.thisClass.getClassPool();
        CodeAttribute ca = this.iterator.get();
        try {
            CtClass[] params = Descriptor.getParameterTypes(signature, cp);
            CtClass newType = cp.get(this.newTypeName);
            int paramVar = ca.getMaxLocals();
            jc.recordParams(this.newTypeName, params, true, paramVar, withinStatic());
            int retVar = jc.recordReturnType(newType, true);
            jc.recordProceed(new ProceedForNew(newType, newIndex, methodIndex));
            checkResultValue(newType, statement);
            Bytecode bytecode = jc.getBytecode();
            storeStack(params, true, paramVar, bytecode);
            jc.recordLocalVariables(ca, pos2);
            bytecode.addConstZero(newType);
            bytecode.addStore(retVar, newType);
            jc.compileStmnt(statement);
            if (codeSize > 3) {
                bytecode.addAload(retVar);
            }
            replace0(pos2, bytecode, 3);
        } catch (NotFoundException e) {
            throw new CannotCompileException(e);
        } catch (BadBytecode e2) {
            throw new CannotCompileException("broken method");
        } catch (CompileError e3) {
            throw new CannotCompileException(e3);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/expr/NewExpr$ProceedForNew.class */
    static class ProceedForNew implements ProceedHandler {
        CtClass newType;
        int newIndex;
        int methodIndex;

        ProceedForNew(CtClass nt, int ni, int mi) {
            this.newType = nt;
            this.newIndex = ni;
            this.methodIndex = mi;
        }

        @Override // org.apache.ibatis.javassist.compiler.ProceedHandler
        public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
            bytecode.addOpcode(187);
            bytecode.addIndex(this.newIndex);
            bytecode.addOpcode(89);
            gen.atMethodCallCore(this.newType, "<init>", args, false, true, -1, null);
            gen.setType(this.newType);
        }

        @Override // org.apache.ibatis.javassist.compiler.ProceedHandler
        public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
            c.atMethodCallCore(this.newType, "<init>", args);
            c.setType(this.newType);
        }
    }
}
