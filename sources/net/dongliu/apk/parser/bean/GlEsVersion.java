package net.dongliu.apk.parser.bean;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/bean/GlEsVersion.class */
public class GlEsVersion {
    private int major;
    private int minor;
    private boolean required = true;

    public int getMajor() {
        return this.major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return this.minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public String toString() {
        return this.major + "." + this.minor;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
