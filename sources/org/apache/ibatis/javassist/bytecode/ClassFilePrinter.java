package org.apache.ibatis.javassist.bytecode;

import ch.qos.logback.classic.net.SyslogAppender;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import org.apache.ibatis.javassist.Modifier;
import org.apache.ibatis.javassist.bytecode.StackMapTable;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/ClassFilePrinter.class */
public class ClassFilePrinter {
    public static void print(ClassFile cf) {
        print(cf, new PrintWriter((OutputStream) System.out, true));
    }

    public static void print(ClassFile cf, PrintWriter out) {
        int mod = AccessFlag.toModifier(cf.getAccessFlags() & (-33));
        out.println("major: " + cf.major + ", minor: " + cf.minor + " modifiers: " + Integer.toHexString(cf.getAccessFlags()));
        out.println(Modifier.toString(mod) + " class " + cf.getName() + " extends " + cf.getSuperclass());
        String[] infs = cf.getInterfaces();
        if (infs != null && infs.length > 0) {
            out.print("    implements ");
            out.print(infs[0]);
            for (int i = 1; i < infs.length; i++) {
                out.print(", " + infs[i]);
            }
            out.println();
        }
        out.println();
        List list = cf.getFields();
        int n = list.size();
        for (int i2 = 0; i2 < n; i2++) {
            FieldInfo finfo = (FieldInfo) list.get(i2);
            int acc = finfo.getAccessFlags();
            out.println(Modifier.toString(AccessFlag.toModifier(acc)) + SymbolConstants.SPACE_SYMBOL + finfo.getName() + SyslogAppender.DEFAULT_STACKTRACE_PATTERN + finfo.getDescriptor());
            printAttributes(finfo.getAttributes(), out, 'f');
        }
        out.println();
        List list2 = cf.getMethods();
        int n2 = list2.size();
        for (int i3 = 0; i3 < n2; i3++) {
            MethodInfo minfo = (MethodInfo) list2.get(i3);
            int acc2 = minfo.getAccessFlags();
            out.println(Modifier.toString(AccessFlag.toModifier(acc2)) + SymbolConstants.SPACE_SYMBOL + minfo.getName() + SyslogAppender.DEFAULT_STACKTRACE_PATTERN + minfo.getDescriptor());
            printAttributes(minfo.getAttributes(), out, 'm');
            out.println();
        }
        out.println();
        printAttributes(cf.getAttributes(), out, 'c');
    }

    static void printAttributes(List list, PrintWriter out, char kind) {
        String s;
        if (list == null) {
            return;
        }
        int n = list.size();
        for (int i = 0; i < n; i++) {
            AttributeInfo ai = (AttributeInfo) list.get(i);
            if (ai instanceof CodeAttribute) {
                CodeAttribute ca = (CodeAttribute) ai;
                out.println("attribute: " + ai.getName() + ": " + ai.getClass().getName());
                out.println("max stack " + ca.getMaxStack() + ", max locals " + ca.getMaxLocals() + ", " + ca.getExceptionTable().size() + " catch blocks");
                out.println("<code attribute begin>");
                printAttributes(ca.getAttributes(), out, kind);
                out.println("<code attribute end>");
            } else if (ai instanceof AnnotationsAttribute) {
                out.println("annnotation: " + ai.toString());
            } else if (ai instanceof ParameterAnnotationsAttribute) {
                out.println("parameter annnotations: " + ai.toString());
            } else if (ai instanceof StackMapTable) {
                out.println("<stack map table begin>");
                StackMapTable.Printer.print((StackMapTable) ai, out);
                out.println("<stack map table end>");
            } else if (ai instanceof StackMap) {
                out.println("<stack map begin>");
                ((StackMap) ai).print(out);
                out.println("<stack map end>");
            } else if (ai instanceof SignatureAttribute) {
                SignatureAttribute sa = (SignatureAttribute) ai;
                String sig = sa.getSignature();
                out.println("signature: " + sig);
                if (kind == 'c') {
                    try {
                        s = SignatureAttribute.toClassSignature(sig).toString();
                    } catch (BadBytecode e) {
                        out.println("           syntax error");
                    }
                } else if (kind == 'm') {
                    s = SignatureAttribute.toMethodSignature(sig).toString();
                } else {
                    s = SignatureAttribute.toFieldSignature(sig).toString();
                }
                out.println("           " + s);
            } else {
                out.println("attribute: " + ai.getName() + " (" + ai.get().length + " byte): " + ai.getClass().getName());
            }
        }
    }
}
