package org.aspectj.apache.bcel.classfile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ClassParser.class */
public final class ClassParser {
    private DataInputStream file;
    private String filename;
    private int classnameIndex;
    private int superclassnameIndex;
    private int major;
    private int minor;
    private int accessflags;
    private int[] interfaceIndices;
    private ConstantPool cpool;
    private Field[] fields;
    private Method[] methods;
    private Attribute[] attributes;
    private static final int BUFSIZE = 8192;
    private static final int[] NO_INTERFACES = new int[0];

    public ClassParser(InputStream inputStream, String str) {
        this.filename = str;
        if (inputStream instanceof DataInputStream) {
            this.file = (DataInputStream) inputStream;
        } else {
            this.file = new DataInputStream(new BufferedInputStream(inputStream, 8192));
        }
    }

    public ClassParser(ByteArrayInputStream byteArrayInputStream, String str) {
        this.filename = str;
        this.file = new DataInputStream(byteArrayInputStream);
    }

    public ClassParser(String str) throws IOException {
        this.filename = str;
        this.file = new DataInputStream(new BufferedInputStream(new FileInputStream(str), 8192));
    }

    public JavaClass parse() throws IOException, ClassFormatException {
        readID();
        readVersion();
        readConstantPool();
        readClassInfo();
        readInterfaces();
        readFields();
        readMethods();
        readAttributes();
        this.file.close();
        return new JavaClass(this.classnameIndex, this.superclassnameIndex, this.filename, this.major, this.minor, this.accessflags, this.cpool, this.interfaceIndices, this.fields, this.methods, this.attributes);
    }

    private final void readAttributes() {
        this.attributes = AttributeUtils.readAttributes(this.file, this.cpool);
    }

    private final void readClassInfo() throws IOException {
        this.accessflags = this.file.readUnsignedShort();
        if ((this.accessflags & 512) != 0) {
            this.accessflags |= 1024;
        }
        this.classnameIndex = this.file.readUnsignedShort();
        this.superclassnameIndex = this.file.readUnsignedShort();
    }

    private final void readConstantPool() throws IOException {
        try {
            this.cpool = new ConstantPool(this.file);
        } catch (ClassFormatException e) {
            e.printStackTrace();
            if (this.filename == null) {
                throw e;
            }
            throw new ClassFormatException("File: '" + this.filename + "': " + e.getMessage());
        }
    }

    private final void readFields() throws IOException, ClassFormatException {
        int unsignedShort = this.file.readUnsignedShort();
        if (unsignedShort == 0) {
            this.fields = Field.NoFields;
            return;
        }
        this.fields = new Field[unsignedShort];
        for (int i = 0; i < unsignedShort; i++) {
            this.fields[i] = new Field(this.file, this.cpool);
        }
    }

    private final void readID() throws IOException {
        if (this.file.readInt() != -889275714) {
            throw new ClassFormatException(this.filename + " is not a Java .class file");
        }
    }

    private final void readInterfaces() throws IOException {
        int unsignedShort = this.file.readUnsignedShort();
        if (unsignedShort == 0) {
            this.interfaceIndices = NO_INTERFACES;
            return;
        }
        this.interfaceIndices = new int[unsignedShort];
        for (int i = 0; i < unsignedShort; i++) {
            this.interfaceIndices[i] = this.file.readUnsignedShort();
        }
    }

    private final void readMethods() throws IOException {
        int unsignedShort = this.file.readUnsignedShort();
        if (unsignedShort == 0) {
            this.methods = Method.NoMethods;
            return;
        }
        this.methods = new Method[unsignedShort];
        for (int i = 0; i < unsignedShort; i++) {
            this.methods[i] = new Method(this.file, this.cpool);
        }
    }

    private final void readVersion() throws IOException {
        this.minor = this.file.readUnsignedShort();
        this.major = this.file.readUnsignedShort();
    }
}
