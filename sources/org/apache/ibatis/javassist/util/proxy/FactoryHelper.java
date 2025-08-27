package org.apache.ibatis.javassist.util.proxy;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.ProtectionDomain;
import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.bytecode.ClassFile;
import org.springframework.util.ClassUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/util/proxy/FactoryHelper.class */
public class FactoryHelper {
    public static final Class[] primitiveTypes = {Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE};
    public static final String[] wrapperTypes = {"java.lang.Boolean", "java.lang.Byte", "java.lang.Character", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.lang.Void"};
    public static final String[] wrapperDesc = {"(Z)V", "(B)V", "(C)V", "(S)V", "(I)V", "(J)V", "(F)V", "(D)V"};
    public static final String[] unwarpMethods = {"booleanValue", "byteValue", "charValue", "shortValue", "intValue", "longValue", "floatValue", "doubleValue"};
    public static final String[] unwrapDesc = {"()Z", "()B", "()C", "()S", "()I", "()J", "()F", "()D"};
    public static final int[] dataSize = {1, 1, 1, 1, 1, 2, 1, 2};

    public static final int typeIndex(Class type) {
        Class[] list = primitiveTypes;
        int n = list.length;
        for (int i = 0; i < n; i++) {
            if (list[i] == type) {
                return i;
            }
        }
        throw new RuntimeException("bad type:" + type.getName());
    }

    public static Class toClass(ClassFile cf, ClassLoader loader) throws CannotCompileException {
        return toClass(cf, loader, null);
    }

    public static Class toClass(ClassFile cf, ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
        try {
            byte[] b = toBytecode(cf);
            if (ProxyFactory.onlyPublicMethods) {
                return DefineClassHelper.toPublicClass(cf.getName(), b);
            }
            return DefineClassHelper.toClass(cf.getName(), loader, domain, b);
        } catch (IOException e) {
            throw new CannotCompileException(e);
        }
    }

    private static byte[] toBytecode(ClassFile cf) throws IOException {
        ByteArrayOutputStream barray = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(barray);
        try {
            cf.write(out);
            return barray.toByteArray();
        } finally {
            out.close();
        }
    }

    public static void writeFile(ClassFile cf, String directoryName) throws CannotCompileException {
        try {
            writeFile0(cf, directoryName);
        } catch (IOException e) {
            throw new CannotCompileException(e);
        }
    }

    private static void writeFile0(ClassFile cf, String directoryName) throws IOException, CannotCompileException {
        String classname = cf.getName();
        String filename = directoryName + File.separatorChar + classname.replace('.', File.separatorChar) + ClassUtils.CLASS_FILE_SUFFIX;
        int pos = filename.lastIndexOf(File.separatorChar);
        if (pos > 0) {
            String dir = filename.substring(0, pos);
            if (!dir.equals(".")) {
                new File(dir).mkdirs();
            }
        }
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
        try {
            try {
                cf.write(out);
                out.close();
            } catch (IOException e) {
                throw e;
            }
        } catch (Throwable th) {
            out.close();
            throw th;
        }
    }
}
