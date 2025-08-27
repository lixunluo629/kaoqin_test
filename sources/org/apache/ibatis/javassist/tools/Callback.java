package org.apache.ibatis.javassist.tools;

import java.util.HashMap;
import java.util.UUID;
import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.CtBehavior;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/tools/Callback.class */
public abstract class Callback {
    public static HashMap callbacks = new HashMap();
    private final String sourceCode;

    public abstract void result(Object[] objArr);

    public Callback(String src) {
        String uuid = UUID.randomUUID().toString();
        callbacks.put(uuid, this);
        this.sourceCode = "((javassist.tools.Callback) javassist.tools.Callback.callbacks.get(\"" + uuid + "\")).result(new Object[]{" + src + "});";
    }

    public String toString() {
        return sourceCode();
    }

    public String sourceCode() {
        return this.sourceCode;
    }

    public static void insertBefore(CtBehavior behavior, Callback callback) throws RuntimeException, CannotCompileException {
        behavior.insertBefore(callback.toString());
    }

    public static void insertAfter(CtBehavior behavior, Callback callback) throws RuntimeException, CannotCompileException {
        behavior.insertAfter(callback.toString(), false);
    }

    public static void insertAfter(CtBehavior behavior, Callback callback, boolean asFinally) throws RuntimeException, CannotCompileException {
        behavior.insertAfter(callback.toString(), asFinally);
    }

    public static int insertAt(CtBehavior behavior, Callback callback, int lineNum) throws CannotCompileException {
        return behavior.insertAt(lineNum, callback.toString());
    }
}
