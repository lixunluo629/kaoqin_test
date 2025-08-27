package com.itextpdf.kernel.xmp;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/XMPVersionInfo.class */
public interface XMPVersionInfo {
    int getMajor();

    int getMinor();

    int getMicro();

    int getBuild();

    boolean isDebug();

    String getMessage();
}
