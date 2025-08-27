package org.apache.ibatis.javassist.expr;

import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.CtBehavior;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtPrimitiveType;
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
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/expr/NewArray.class */
public class NewArray extends Expr {
    int opcode;

    protected NewArray(int pos, CodeIterator i, CtClass declaring, MethodInfo m, int op) {
        super(pos, i, declaring, m);
        this.opcode = op;
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

    public CtClass getComponentType() throws NotFoundException {
        if (this.opcode == 188) {
            int atype = this.iterator.byteAt(this.currentPos + 1);
            return getPrimitiveType(atype);
        }
        if (this.opcode == 189 || this.opcode == 197) {
            int index = this.iterator.u16bitAt(this.currentPos + 1);
            String desc = getConstPool().getClassInfo(index);
            int dim = Descriptor.arrayDimension(desc);
            return Descriptor.toCtClass(Descriptor.toArrayComponent(desc, dim), this.thisClass.getClassPool());
        }
        throw new RuntimeException("bad opcode: " + this.opcode);
    }

    CtClass getPrimitiveType(int atype) {
        switch (atype) {
            case 4:
                return CtClass.booleanType;
            case 5:
                return CtClass.charType;
            case 6:
                return CtClass.floatType;
            case 7:
                return CtClass.doubleType;
            case 8:
                return CtClass.byteType;
            case 9:
                return CtClass.shortType;
            case 10:
                return CtClass.intType;
            case 11:
                return CtClass.longType;
            default:
                throw new RuntimeException("bad atype: " + atype);
        }
    }

    public int getDimension() {
        if (this.opcode == 188) {
            return 1;
        }
        if (this.opcode == 189 || this.opcode == 197) {
            int index = this.iterator.u16bitAt(this.currentPos + 1);
            String desc = getConstPool().getClassInfo(index);
            return Descriptor.arrayDimension(desc) + (this.opcode == 189 ? 1 : 0);
        }
        throw new RuntimeException("bad opcode: " + this.opcode);
    }

    public int getCreatedDimensions() {
        if (this.opcode == 197) {
            return this.iterator.byteAt(this.currentPos + 3);
        }
        return 1;
    }

    @Override // org.apache.ibatis.javassist.expr.Expr
    public void replace(String statement) throws RuntimeException, CannotCompileException {
        try {
            replace2(statement);
        } catch (NotFoundException e) {
            throw new CannotCompileException(e);
        } catch (BadBytecode e2) {
            throw new CannotCompileException("broken method");
        } catch (CompileError e3) {
            throw new CannotCompileException(e3);
        }
    }

    private void replace2(String statement) throws CompileError, BadBytecode, NotFoundException, RuntimeException, CannotCompileException {
        int index;
        String desc;
        int codeLength;
        this.thisClass.getClassFile();
        ConstPool constPool = getConstPool();
        int pos = this.currentPos;
        int dim = 1;
        if (this.opcode == 188) {
            index = this.iterator.byteAt(this.currentPos + 1);
            CtPrimitiveType cpt = (CtPrimitiveType) getPrimitiveType(index);
            desc = PropertyAccessor.PROPERTY_KEY_PREFIX + cpt.getDescriptor();
            codeLength = 2;
        } else if (this.opcode == 189) {
            index = this.iterator.u16bitAt(pos + 1);
            String desc2 = constPool.getClassInfo(index);
            if (desc2.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
                desc = PropertyAccessor.PROPERTY_KEY_PREFIX + desc2;
            } else {
                desc = "[L" + desc2 + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR;
            }
            codeLength = 3;
        } else if (this.opcode == 197) {
            index = this.iterator.u16bitAt(this.currentPos + 1);
            desc = constPool.getClassInfo(index);
            dim = this.iterator.byteAt(this.currentPos + 3);
            codeLength = 4;
        } else {
            throw new RuntimeException("bad opcode: " + this.opcode);
        }
        CtClass retType = Descriptor.toCtClass(desc, this.thisClass.getClassPool());
        Javac jc = new Javac(this.thisClass);
        CodeAttribute ca = this.iterator.get();
        CtClass[] params = new CtClass[dim];
        for (int i = 0; i < dim; i++) {
            params[i] = CtClass.intType;
        }
        int paramVar = ca.getMaxLocals();
        jc.recordParams("java.lang.Object", params, true, paramVar, withinStatic());
        checkResultValue(retType, statement);
        int retVar = jc.recordReturnType(retType, true);
        jc.recordProceed(new ProceedForArray(retType, this.opcode, index, dim));
        Bytecode bytecode = jc.getBytecode();
        storeStack(params, true, paramVar, bytecode);
        jc.recordLocalVariables(ca, pos);
        bytecode.addOpcode(1);
        bytecode.addAstore(retVar);
        jc.compileStmnt(statement);
        bytecode.addAload(retVar);
        replace0(pos, bytecode, codeLength);
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/expr/NewArray$ProceedForArray.class */
    static class ProceedForArray implements ProceedHandler {
        CtClass arrayType;
        int opcode;
        int index;
        int dimension;

        ProceedForArray(CtClass type, int op, int i, int dim) {
            this.arrayType = type;
            this.opcode = op;
            this.index = i;
            this.dimension = dim;
        }

        @Override // org.apache.ibatis.javassist.compiler.ProceedHandler
        public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
            int num = gen.getMethodArgsLength(args);
            if (num != this.dimension) {
                throw new CompileError("$proceed() with a wrong number of parameters");
            }
            gen.atMethodArgs(args, new int[num], new int[num], new String[num]);
            bytecode.addOpcode(this.opcode);
            if (this.opcode == 189) {
                bytecode.addIndex(this.index);
            } else if (this.opcode == 188) {
                bytecode.add(this.index);
            } else {
                bytecode.addIndex(this.index);
                bytecode.add(this.dimension);
                bytecode.growStack(1 - this.dimension);
            }
            gen.setType(this.arrayType);
        }

        @Override // org.apache.ibatis.javassist.compiler.ProceedHandler
        public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
            c.setType(this.arrayType);
        }
    }
}
