package org.aspectj.weaver.bcel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.util.FileUtil;
import org.aspectj.weaver.IUnwovenClassFile;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/UnwovenClassFile.class */
public class UnwovenClassFile implements IUnwovenClassFile {
    protected String filename;
    protected char[] charfilename;
    protected byte[] bytes;
    protected List<ChildClass> writtenChildClasses;
    protected String className;

    public UnwovenClassFile(String filename, byte[] bytes) {
        this.writtenChildClasses = Collections.emptyList();
        this.className = null;
        this.filename = filename;
        this.bytes = bytes;
    }

    public UnwovenClassFile(String filename, String classname, byte[] bytes) {
        this.writtenChildClasses = Collections.emptyList();
        this.className = null;
        this.filename = filename;
        this.className = classname;
        this.bytes = bytes;
    }

    @Override // org.aspectj.weaver.IUnwovenClassFile
    public String getFilename() {
        return this.filename;
    }

    public String makeInnerFileName(String innerName) {
        String prefix = this.filename.substring(0, this.filename.length() - 6);
        return prefix + PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX + innerName + ClassUtils.CLASS_FILE_SUFFIX;
    }

    @Override // org.aspectj.weaver.IUnwovenClassFile
    public byte[] getBytes() {
        return this.bytes;
    }

    public JavaClass getJavaClass() {
        if (getBytes() == null) {
            System.out.println("no bytes for: " + getFilename());
            Thread.dumpStack();
        }
        return Utility.makeJavaClass(this.filename, getBytes());
    }

    public void writeUnchangedBytes() throws IOException {
        writeWovenBytes(getBytes(), Collections.emptyList());
    }

    public void writeWovenBytes(byte[] bytes, List<ChildClass> childClasses) throws IOException {
        writeChildClasses(childClasses);
        BufferedOutputStream os = FileUtil.makeOutputStream(new File(this.filename));
        os.write(bytes);
        os.close();
    }

    private void writeChildClasses(List<ChildClass> childClasses) throws IOException {
        deleteAllChildClasses();
        childClasses.removeAll(this.writtenChildClasses);
        for (ChildClass childClass : childClasses) {
            writeChildClassFile(childClass.name, childClass.bytes);
        }
        this.writtenChildClasses = childClasses;
    }

    private void writeChildClassFile(String innerName, byte[] bytes) throws IOException {
        BufferedOutputStream os = FileUtil.makeOutputStream(new File(makeInnerFileName(innerName)));
        os.write(bytes);
        os.close();
    }

    protected void deleteAllChildClasses() {
        for (ChildClass childClass : this.writtenChildClasses) {
            deleteChildClassFile(childClass.name);
        }
    }

    protected void deleteChildClassFile(String innerName) {
        File childClassFile = new File(makeInnerFileName(innerName));
        childClassFile.delete();
    }

    static boolean unchanged(byte[] b1, byte[] b2) {
        int len = b1.length;
        if (b2.length != len) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (b1[i] != b2[i]) {
                return false;
            }
        }
        return true;
    }

    @Override // org.aspectj.weaver.IUnwovenClassFile
    public char[] getClassNameAsChars() {
        if (this.charfilename == null) {
            this.charfilename = getClassName().replace('.', '/').toCharArray();
        }
        return this.charfilename;
    }

    @Override // org.aspectj.weaver.IUnwovenClassFile
    public String getClassName() {
        if (this.className == null) {
            this.className = getJavaClass().getClassName();
        }
        return this.className;
    }

    public String toString() {
        return "UnwovenClassFile(" + this.filename + ", " + getClassName() + ")";
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/UnwovenClassFile$ChildClass.class */
    public static class ChildClass {
        public final String name;
        public final byte[] bytes;

        ChildClass(String name, byte[] bytes) {
            this.name = name;
            this.bytes = bytes;
        }

        public boolean equals(Object other) {
            if (!(other instanceof ChildClass)) {
                return false;
            }
            ChildClass o = (ChildClass) other;
            return o.name.equals(this.name) && UnwovenClassFile.unchanged(o.bytes, this.bytes);
        }

        public int hashCode() {
            return this.name.hashCode();
        }

        public String toString() {
            return "(ChildClass " + this.name + ")";
        }
    }

    public void setClassNameAsChars(char[] classNameAsChars) {
        this.charfilename = classNameAsChars;
    }
}
