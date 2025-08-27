package org.apache.ibatis.javassist.bytecode.analysis;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.PrintStream;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.Modifier;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.bytecode.BadBytecode;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.CodeIterator;
import org.apache.ibatis.javassist.bytecode.ConstPool;
import org.apache.ibatis.javassist.bytecode.Descriptor;
import org.apache.ibatis.javassist.bytecode.InstructionPrinter;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/analysis/FramePrinter.class */
public final class FramePrinter {
    private final PrintStream stream;

    public FramePrinter(PrintStream stream) {
        this.stream = stream;
    }

    public static void print(CtClass clazz, PrintStream stream) {
        new FramePrinter(stream).print(clazz);
    }

    public void print(CtClass clazz) {
        CtMethod[] methods = clazz.getDeclaredMethods();
        for (CtMethod ctMethod : methods) {
            print(ctMethod);
        }
    }

    private String getMethodString(CtMethod method) {
        try {
            return Modifier.toString(method.getModifiers()) + SymbolConstants.SPACE_SYMBOL + method.getReturnType().getName() + SymbolConstants.SPACE_SYMBOL + method.getName() + Descriptor.toString(method.getSignature()) + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR;
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void print(CtMethod method) {
        this.stream.println(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + getMethodString(method));
        MethodInfo info = method.getMethodInfo2();
        ConstPool pool = info.getConstPool();
        CodeAttribute code = info.getCodeAttribute();
        if (code == null) {
            return;
        }
        try {
            Frame[] frames = new Analyzer().analyze(method.getDeclaringClass(), info);
            int spacing = String.valueOf(code.getCodeLength()).length();
            CodeIterator iterator = code.iterator();
            while (iterator.hasNext()) {
                try {
                    int pos = iterator.next();
                    this.stream.println(pos + ": " + InstructionPrinter.instructionString(iterator, pos, pool));
                    addSpacing(spacing + 3);
                    Frame frame = frames[pos];
                    if (frame == null) {
                        this.stream.println("--DEAD CODE--");
                    } else {
                        printStack(frame);
                        addSpacing(spacing + 3);
                        printLocals(frame);
                    }
                } catch (BadBytecode e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (BadBytecode e2) {
            throw new RuntimeException(e2);
        }
    }

    private void printStack(Frame frame) {
        this.stream.print("stack [");
        int top = frame.getTopIndex();
        for (int i = 0; i <= top; i++) {
            if (i > 0) {
                this.stream.print(", ");
            }
            Type type = frame.getStack(i);
            this.stream.print(type);
        }
        this.stream.println("]");
    }

    private void printLocals(Frame frame) {
        this.stream.print("locals [");
        int length = frame.localsLength();
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                this.stream.print(", ");
            }
            Type type = frame.getLocal(i);
            this.stream.print(type == null ? "empty" : type.toString());
        }
        this.stream.println("]");
    }

    private void addSpacing(int count) {
        while (true) {
            int i = count;
            count--;
            if (i > 0) {
                this.stream.print(' ');
            } else {
                return;
            }
        }
    }
}
