package org.hyperic.sigar.win32;

import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/FileVersion.class */
public class FileVersion {
    private int product_major;
    private int product_minor;
    private int product_build;
    private int product_revision;
    private int file_major;
    private int file_minor;
    private int file_build;
    private int file_revision;
    private Map string_file_info = new LinkedHashMap();

    native boolean gather(String str);

    FileVersion() {
    }

    public int getProductMajor() {
        return this.product_major;
    }

    public int getProductMinor() {
        return this.product_minor;
    }

    public int getProductBuild() {
        return this.product_build;
    }

    public int getProductRevision() {
        return this.product_revision;
    }

    public int getFileMajor() {
        return this.file_major;
    }

    public int getFileMinor() {
        return this.file_minor;
    }

    public int getFileBuild() {
        return this.file_build;
    }

    public int getFileRevision() {
        return this.file_revision;
    }

    public Map getInfo() {
        return this.string_file_info;
    }

    private String toVersion(int major, int minor, int build, int revision) {
        return new StringBuffer().append(major).append(".").append(minor).append(".").append(build).append(".").append(revision).toString();
    }

    public String getProductVersion() {
        return toVersion(this.product_major, this.product_minor, this.product_build, this.product_revision);
    }

    public String getFileVersion() {
        return toVersion(this.file_major, this.file_minor, this.file_build, this.file_revision);
    }
}
