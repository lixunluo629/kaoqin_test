package net.dongliu.apk.parser.bean;

import net.dongliu.apk.parser.struct.dex.DexClassStruct;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/bean/DexClass.class */
public class DexClass {
    private String classType;
    private String superClass;
    private int accessFlags;

    public String getPackageName() {
        String packageName = this.classType;
        if (packageName.length() > 0 && packageName.charAt(0) == 'L') {
            packageName = packageName.substring(1);
        }
        if (packageName.length() > 0) {
            int idx = this.classType.lastIndexOf(47);
            if (idx > 0) {
                packageName = packageName.substring(0, this.classType.lastIndexOf(47) - 1);
            } else if (packageName.charAt(packageName.length() - 1) == ';') {
                packageName = packageName.substring(0, packageName.length() - 1);
            }
        }
        return packageName.replace('/', '.');
    }

    public String getClassType() {
        return this.classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getSuperClass() {
        return this.superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public void setAccessFlags(int accessFlags) {
        this.accessFlags = accessFlags;
    }

    public boolean isInterface() {
        return (this.accessFlags & DexClassStruct.ACC_INTERFACE) != 0;
    }

    public boolean isEnum() {
        return (this.accessFlags & DexClassStruct.ACC_ENUM) != 0;
    }

    public boolean isAnnotation() {
        return (this.accessFlags & DexClassStruct.ACC_ANNOTATION) != 0;
    }

    public boolean isPublic() {
        return (this.accessFlags & DexClassStruct.ACC_PUBLIC) != 0;
    }

    public boolean isProtected() {
        return (this.accessFlags & DexClassStruct.ACC_PROTECTED) != 0;
    }

    public boolean isStatic() {
        return (this.accessFlags & DexClassStruct.ACC_STATIC) != 0;
    }

    public String toString() {
        return this.classType;
    }
}
