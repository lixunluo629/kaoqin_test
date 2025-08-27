package org.apache.ibatis.javassist;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Collection;
import org.apache.ibatis.javassist.CtField;
import org.apache.ibatis.javassist.bytecode.ClassFile;
import org.apache.ibatis.javassist.bytecode.Descriptor;
import org.apache.ibatis.javassist.compiler.AccessorMaker;
import org.apache.ibatis.javassist.expr.ExprEditor;
import org.apache.xmlbeans.XmlErrorCodes;
import org.springframework.beans.PropertyAccessor;
import org.springframework.util.ClassUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/CtClass.class */
public abstract class CtClass {
    protected String qualifiedName;
    public static final String version = "3.22.0-GA";
    static final String javaLangObject = "java.lang.Object";
    public static CtClass charType;
    public static CtClass byteType;
    public static CtClass shortType;
    public static CtClass intType;
    public static CtClass longType;
    public static CtClass floatType;
    public static CtClass doubleType;
    public static CtClass voidType;
    public static String debugDump = null;
    static CtClass[] primitiveTypes = new CtClass[9];
    public static CtClass booleanType = new CtPrimitiveType("boolean", 'Z', "java.lang.Boolean", "booleanValue", "()Z", 172, 4, 1);

    static {
        primitiveTypes[0] = booleanType;
        charType = new CtPrimitiveType("char", 'C', "java.lang.Character", "charValue", "()C", 172, 5, 1);
        primitiveTypes[1] = charType;
        byteType = new CtPrimitiveType("byte", 'B', "java.lang.Byte", "byteValue", "()B", 172, 8, 1);
        primitiveTypes[2] = byteType;
        shortType = new CtPrimitiveType("short", 'S', "java.lang.Short", "shortValue", "()S", 172, 9, 1);
        primitiveTypes[3] = shortType;
        intType = new CtPrimitiveType(XmlErrorCodes.INT, 'I', "java.lang.Integer", "intValue", "()I", 172, 10, 1);
        primitiveTypes[4] = intType;
        longType = new CtPrimitiveType(XmlErrorCodes.LONG, 'J', "java.lang.Long", "longValue", "()J", 173, 11, 2);
        primitiveTypes[5] = longType;
        floatType = new CtPrimitiveType(XmlErrorCodes.FLOAT, 'F', "java.lang.Float", "floatValue", "()F", 174, 6, 1);
        primitiveTypes[6] = floatType;
        doubleType = new CtPrimitiveType(XmlErrorCodes.DOUBLE, 'D', "java.lang.Double", "doubleValue", "()D", 175, 7, 2);
        primitiveTypes[7] = doubleType;
        voidType = new CtPrimitiveType("void", 'V', "java.lang.Void", null, null, 177, 0, 0);
        primitiveTypes[8] = voidType;
    }

    public static void main(String[] args) {
        System.out.println("Javassist version 3.22.0-GA");
        System.out.println("Copyright (C) 1999-2017 Shigeru Chiba. All Rights Reserved.");
    }

    protected CtClass(String name) {
        this.qualifiedName = name;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(getClass().getName());
        buf.append("@");
        buf.append(Integer.toHexString(hashCode()));
        buf.append(PropertyAccessor.PROPERTY_KEY_PREFIX);
        extendToString(buf);
        buf.append("]");
        return buf.toString();
    }

    protected void extendToString(StringBuffer buffer) {
        buffer.append(getName());
    }

    public ClassPool getClassPool() {
        return null;
    }

    public ClassFile getClassFile() throws RuntimeException {
        checkModify();
        return getClassFile2();
    }

    public ClassFile getClassFile2() {
        return null;
    }

    public AccessorMaker getAccessorMaker() {
        return null;
    }

    public URL getURL() throws NotFoundException {
        throw new NotFoundException(getName());
    }

    public boolean isModified() {
        return false;
    }

    public boolean isFrozen() {
        return true;
    }

    public void freeze() {
    }

    void checkModify() throws RuntimeException {
        if (isFrozen()) {
            throw new RuntimeException(getName() + " class is frozen");
        }
    }

    public void defrost() {
        throw new RuntimeException("cannot defrost " + getName());
    }

    public boolean isPrimitive() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public CtClass getComponentType() throws NotFoundException {
        return null;
    }

    public boolean subtypeOf(CtClass clazz) throws NotFoundException {
        return this == clazz || getName().equals(clazz.getName());
    }

    public String getName() {
        return this.qualifiedName;
    }

    public final String getSimpleName() {
        String qname = this.qualifiedName;
        int index = qname.lastIndexOf(46);
        if (index < 0) {
            return qname;
        }
        return qname.substring(index + 1);
    }

    public final String getPackageName() {
        String qname = this.qualifiedName;
        int index = qname.lastIndexOf(46);
        if (index < 0) {
            return null;
        }
        return qname.substring(0, index);
    }

    public void setName(String name) {
        checkModify();
        if (name != null) {
            this.qualifiedName = name;
        }
    }

    public String getGenericSignature() {
        return null;
    }

    public void setGenericSignature(String sig) throws RuntimeException {
        checkModify();
    }

    public void replaceClassName(String oldName, String newName) throws RuntimeException {
        checkModify();
    }

    public void replaceClassName(ClassMap map) throws RuntimeException {
        checkModify();
    }

    public synchronized Collection getRefClasses() {
        ClassFile cf = getClassFile2();
        if (cf != null) {
            ClassMap cm = new ClassMap() { // from class: org.apache.ibatis.javassist.CtClass.1
                @Override // org.apache.ibatis.javassist.ClassMap
                public void put(String oldname, String newname) {
                    put0(oldname, newname);
                }

                @Override // org.apache.ibatis.javassist.ClassMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
                public Object get(Object jvmClassName) {
                    String n = toJavaName((String) jvmClassName);
                    put0(n, n);
                    return null;
                }

                @Override // org.apache.ibatis.javassist.ClassMap
                public void fix(String name) {
                }
            };
            cf.getRefClasses(cm);
            return cm.values();
        }
        return null;
    }

    public boolean isInterface() {
        return false;
    }

    public boolean isAnnotation() {
        return false;
    }

    public boolean isEnum() {
        return false;
    }

    public int getModifiers() {
        return 0;
    }

    public boolean hasAnnotation(Class annotationType) {
        return hasAnnotation(annotationType.getName());
    }

    public boolean hasAnnotation(String annotationTypeName) {
        return false;
    }

    public Object getAnnotation(Class clz) throws ClassNotFoundException {
        return null;
    }

    public Object[] getAnnotations() throws ClassNotFoundException {
        return new Object[0];
    }

    public Object[] getAvailableAnnotations() {
        return new Object[0];
    }

    public CtClass[] getDeclaredClasses() throws NotFoundException {
        return getNestedClasses();
    }

    public CtClass[] getNestedClasses() throws NotFoundException {
        return new CtClass[0];
    }

    public void setModifiers(int mod) {
        checkModify();
    }

    public boolean subclassOf(CtClass superclass) {
        return false;
    }

    public CtClass getSuperclass() throws NotFoundException {
        return null;
    }

    public void setSuperclass(CtClass clazz) throws RuntimeException, CannotCompileException {
        checkModify();
    }

    public CtClass[] getInterfaces() throws NotFoundException {
        return new CtClass[0];
    }

    public void setInterfaces(CtClass[] list) throws RuntimeException {
        checkModify();
    }

    public void addInterface(CtClass anInterface) throws RuntimeException {
        checkModify();
    }

    public CtClass getDeclaringClass() throws NotFoundException {
        return null;
    }

    public final CtMethod getEnclosingMethod() throws NotFoundException {
        CtBehavior b = getEnclosingBehavior();
        if (b == null) {
            return null;
        }
        if (b instanceof CtMethod) {
            return (CtMethod) b;
        }
        throw new NotFoundException(b.getLongName() + " is enclosing " + getName());
    }

    public CtBehavior getEnclosingBehavior() throws NotFoundException {
        return null;
    }

    public CtClass makeNestedClass(String name, boolean isStatic) {
        throw new RuntimeException(getName() + " is not a class");
    }

    public CtField[] getFields() {
        return new CtField[0];
    }

    public CtField getField(String name) throws NotFoundException {
        return getField(name, null);
    }

    public CtField getField(String name, String desc) throws NotFoundException {
        throw new NotFoundException(name);
    }

    CtField getField2(String name, String desc) {
        return null;
    }

    public CtField[] getDeclaredFields() {
        return new CtField[0];
    }

    public CtField getDeclaredField(String name) throws NotFoundException {
        throw new NotFoundException(name);
    }

    public CtField getDeclaredField(String name, String desc) throws NotFoundException {
        throw new NotFoundException(name);
    }

    public CtBehavior[] getDeclaredBehaviors() {
        return new CtBehavior[0];
    }

    public CtConstructor[] getConstructors() {
        return new CtConstructor[0];
    }

    public CtConstructor getConstructor(String desc) throws NotFoundException {
        throw new NotFoundException("no such constructor");
    }

    public CtConstructor[] getDeclaredConstructors() {
        return new CtConstructor[0];
    }

    public CtConstructor getDeclaredConstructor(CtClass[] params) throws NotFoundException {
        String desc = Descriptor.ofConstructor(params);
        return getConstructor(desc);
    }

    public CtConstructor getClassInitializer() {
        return null;
    }

    public CtMethod[] getMethods() {
        return new CtMethod[0];
    }

    public CtMethod getMethod(String name, String desc) throws NotFoundException {
        throw new NotFoundException(name);
    }

    public CtMethod[] getDeclaredMethods() {
        return new CtMethod[0];
    }

    public CtMethod getDeclaredMethod(String name, CtClass[] params) throws NotFoundException {
        throw new NotFoundException(name);
    }

    public CtMethod[] getDeclaredMethods(String name) throws NotFoundException {
        throw new NotFoundException(name);
    }

    public CtMethod getDeclaredMethod(String name) throws NotFoundException {
        throw new NotFoundException(name);
    }

    public CtConstructor makeClassInitializer() throws CannotCompileException {
        throw new CannotCompileException("not a class");
    }

    public void addConstructor(CtConstructor c) throws RuntimeException, CannotCompileException {
        checkModify();
    }

    public void removeConstructor(CtConstructor c) throws NotFoundException, RuntimeException {
        checkModify();
    }

    public void addMethod(CtMethod m) throws RuntimeException, CannotCompileException {
        checkModify();
    }

    public void removeMethod(CtMethod m) throws NotFoundException, RuntimeException {
        checkModify();
    }

    public void addField(CtField f) throws RuntimeException, CannotCompileException {
        addField(f, (CtField.Initializer) null);
    }

    public void addField(CtField f, String init) throws RuntimeException, CannotCompileException {
        checkModify();
    }

    public void addField(CtField f, CtField.Initializer init) throws RuntimeException, CannotCompileException {
        checkModify();
    }

    public void removeField(CtField f) throws NotFoundException, RuntimeException {
        checkModify();
    }

    public byte[] getAttribute(String name) {
        return null;
    }

    public void setAttribute(String name, byte[] data) throws RuntimeException {
        checkModify();
    }

    public void instrument(CodeConverter converter) throws RuntimeException, CannotCompileException {
        checkModify();
    }

    public void instrument(ExprEditor editor) throws RuntimeException, CannotCompileException {
        checkModify();
    }

    public Class toClass() throws CannotCompileException {
        return getClassPool().toClass(this);
    }

    public Class toClass(ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
        ClassPool cp = getClassPool();
        if (loader == null) {
            loader = cp.getClassLoader();
        }
        return cp.toClass(this, loader, domain);
    }

    public final Class toClass(ClassLoader loader) throws CannotCompileException {
        return getClassPool().toClass(this, loader);
    }

    public void detach() {
        ClassPool cp = getClassPool();
        CtClass obj = cp.removeCached(getName());
        if (obj != this) {
            cp.cacheCtClass(getName(), obj, false);
        }
    }

    public boolean stopPruning(boolean stop) {
        return true;
    }

    public void prune() {
    }

    void incGetCounter() {
    }

    public void rebuildClassFile() {
    }

    public byte[] toBytecode() throws IOException, CannotCompileException {
        ByteArrayOutputStream barray = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(barray);
        try {
            toBytecode(out);
            return barray.toByteArray();
        } finally {
            out.close();
        }
    }

    public void writeFile() throws IOException, NotFoundException, CannotCompileException {
        writeFile(".");
    }

    public void writeFile(String directoryName) throws IOException, CannotCompileException {
        DataOutputStream out = makeFileOutput(directoryName);
        try {
            toBytecode(out);
        } finally {
            out.close();
        }
    }

    protected DataOutputStream makeFileOutput(String directoryName) {
        String classname = getName();
        String filename = directoryName + File.separatorChar + classname.replace('.', File.separatorChar) + ClassUtils.CLASS_FILE_SUFFIX;
        int pos = filename.lastIndexOf(File.separatorChar);
        if (pos > 0) {
            String dir = filename.substring(0, pos);
            if (!dir.equals(".")) {
                new File(dir).mkdirs();
            }
        }
        return new DataOutputStream(new BufferedOutputStream(new DelayedFileOutputStream(filename)));
    }

    public void debugWriteFile() {
        debugWriteFile(".");
    }

    public void debugWriteFile(String directoryName) {
        try {
            boolean p = stopPruning(true);
            writeFile(directoryName);
            defrost();
            stopPruning(p);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/CtClass$DelayedFileOutputStream.class */
    static class DelayedFileOutputStream extends OutputStream {
        private FileOutputStream file = null;
        private String filename;

        DelayedFileOutputStream(String name) {
            this.filename = name;
        }

        private void init() throws IOException {
            if (this.file == null) {
                this.file = new FileOutputStream(this.filename);
            }
        }

        @Override // java.io.OutputStream
        public void write(int b) throws IOException {
            init();
            this.file.write(b);
        }

        @Override // java.io.OutputStream
        public void write(byte[] b) throws IOException {
            init();
            this.file.write(b);
        }

        @Override // java.io.OutputStream
        public void write(byte[] b, int off, int len) throws IOException {
            init();
            this.file.write(b, off, len);
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            init();
            this.file.flush();
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            init();
            this.file.close();
        }
    }

    public void toBytecode(DataOutputStream out) throws IOException, CannotCompileException {
        throw new CannotCompileException("not a class");
    }

    public String makeUniqueName(String prefix) {
        throw new RuntimeException("not available in " + getName());
    }

    void compress() {
    }
}
