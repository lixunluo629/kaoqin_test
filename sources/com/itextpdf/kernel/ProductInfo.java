package com.itextpdf.kernel;

import java.io.Serializable;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/ProductInfo.class */
public class ProductInfo implements Serializable {
    private static final long serialVersionUID = 2410734474798313936L;
    private String name;
    private int major;
    private int minor;
    private int patch;
    private boolean snapshot;

    public ProductInfo(String name, int major, int minor, int patch, boolean snapshot) {
        this.name = name;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.snapshot = snapshot;
    }

    public String getName() {
        return this.name;
    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    public int getPatch() {
        return this.patch;
    }

    public boolean isSnapshot() {
        return this.snapshot;
    }

    public String toString() {
        return this.name + "-" + this.major + "." + this.minor + "." + this.patch + (this.snapshot ? "-SNAPSHOT" : "");
    }
}
