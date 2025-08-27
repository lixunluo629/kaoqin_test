package org.apache.ibatis.javassist;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.ibatis.javassist.bytecode.ClassFile;
import org.apache.ibatis.javassist.bytecode.Descriptor;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/SerialVersionUID.class */
public class SerialVersionUID {
    public static void setSerialVersionUID(CtClass clazz) throws NotFoundException, RuntimeException, CannotCompileException {
        try {
            clazz.getDeclaredField("serialVersionUID");
        } catch (NotFoundException e) {
            if (!isSerializable(clazz)) {
                return;
            }
            CtField field = new CtField(CtClass.longType, "serialVersionUID", clazz);
            field.setModifiers(26);
            clazz.addField(field, calculateDefault(clazz) + StandardRoles.L);
        }
    }

    private static boolean isSerializable(CtClass clazz) throws NotFoundException {
        ClassPool pool = clazz.getClassPool();
        return clazz.subtypeOf(pool.get("java.io.Serializable"));
    }

    public static long calculateDefault(CtClass clazz) throws NoSuchAlgorithmException, IOException, RuntimeException, CannotCompileException {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bout);
            ClassFile classFile = clazz.getClassFile();
            String javaName = javaName(clazz);
            out.writeUTF(javaName);
            CtMethod[] methods = clazz.getDeclaredMethods();
            int classMods = clazz.getModifiers();
            if ((classMods & 512) != 0) {
                if (methods.length > 0) {
                    classMods |= 1024;
                } else {
                    classMods &= -1025;
                }
            }
            out.writeInt(classMods);
            String[] interfaces = classFile.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                interfaces[i] = javaName(interfaces[i]);
            }
            Arrays.sort(interfaces);
            for (String str : interfaces) {
                out.writeUTF(str);
            }
            CtField[] fields = clazz.getDeclaredFields();
            Arrays.sort(fields, new Comparator() { // from class: org.apache.ibatis.javassist.SerialVersionUID.1
                @Override // java.util.Comparator
                public int compare(Object o1, Object o2) {
                    CtField field1 = (CtField) o1;
                    CtField field2 = (CtField) o2;
                    return field1.getName().compareTo(field2.getName());
                }
            });
            for (CtField field : fields) {
                int mods = field.getModifiers();
                if ((mods & 2) == 0 || (mods & 136) == 0) {
                    out.writeUTF(field.getName());
                    out.writeInt(mods);
                    out.writeUTF(field.getFieldInfo2().getDescriptor());
                }
            }
            if (classFile.getStaticInitializer() != null) {
                out.writeUTF("<clinit>");
                out.writeInt(8);
                out.writeUTF("()V");
            }
            CtConstructor[] constructors = clazz.getDeclaredConstructors();
            Arrays.sort(constructors, new Comparator() { // from class: org.apache.ibatis.javassist.SerialVersionUID.2
                @Override // java.util.Comparator
                public int compare(Object o1, Object o2) {
                    CtConstructor c1 = (CtConstructor) o1;
                    CtConstructor c2 = (CtConstructor) o2;
                    return c1.getMethodInfo2().getDescriptor().compareTo(c2.getMethodInfo2().getDescriptor());
                }
            });
            for (CtConstructor constructor : constructors) {
                int mods2 = constructor.getModifiers();
                if ((mods2 & 2) == 0) {
                    out.writeUTF("<init>");
                    out.writeInt(mods2);
                    out.writeUTF(constructor.getMethodInfo2().getDescriptor().replace('/', '.'));
                }
            }
            Arrays.sort(methods, new Comparator() { // from class: org.apache.ibatis.javassist.SerialVersionUID.3
                @Override // java.util.Comparator
                public int compare(Object o1, Object o2) {
                    CtMethod m1 = (CtMethod) o1;
                    CtMethod m2 = (CtMethod) o2;
                    int value = m1.getName().compareTo(m2.getName());
                    if (value == 0) {
                        value = m1.getMethodInfo2().getDescriptor().compareTo(m2.getMethodInfo2().getDescriptor());
                    }
                    return value;
                }
            });
            for (CtMethod method : methods) {
                int mods3 = method.getModifiers() & 3391;
                if ((mods3 & 2) == 0) {
                    out.writeUTF(method.getName());
                    out.writeInt(mods3);
                    out.writeUTF(method.getMethodInfo2().getDescriptor().replace('/', '.'));
                }
            }
            out.flush();
            MessageDigest digest = MessageDigest.getInstance("SHA");
            byte[] digested = digest.digest(bout.toByteArray());
            long hash = 0;
            for (int i2 = Math.min(digested.length, 8) - 1; i2 >= 0; i2--) {
                hash = (hash << 8) | (digested[i2] & 255);
            }
            return hash;
        } catch (IOException e) {
            throw new CannotCompileException(e);
        } catch (NoSuchAlgorithmException e2) {
            throw new CannotCompileException(e2);
        }
    }

    private static String javaName(CtClass clazz) {
        return Descriptor.toJavaName(Descriptor.toJvmName(clazz));
    }

    private static String javaName(String name) {
        return Descriptor.toJavaName(Descriptor.toJvmName(name));
    }
}
