package org.aspectj.weaver;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/IUnwovenClassFile.class */
public interface IUnwovenClassFile {
    String getFilename();

    String getClassName();

    byte[] getBytes();

    char[] getClassNameAsChars();
}
