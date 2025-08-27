package net.dongliu.apk.parser.struct.dex;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/dex/DexClassStruct.class */
public class DexClassStruct {
    private int classIdx;
    private int accessFlags;
    private int superclassIdx;
    private long interfacesOff;
    private int sourceFileIdx;
    private long annotationsOff;
    private long classDataOff;
    private long staticValuesOff;
    public static int ACC_PUBLIC = 1;
    public static int ACC_PRIVATE = 2;
    public static int ACC_PROTECTED = 4;
    public static int ACC_STATIC = 8;
    public static int ACC_FINAL = 16;
    public static int ACC_SYNCHRONIZED = 32;
    public static int ACC_VOLATILE = 64;
    public static int ACC_BRIDGE = 64;
    public static int ACC_TRANSIENT = 128;
    public static int ACC_VARARGS = 128;
    public static int ACC_NATIVE = 256;
    public static int ACC_INTERFACE = 512;
    public static int ACC_ABSTRACT = 1024;
    public static int ACC_STRICT = 2048;
    public static int ACC_SYNTHETIC = 4096;
    public static int ACC_ANNOTATION = 8192;
    public static int ACC_ENUM = 16384;
    public static int ACC_CONSTRUCTOR = 65536;
    public static int ACC_DECLARED_SYNCHRONIZED = 131072;

    public int getClassIdx() {
        return this.classIdx;
    }

    public void setClassIdx(int classIdx) {
        this.classIdx = classIdx;
    }

    public int getAccessFlags() {
        return this.accessFlags;
    }

    public void setAccessFlags(int accessFlags) {
        this.accessFlags = accessFlags;
    }

    public int getSuperclassIdx() {
        return this.superclassIdx;
    }

    public void setSuperclassIdx(int superclassIdx) {
        this.superclassIdx = superclassIdx;
    }

    public long getInterfacesOff() {
        return this.interfacesOff;
    }

    public void setInterfacesOff(long interfacesOff) {
        this.interfacesOff = interfacesOff;
    }

    public int getSourceFileIdx() {
        return this.sourceFileIdx;
    }

    public void setSourceFileIdx(int sourceFileIdx) {
        this.sourceFileIdx = sourceFileIdx;
    }

    public long getAnnotationsOff() {
        return this.annotationsOff;
    }

    public void setAnnotationsOff(long annotationsOff) {
        this.annotationsOff = annotationsOff;
    }

    public long getClassDataOff() {
        return this.classDataOff;
    }

    public void setClassDataOff(long classDataOff) {
        this.classDataOff = classDataOff;
    }

    public long getStaticValuesOff() {
        return this.staticValuesOff;
    }

    public void setStaticValuesOff(long staticValuesOff) {
        this.staticValuesOff = staticValuesOff;
    }
}
