package org.apache.ibatis.javassist.util.proxy;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.bytecode.ClassFile;
import sun.misc.Unsafe;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/util/proxy/DefineClassHelper.class */
public class DefineClassHelper {
    private static Method defineClass1;
    private static Method defineClass2;
    private static Unsafe sunMiscUnsafe;

    static {
        defineClass1 = null;
        defineClass2 = null;
        sunMiscUnsafe = null;
        if (ClassFile.MAJOR_VERSION < 53) {
            try {
                Class<?> cl = Class.forName("java.lang.ClassLoader");
                defineClass1 = SecurityActions.getDeclaredMethod(cl, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE});
                defineClass2 = SecurityActions.getDeclaredMethod(cl, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
                return;
            } catch (Exception e) {
                throw new RuntimeException("cannot initialize");
            }
        }
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            sunMiscUnsafe = (Unsafe) theUnsafe.get(null);
        } catch (Throwable th) {
        }
    }

    public static Class<?> toClass(String className, ClassLoader loader, ProtectionDomain domain, byte[] bcode) throws CannotCompileException {
        if (ClassFile.MAJOR_VERSION >= 53 && sunMiscUnsafe != null) {
            try {
                return sunMiscUnsafe.defineClass(className, bcode, 0, bcode.length, loader, domain);
            } catch (Throwable th) {
            }
        }
        return toClass2(className, loader, domain, bcode);
    }

    static Class<?> toPublicClass(String className, byte[] bcode) throws CannotCompileException {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            return lookup.dropLookupMode(2).defineClass(bcode);
        } catch (Throwable t) {
            throw new CannotCompileException(t);
        }
    }

    private static Class<?> toClass2(String cname, ClassLoader loader, ProtectionDomain domain, byte[] bcode) throws CannotCompileException {
        Method method;
        Object[] args;
        try {
            if (domain == null) {
                method = defineClass1;
                args = new Object[]{cname, bcode, 0, Integer.valueOf(bcode.length)};
            } else {
                method = defineClass2;
                args = new Object[]{cname, bcode, 0, Integer.valueOf(bcode.length), domain};
            }
            return toClass3(method, loader, args);
        } catch (RuntimeException e) {
            throw e;
        } catch (InvocationTargetException e2) {
            throw new CannotCompileException(e2.getTargetException());
        } catch (Exception e3) {
            throw new CannotCompileException(e3);
        }
    }

    private static synchronized Class<?> toClass3(Method method, ClassLoader loader, Object[] args) throws Exception {
        SecurityActions.setAccessible(method, true);
        Class<?> clazz = (Class) method.invoke(loader, args);
        SecurityActions.setAccessible(method, false);
        return clazz;
    }
}
