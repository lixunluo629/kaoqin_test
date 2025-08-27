package org.apache.tomcat.util.bcel.classfile;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/bcel/classfile/JavaClass.class */
public class JavaClass {
    private final int access_flags;
    private final String class_name;
    private final String superclass_name;
    private final String[] interface_names;
    private final Annotations runtimeVisibleAnnotations;

    JavaClass(String class_name, String superclass_name, int access_flags, ConstantPool constant_pool, String[] interface_names, Annotations runtimeVisibleAnnotations) {
        this.access_flags = access_flags;
        this.runtimeVisibleAnnotations = runtimeVisibleAnnotations;
        this.class_name = class_name;
        this.superclass_name = superclass_name;
        this.interface_names = interface_names;
    }

    public final int getAccessFlags() {
        return this.access_flags;
    }

    public AnnotationEntry[] getAnnotationEntries() {
        if (this.runtimeVisibleAnnotations != null) {
            return this.runtimeVisibleAnnotations.getAnnotationEntries();
        }
        return null;
    }

    public String getClassName() {
        return this.class_name;
    }

    public String[] getInterfaceNames() {
        return this.interface_names;
    }

    public String getSuperclassName() {
        return this.superclass_name;
    }
}
