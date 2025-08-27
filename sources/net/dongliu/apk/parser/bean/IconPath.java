package net.dongliu.apk.parser.bean;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/bean/IconPath.class */
public class IconPath {
    private String path;
    private int density;

    public IconPath(String path, int density) {
        this.path = path;
        this.density = density;
    }

    public String getPath() {
        return this.path;
    }

    public int getDensity() {
        return this.density;
    }

    public String toString() {
        return "IconPath{path='" + this.path + "', density=" + this.density + '}';
    }
}
