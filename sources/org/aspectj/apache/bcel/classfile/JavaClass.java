package org.aspectj.apache.bcel.classfile;

import ch.qos.logback.classic.net.SyslogAppender;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeAnnos;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.apache.bcel.util.Repository;
import org.aspectj.apache.bcel.util.SyntheticRepository;
import org.aspectj.weaver.AbstractReferenceTypeDelegate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/JavaClass.class */
public class JavaClass extends Modifiers implements Cloneable, Node {
    private static final String[] NoInterfaceNames = new String[0];
    private static final Field[] NoFields = new Field[0];
    private static final Method[] NoMethod = new Method[0];
    private static final int[] NoInterfaceIndices = new int[0];
    private static final Attribute[] NoAttributes = new Attribute[0];
    private String fileName;
    private String packageName;
    private String sourcefileName;
    private int classnameIdx;
    private int superclassnameIdx;
    private String classname;
    private String superclassname;
    private int major;
    private int minor;
    private ConstantPool cpool;
    private int[] interfaces;
    private String[] interfacenames;
    private Field[] fields;
    private Method[] methods;
    private Attribute[] attributes;
    private AnnotationGen[] annotations;
    private boolean annotationsOutOfDate;
    private boolean isGeneric = false;
    private boolean isAnonymous = false;
    private boolean isNested = false;
    private boolean computedNestedTypeStatus = false;
    private String signatureAttributeString = null;
    private Signature signatureAttribute = null;
    private boolean searchedForSignatureAttribute = false;
    private transient Repository repository = null;

    public JavaClass(int i, int i2, String str, int i3, int i4, int i5, ConstantPool constantPool, int[] iArr, Field[] fieldArr, Method[] methodArr, Attribute[] attributeArr) {
        this.annotationsOutOfDate = true;
        iArr = iArr == null ? NoInterfaceIndices : iArr;
        this.classnameIdx = i;
        this.superclassnameIdx = i2;
        this.fileName = str;
        this.major = i3;
        this.minor = i4;
        this.modifiers = i5;
        this.cpool = constantPool;
        this.interfaces = iArr;
        this.fields = fieldArr == null ? NoFields : fieldArr;
        this.methods = methodArr == null ? NoMethod : methodArr;
        this.attributes = attributeArr == null ? NoAttributes : attributeArr;
        this.annotationsOutOfDate = true;
        SourceFile sourceFileAttribute = AttributeUtils.getSourceFileAttribute(attributeArr);
        this.sourcefileName = sourceFileAttribute == null ? AbstractReferenceTypeDelegate.UNKNOWN_SOURCE_FILE : sourceFileAttribute.getSourceFileName();
        this.classname = constantPool.getConstantString(i, (byte) 7);
        this.classname = Utility.compactClassName(this.classname, false);
        int iLastIndexOf = this.classname.lastIndexOf(46);
        if (iLastIndexOf < 0) {
            this.packageName = "";
        } else {
            this.packageName = this.classname.substring(0, iLastIndexOf);
        }
        if (i2 > 0) {
            this.superclassname = constantPool.getConstantString(i2, (byte) 7);
            this.superclassname = Utility.compactClassName(this.superclassname, false);
        } else {
            this.superclassname = "java.lang.Object";
        }
        if (iArr.length == 0) {
            this.interfacenames = NoInterfaceNames;
            return;
        }
        this.interfacenames = new String[iArr.length];
        for (int i6 = 0; i6 < iArr.length; i6++) {
            this.interfacenames[i6] = Utility.compactClassName(constantPool.getConstantString(iArr[i6], (byte) 7), false);
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitJavaClass(this);
    }

    public void dump(File file) throws IOException {
        String parent = file.getParent();
        if (parent != null) {
            new File(parent).mkdirs();
        }
        dump(new DataOutputStream(new FileOutputStream(file)));
    }

    public void dump(String str) throws IOException {
        dump(new File(str));
    }

    public byte[] getBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            try {
                dump(dataOutputStream);
                try {
                    dataOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Throwable th) {
                try {
                    dataOutputStream.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                throw th;
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            try {
                dataOutputStream.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    public void dump(OutputStream outputStream) throws IOException {
        dump(new DataOutputStream(outputStream));
    }

    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(-889275714);
        dataOutputStream.writeShort(this.minor);
        dataOutputStream.writeShort(this.major);
        this.cpool.dump(dataOutputStream);
        dataOutputStream.writeShort(this.modifiers);
        dataOutputStream.writeShort(this.classnameIdx);
        dataOutputStream.writeShort(this.superclassnameIdx);
        dataOutputStream.writeShort(this.interfaces.length);
        for (int i = 0; i < this.interfaces.length; i++) {
            dataOutputStream.writeShort(this.interfaces[i]);
        }
        dataOutputStream.writeShort(this.fields.length);
        for (int i2 = 0; i2 < this.fields.length; i2++) {
            this.fields[i2].dump(dataOutputStream);
        }
        dataOutputStream.writeShort(this.methods.length);
        for (int i3 = 0; i3 < this.methods.length; i3++) {
            this.methods[i3].dump(dataOutputStream);
        }
        AttributeUtils.writeAttributes(this.attributes, dataOutputStream);
        dataOutputStream.close();
    }

    public Attribute[] getAttributes() {
        return this.attributes;
    }

    public AnnotationGen[] getAnnotations() {
        if (this.annotationsOutOfDate) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.attributes.length; i++) {
                Attribute attribute = this.attributes[i];
                if (attribute instanceof RuntimeAnnos) {
                    arrayList.addAll(((RuntimeAnnos) attribute).getAnnotations());
                }
            }
            this.annotations = (AnnotationGen[]) arrayList.toArray(new AnnotationGen[0]);
            this.annotationsOutOfDate = false;
        }
        return this.annotations;
    }

    public String getClassName() {
        return this.classname;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public int getClassNameIndex() {
        return this.classnameIdx;
    }

    public ConstantPool getConstantPool() {
        return this.cpool;
    }

    public Field[] getFields() {
        return this.fields;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String[] getInterfaceNames() {
        return this.interfacenames;
    }

    public int[] getInterfaceIndices() {
        return this.interfaces;
    }

    public int getMajor() {
        return this.major;
    }

    public Method[] getMethods() {
        return this.methods;
    }

    public Method getMethod(java.lang.reflect.Method method) {
        for (int i = 0; i < this.methods.length; i++) {
            Method method2 = this.methods[i];
            if (method.getName().equals(method2.getName()) && method.getModifiers() == method2.getModifiers() && Type.getSignature(method).equals(method2.getSignature())) {
                return method2;
            }
        }
        return null;
    }

    public Method getMethod(Constructor<?> constructor) {
        for (int i = 0; i < this.methods.length; i++) {
            Method method = this.methods[i];
            if (method.getName().equals("<init>") && constructor.getModifiers() == method.getModifiers() && Type.getSignature(constructor).equals(method.getSignature())) {
                return method;
            }
        }
        return null;
    }

    public Field getField(java.lang.reflect.Field field) {
        String name = field.getName();
        for (Field field2 : this.fields) {
            if (field2.getName().equals(name)) {
                return field2;
            }
        }
        return null;
    }

    public int getMinor() {
        return this.minor;
    }

    public String getSourceFileName() {
        return this.sourcefileName;
    }

    public String getSuperclassName() {
        return this.superclassname;
    }

    public int getSuperclassNameIndex() {
        return this.superclassnameIdx;
    }

    public void setAttributes(Attribute[] attributeArr) {
        this.attributes = attributeArr;
        this.annotationsOutOfDate = true;
    }

    public void setClassName(String str) {
        this.classname = str;
    }

    public void setClassNameIndex(int i) {
        this.classnameIdx = i;
    }

    public void setConstantPool(ConstantPool constantPool) {
        this.cpool = constantPool;
    }

    public void setFields(Field[] fieldArr) {
        this.fields = fieldArr;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public void setInterfaceNames(String[] strArr) {
        this.interfacenames = strArr;
    }

    public void setInterfaces(int[] iArr) {
        this.interfaces = iArr;
    }

    public void setMajor(int i) {
        this.major = i;
    }

    public void setMethods(Method[] methodArr) {
        this.methods = methodArr;
    }

    public void setMinor(int i) {
        this.minor = i;
    }

    public void setSourceFileName(String str) {
        this.sourcefileName = str;
    }

    public void setSuperclassName(String str) {
        this.superclassname = str;
    }

    public void setSuperclassNameIndex(int i) {
        this.superclassnameIdx = i;
    }

    public String toString() {
        String strAccessToString = Utility.accessToString(this.modifiers, true);
        StringBuffer stringBuffer = new StringBuffer((strAccessToString.equals("") ? "" : strAccessToString + SymbolConstants.SPACE_SYMBOL) + Utility.classOrInterface(this.modifiers) + SymbolConstants.SPACE_SYMBOL + this.classname + " extends " + Utility.compactClassName(this.superclassname, false) + '\n');
        int length = this.interfaces.length;
        if (length > 0) {
            stringBuffer.append("implements\t\t");
            for (int i = 0; i < length; i++) {
                stringBuffer.append(this.interfacenames[i]);
                if (i < length - 1) {
                    stringBuffer.append(", ");
                }
            }
            stringBuffer.append('\n');
        }
        stringBuffer.append("filename\t\t" + this.fileName + '\n');
        stringBuffer.append("compiled from\t\t" + this.sourcefileName + '\n');
        stringBuffer.append("compiler version\t" + this.major + "." + this.minor + '\n');
        stringBuffer.append("access flags\t\t" + this.modifiers + '\n');
        stringBuffer.append("constant pool\t\t" + this.cpool.getLength() + " entries\n");
        stringBuffer.append("ACC_SUPER flag\t\t" + isSuper() + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        if (this.attributes.length > 0) {
            stringBuffer.append("\nAttribute(s):\n");
            for (int i2 = 0; i2 < this.attributes.length; i2++) {
                stringBuffer.append(indent(this.attributes[i2]));
            }
        }
        if (this.annotations != null && this.annotations.length > 0) {
            stringBuffer.append("\nAnnotation(s):\n");
            for (int i3 = 0; i3 < this.annotations.length; i3++) {
                stringBuffer.append(indent(this.annotations[i3]));
            }
        }
        if (this.fields.length > 0) {
            stringBuffer.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + this.fields.length + " fields:\n");
            for (int i4 = 0; i4 < this.fields.length; i4++) {
                stringBuffer.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN + this.fields[i4] + '\n');
            }
        }
        if (this.methods.length > 0) {
            stringBuffer.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + this.methods.length + " methods:\n");
            for (int i5 = 0; i5 < this.methods.length; i5++) {
                stringBuffer.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN + this.methods[i5] + '\n');
            }
        }
        return stringBuffer.toString();
    }

    private static final String indent(Object obj) {
        StringTokenizer stringTokenizer = new StringTokenizer(obj.toString(), ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        StringBuffer stringBuffer = new StringBuffer();
        while (stringTokenizer.hasMoreTokens()) {
            stringBuffer.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN + stringTokenizer.nextToken() + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        return stringBuffer.toString();
    }

    public final boolean isSuper() {
        return (this.modifiers & 32) != 0;
    }

    public final boolean isClass() {
        return (this.modifiers & 512) == 0;
    }

    public final boolean isAnonymous() {
        computeNestedTypeStatus();
        return this.isAnonymous;
    }

    public final boolean isNested() {
        computeNestedTypeStatus();
        return this.isNested;
    }

    private final void computeNestedTypeStatus() {
        if (this.computedNestedTypeStatus) {
            return;
        }
        for (int i = 0; i < this.attributes.length; i++) {
            if (this.attributes[i] instanceof InnerClasses) {
                InnerClass[] innerClasses = ((InnerClasses) this.attributes[i]).getInnerClasses();
                for (int i2 = 0; i2 < innerClasses.length; i2++) {
                    if (Utility.compactClassName(this.cpool.getConstantString(innerClasses[i2].getInnerClassIndex(), (byte) 7)).equals(getClassName())) {
                        this.isNested = true;
                        if (innerClasses[i2].getInnerNameIndex() == 0) {
                            this.isAnonymous = true;
                        }
                    }
                }
            }
        }
        this.computedNestedTypeStatus = true;
    }

    public final boolean isAnnotation() {
        return (this.modifiers & 8192) != 0;
    }

    public final boolean isEnum() {
        return (this.modifiers & 16384) != 0;
    }

    public Repository getRepository() {
        if (this.repository == null) {
            this.repository = SyntheticRepository.getInstance();
        }
        return this.repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public final boolean instanceOf(JavaClass javaClass) {
        if (equals(javaClass)) {
            return true;
        }
        for (JavaClass javaClass2 : getSuperClasses()) {
            if (javaClass2.equals(javaClass)) {
                return true;
            }
        }
        if (javaClass.isInterface()) {
            return implementationOf(javaClass);
        }
        return false;
    }

    public boolean implementationOf(JavaClass javaClass) {
        if (!javaClass.isInterface()) {
            throw new IllegalArgumentException(javaClass.getClassName() + " is no interface");
        }
        if (equals(javaClass)) {
            return true;
        }
        Iterator<JavaClass> it = getAllInterfaces().iterator();
        while (it.hasNext()) {
            if (it.next().equals(javaClass)) {
                return true;
            }
        }
        return false;
    }

    public JavaClass getSuperClass() {
        if ("java.lang.Object".equals(getClassName())) {
            return null;
        }
        try {
            return getRepository().loadClass(getSuperclassName());
        } catch (ClassNotFoundException e) {
            System.err.println(e);
            return null;
        }
    }

    public JavaClass[] getSuperClasses() {
        ArrayList arrayList = new ArrayList();
        JavaClass superClass = getSuperClass();
        while (true) {
            JavaClass javaClass = superClass;
            if (javaClass == null) {
                return (JavaClass[]) arrayList.toArray(new JavaClass[arrayList.size()]);
            }
            arrayList.add(javaClass);
            superClass = javaClass.getSuperClass();
        }
    }

    public JavaClass[] getInterfaces() {
        String[] interfaceNames = getInterfaceNames();
        JavaClass[] javaClassArr = new JavaClass[interfaceNames.length];
        for (int i = 0; i < interfaceNames.length; i++) {
            try {
                javaClassArr[i] = getRepository().loadClass(interfaceNames[i]);
            } catch (ClassNotFoundException e) {
                System.err.println(e);
                return null;
            }
        }
        return javaClassArr;
    }

    public Collection<JavaClass> getAllInterfaces() {
        LinkedList linkedList = new LinkedList();
        ArrayList arrayList = new ArrayList();
        linkedList.add(this);
        while (!linkedList.isEmpty()) {
            JavaClass javaClass = (JavaClass) linkedList.remove();
            JavaClass superClass = javaClass.getSuperClass();
            JavaClass[] interfaces = javaClass.getInterfaces();
            if (javaClass.isInterface()) {
                arrayList.add(javaClass);
            } else if (superClass != null) {
                linkedList.add(superClass);
            }
            for (JavaClass javaClass2 : interfaces) {
                linkedList.add(javaClass2);
            }
        }
        return arrayList;
    }

    public final String getGenericSignature() {
        loadGenericSignatureInfoIfNecessary();
        return this.signatureAttributeString;
    }

    public boolean isGeneric() {
        loadGenericSignatureInfoIfNecessary();
        return this.isGeneric;
    }

    private void loadGenericSignatureInfoIfNecessary() {
        if (this.searchedForSignatureAttribute) {
            return;
        }
        this.signatureAttribute = AttributeUtils.getSignatureAttribute(this.attributes);
        this.signatureAttributeString = this.signatureAttribute == null ? null : this.signatureAttribute.getSignature();
        this.isGeneric = this.signatureAttribute != null && this.signatureAttributeString.charAt(0) == '<';
        this.searchedForSignatureAttribute = true;
    }

    public final Signature getSignatureAttribute() {
        loadGenericSignatureInfoIfNecessary();
        return this.signatureAttribute;
    }
}
