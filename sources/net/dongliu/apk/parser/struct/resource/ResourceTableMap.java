package net.dongliu.apk.parser.struct.resource;

import net.dongliu.apk.parser.struct.ResourceValue;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/ResourceTableMap.class */
public class ResourceTableMap {
    private long nameRef;
    private ResourceValue resValue;
    private String data;

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/ResourceTableMap$AttributeType.class */
    public static class AttributeType {
        public static final int ANY = 65535;
        public static final int REFERENCE = 1;
        public static final int STRING = 2;
        public static final int INTEGER = 4;
        public static final int BOOLEAN = 8;
        public static final int COLOR = 16;
        public static final int FLOAT = 32;
        public static final int DIMENSION = 64;
        public static final int FRACTION = 128;
        public static final int ENUM = 65536;
        public static final int FLAGS = 131072;
    }

    public long getNameRef() {
        return this.nameRef;
    }

    public void setNameRef(long nameRef) {
        this.nameRef = nameRef;
    }

    public ResourceValue getResValue() {
        return this.resValue;
    }

    public void setResValue(ResourceValue resValue) {
        this.resValue = resValue;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String toString() {
        return this.data;
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/ResourceTableMap$MapAttr.class */
    public static class MapAttr {
        public static final int TYPE = 16777216;
        public static final int MIN = 16777217;
        public static final int MAX = 16777218;
        public static final int L10N = 16777219;
        public static final int OTHER = 16777220;
        public static final int ZERO = 16777221;
        public static final int ONE = 16777222;
        public static final int TWO = 16777223;
        public static final int FEW = 16777224;
        public static final int MANY = 16777225;

        public static int makeArray(int entry) {
            return 33554432 | (entry & 65535);
        }
    }
}
