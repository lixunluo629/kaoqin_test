package org.aspectj.weaver.bcel.asm;

import java.lang.reflect.Method;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/asm/AsmDetector.class */
public class AsmDetector {
    public static boolean isAsmAround;

    static {
        try {
            Class<?> reader = Class.forName("aj.org.objectweb.asm.ClassReader");
            Class<?> visitor = Class.forName("aj.org.objectweb.asm.ClassVisitor");
            Method m = reader.getMethod("accept", visitor, Integer.TYPE);
            isAsmAround = m != null;
        } catch (Exception e) {
            isAsmAround = false;
        }
    }
}
