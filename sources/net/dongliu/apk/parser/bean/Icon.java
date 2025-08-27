package net.dongliu.apk.parser.bean;

import javax.annotation.Nullable;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/bean/Icon.class */
public class Icon {
    private final String path;
    private int density;
    private final byte[] data;

    public Icon(String path, int density, byte[] data) {
        this.path = path;
        this.density = density;
        this.data = data;
    }

    public String getPath() {
        return this.path;
    }

    public int getDensity() {
        return this.density;
    }

    @Nullable
    public byte[] getData() {
        return this.data;
    }

    public String toString() {
        return "Icon{path='" + this.path + "', density=" + this.density + ", size=" + (this.data == null ? 0 : this.data.length) + '}';
    }
}
