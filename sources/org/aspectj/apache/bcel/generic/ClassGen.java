package org.aspectj.apache.bcel.generic;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.ibatis.javassist.bytecode.SourceFileAttribute;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.Field;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.Method;
import org.aspectj.apache.bcel.classfile.Modifiers;
import org.aspectj.apache.bcel.classfile.SourceFile;
import org.aspectj.apache.bcel.classfile.Utility;
import org.aspectj.apache.bcel.classfile.annotation.AnnotationGen;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisAnnos;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeVisAnnos;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/ClassGen.class */
public class ClassGen extends Modifiers implements Cloneable {
    private String classname;
    private String superclassname;
    private String filename;
    private int classnameIndex;
    private int superclassnameIndex;
    private int major;
    private int minor;
    private ConstantPool cpool;
    private List<Field> fieldsList;
    private List<Method> methodsList;
    private List<Attribute> attributesList;
    private List<String> interfaceList;
    private List<AnnotationGen> annotationsList;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/ClassGen$ConstructorComparator.class */
    private static class ConstructorComparator implements Comparator<Method> {
        private ConstructorComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Method method, Method method2) {
            return method.getSignature().compareTo(method2.getSignature());
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/ClassGen$FieldComparator.class */
    private static class FieldComparator implements Comparator<Field> {
        private FieldComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Field field, Field field2) {
            return field.getName().compareTo(field2.getName());
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/ClassGen$MethodComparator.class */
    private static class MethodComparator implements Comparator<Method> {
        private MethodComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Method method, Method method2) {
            int iCompareTo = method.getName().compareTo(method2.getName());
            if (iCompareTo == 0) {
                iCompareTo = method.getSignature().compareTo(method2.getSignature());
            }
            return iCompareTo;
        }
    }

    public ClassGen(String str, String str2, String str3, int i, String[] strArr, ConstantPool constantPool) {
        this.classnameIndex = -1;
        this.superclassnameIndex = -1;
        this.major = 45;
        this.minor = 3;
        this.fieldsList = new ArrayList();
        this.methodsList = new ArrayList();
        this.attributesList = new ArrayList();
        this.interfaceList = new ArrayList();
        this.annotationsList = new ArrayList();
        this.classname = str;
        this.superclassname = str2;
        this.filename = str3;
        this.modifiers = i;
        this.cpool = constantPool;
        if (str3 != null) {
            addAttribute(new SourceFile(constantPool.addUtf8(SourceFileAttribute.tag), 2, constantPool.addUtf8(str3), constantPool));
        }
        this.classnameIndex = constantPool.addClass(str);
        this.superclassnameIndex = constantPool.addClass(str2);
        if (strArr != null) {
            for (String str4 : strArr) {
                addInterface(str4);
            }
        }
    }

    public ClassGen(String str, String str2, String str3, int i, String[] strArr) {
        this(str, str2, str3, i, strArr, new ConstantPool());
    }

    public ClassGen(JavaClass javaClass) {
        this.classnameIndex = -1;
        this.superclassnameIndex = -1;
        this.major = 45;
        this.minor = 3;
        this.fieldsList = new ArrayList();
        this.methodsList = new ArrayList();
        this.attributesList = new ArrayList();
        this.interfaceList = new ArrayList();
        this.annotationsList = new ArrayList();
        this.classnameIndex = javaClass.getClassNameIndex();
        this.superclassnameIndex = javaClass.getSuperclassNameIndex();
        this.classname = javaClass.getClassName();
        this.superclassname = javaClass.getSuperclassName();
        this.filename = javaClass.getSourceFileName();
        this.modifiers = javaClass.getModifiers();
        this.cpool = javaClass.getConstantPool().copy();
        this.major = javaClass.getMajor();
        this.minor = javaClass.getMinor();
        Method[] methods = javaClass.getMethods();
        Field[] fields = javaClass.getFields();
        for (String str : javaClass.getInterfaceNames()) {
            addInterface(str);
        }
        for (Attribute attribute : javaClass.getAttributes()) {
            if (attribute instanceof RuntimeVisAnnos) {
                Iterator<AnnotationGen> it = ((RuntimeVisAnnos) attribute).getAnnotations().iterator();
                while (it.hasNext()) {
                    this.annotationsList.add(new AnnotationGen(it.next(), this.cpool, false));
                }
            } else if (attribute instanceof RuntimeInvisAnnos) {
                Iterator<AnnotationGen> it2 = ((RuntimeInvisAnnos) attribute).getAnnotations().iterator();
                while (it2.hasNext()) {
                    this.annotationsList.add(new AnnotationGen(it2.next(), this.cpool, false));
                }
            } else {
                this.attributesList.add(attribute);
            }
        }
        for (Method method : methods) {
            addMethod(method);
        }
        for (Field field : fields) {
            addField(field);
        }
    }

    public JavaClass getJavaClass() {
        Collection arrayList;
        int[] interfaces = getInterfaces();
        Field[] fields = getFields();
        Method[] methods = getMethods();
        if (this.annotationsList.size() == 0) {
            arrayList = this.attributesList;
        } else {
            arrayList = new ArrayList();
            arrayList.addAll(Utility.getAnnotationAttributes(this.cpool, this.annotationsList));
            arrayList.addAll(this.attributesList);
        }
        return new JavaClass(this.classnameIndex, this.superclassnameIndex, this.filename, this.major, this.minor, this.modifiers, this.cpool.getFinalConstantPool(), interfaces, fields, methods, (Attribute[]) arrayList.toArray(new Attribute[arrayList.size()]));
    }

    public void addInterface(String str) {
        this.interfaceList.add(str);
    }

    public void removeInterface(String str) {
        this.interfaceList.remove(str);
    }

    public int getMajor() {
        return this.major;
    }

    public void setMajor(int i) {
        this.major = i;
    }

    public void setMinor(int i) {
        this.minor = i;
    }

    public int getMinor() {
        return this.minor;
    }

    public void addAttribute(Attribute attribute) {
        this.attributesList.add(attribute);
    }

    public void addAnnotation(AnnotationGen annotationGen) {
        this.annotationsList.add(annotationGen);
    }

    public void addMethod(Method method) {
        this.methodsList.add(method);
    }

    public void addEmptyConstructor(int i) {
        InstructionList instructionList = new InstructionList();
        instructionList.append(InstructionConstants.THIS);
        instructionList.append(new InvokeInstruction((short) 183, this.cpool.addMethodref(this.superclassname, "<init>", "()V")));
        instructionList.append(InstructionConstants.RETURN);
        MethodGen methodGen = new MethodGen(i, Type.VOID, Type.NO_ARGS, null, "<init>", this.classname, instructionList, this.cpool);
        methodGen.setMaxStack(1);
        methodGen.setMaxLocals();
        addMethod(methodGen.getMethod());
    }

    public void addField(Field field) {
        this.fieldsList.add(field);
    }

    public boolean containsField(Field field) {
        return this.fieldsList.contains(field);
    }

    public Field containsField(String str) {
        for (Field field : this.fieldsList) {
            if (field.getName().equals(str)) {
                return field;
            }
        }
        return null;
    }

    public Method containsMethod(String str, String str2) {
        for (Method method : this.methodsList) {
            if (method.getName().equals(str) && method.getSignature().equals(str2)) {
                return method;
            }
        }
        return null;
    }

    public void removeAttribute(Attribute attribute) {
        this.attributesList.remove(attribute);
    }

    public void removeAnnotation(AnnotationGen annotationGen) {
        this.annotationsList.remove(annotationGen);
    }

    public void removeMethod(Method method) {
        this.methodsList.remove(method);
    }

    public void replaceMethod(Method method, Method method2) {
        if (method2 == null) {
            throw new ClassGenException("Replacement method must not be null");
        }
        int iIndexOf = this.methodsList.indexOf(method);
        if (iIndexOf < 0) {
            this.methodsList.add(method2);
        } else {
            this.methodsList.set(iIndexOf, method2);
        }
    }

    public void replaceField(Field field, Field field2) {
        if (field2 == null) {
            throw new ClassGenException("Replacement method must not be null");
        }
        int iIndexOf = this.fieldsList.indexOf(field);
        if (iIndexOf < 0) {
            this.fieldsList.add(field2);
        } else {
            this.fieldsList.set(iIndexOf, field2);
        }
    }

    public void removeField(Field field) {
        this.fieldsList.remove(field);
    }

    public String getClassName() {
        return this.classname;
    }

    public String getSuperclassName() {
        return this.superclassname;
    }

    public String getFileName() {
        return this.filename;
    }

    public void setClassName(String str) {
        this.classname = str.replace('/', '.');
        this.classnameIndex = this.cpool.addClass(str);
    }

    public void setSuperclassName(String str) {
        this.superclassname = str.replace('/', '.');
        this.superclassnameIndex = this.cpool.addClass(str);
    }

    public Method[] getMethods() {
        Method[] methodArr = new Method[this.methodsList.size()];
        this.methodsList.toArray(methodArr);
        return methodArr;
    }

    public void setMethods(Method[] methodArr) {
        this.methodsList.clear();
        for (Method method : methodArr) {
            addMethod(method);
        }
    }

    public void setFields(Field[] fieldArr) {
        this.fieldsList.clear();
        for (Field field : fieldArr) {
            addField(field);
        }
    }

    public void setMethodAt(Method method, int i) {
        this.methodsList.set(i, method);
    }

    public Method getMethodAt(int i) {
        return this.methodsList.get(i);
    }

    public String[] getInterfaceNames() {
        String[] strArr = new String[this.interfaceList.size()];
        this.interfaceList.toArray(strArr);
        return strArr;
    }

    public int[] getInterfaces() {
        int size = this.interfaceList.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = this.cpool.addClass(this.interfaceList.get(i));
        }
        return iArr;
    }

    public Field[] getFields() {
        Field[] fieldArr = new Field[this.fieldsList.size()];
        this.fieldsList.toArray(fieldArr);
        return fieldArr;
    }

    public Collection<Attribute> getAttributes() {
        return this.attributesList;
    }

    public AnnotationGen[] getAnnotations() {
        AnnotationGen[] annotationGenArr = new AnnotationGen[this.annotationsList.size()];
        this.annotationsList.toArray(annotationGenArr);
        return annotationGenArr;
    }

    public ConstantPool getConstantPool() {
        return this.cpool;
    }

    public void setConstantPool(ConstantPool constantPool) {
        this.cpool = constantPool;
    }

    public void setClassNameIndex(int i) {
        this.classnameIndex = i;
        this.classname = this.cpool.getConstantString(i, (byte) 7).replace('/', '.');
    }

    public void setSuperclassNameIndex(int i) {
        this.superclassnameIndex = i;
        this.superclassname = this.cpool.getConstantString(i, (byte) 7).replace('/', '.');
    }

    public int getSuperclassNameIndex() {
        return this.superclassnameIndex;
    }

    public int getClassNameIndex() {
        return this.classnameIndex;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println(e);
            return null;
        }
    }

    public final boolean isAnnotation() {
        return (this.modifiers & 8192) != 0;
    }

    public final boolean isEnum() {
        return (this.modifiers & 16384) != 0;
    }

    public long getSUID() throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF(getClassName());
            int i = 0 | (isPublic() ? 1 : 0) | (isFinal() ? 16 : 0) | (isInterface() ? 512 : 0);
            if (isAbstract() && (!isInterface() || this.methodsList.size() > 0)) {
                i |= 1024;
            }
            dataOutputStream.writeInt(i);
            String[] interfaceNames = getInterfaceNames();
            if (interfaceNames != null) {
                Arrays.sort(interfaceNames);
                for (String str : interfaceNames) {
                    dataOutputStream.writeUTF(str);
                }
            }
            ArrayList<Field> arrayList = new ArrayList();
            for (Field field : this.fieldsList) {
                if ((!field.isPrivate() || !field.isStatic()) && (!field.isPrivate() || !field.isTransient())) {
                    arrayList.add(field);
                }
            }
            Collections.sort(arrayList, new FieldComparator());
            for (Field field2 : arrayList) {
                dataOutputStream.writeUTF(field2.getName());
                dataOutputStream.writeInt(223 & field2.getModifiers());
                dataOutputStream.writeUTF(field2.getType().getSignature());
            }
            ArrayList<Method> arrayList2 = new ArrayList();
            ArrayList<Method> arrayList3 = new ArrayList();
            boolean z = false;
            for (Method method : this.methodsList) {
                boolean z2 = method.getName().charAt(0) == '<';
                if (z2 && method.getName().equals("<clinit>")) {
                    z = true;
                } else if (z2 && method.getName().equals("<init>")) {
                    if (!method.isPrivate()) {
                        arrayList3.add(method);
                    }
                } else if (!method.isPrivate()) {
                    arrayList2.add(method);
                }
            }
            Collections.sort(arrayList3, new ConstructorComparator());
            Collections.sort(arrayList2, new MethodComparator());
            if (z) {
                dataOutputStream.writeUTF("<clinit>");
                dataOutputStream.writeInt(8);
                dataOutputStream.writeUTF("()V");
            }
            for (Method method2 : arrayList3) {
                dataOutputStream.writeUTF(method2.getName());
                dataOutputStream.writeInt(3391 & method2.getModifiers());
                dataOutputStream.writeUTF(method2.getSignature().replace('/', '.'));
            }
            for (Method method3 : arrayList2) {
                dataOutputStream.writeUTF(method3.getName());
                dataOutputStream.writeInt(3391 & method3.getModifiers());
                dataOutputStream.writeUTF(method3.getSignature().replace('/', '.'));
            }
            dataOutputStream.flush();
            dataOutputStream.close();
            byte[] bArrDigest = MessageDigest.getInstance("SHA").digest(byteArrayOutputStream.toByteArray());
            long j = 0;
            int length = bArrDigest.length > 8 ? 7 : bArrDigest.length - 1;
            while (length >= 0) {
                int i2 = length;
                length--;
                j = (j << 8) | (bArrDigest[i2] & 255);
            }
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to calculate suid for " + getClassName() + ": " + e.toString());
        }
    }

    public boolean hasAttribute(String str) {
        Iterator<Attribute> it = this.attributesList.iterator();
        while (it.hasNext()) {
            if (it.next().getName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public Attribute getAttribute(String str) {
        for (Attribute attribute : this.attributesList) {
            if (attribute.getName().equals(str)) {
                return attribute;
            }
        }
        return null;
    }
}
