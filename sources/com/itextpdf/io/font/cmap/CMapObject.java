package com.itextpdf.io.font.cmap;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/cmap/CMapObject.class */
public class CMapObject {
    protected static final int STRING = 1;
    protected static final int HEX_STRING = 2;
    protected static final int NAME = 3;
    protected static final int NUMBER = 4;
    protected static final int LITERAL = 5;
    protected static final int ARRAY = 6;
    protected static final int DICTIONARY = 7;
    protected static final int TOKEN = 8;
    private int type;
    private Object value;

    public CMapObject(int objectType, Object value) {
        this.type = objectType;
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public int getType() {
        return this.type;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isString() {
        return this.type == 1 || this.type == 2;
    }

    public boolean isHexString() {
        return this.type == 2;
    }

    public boolean isName() {
        return this.type == 3;
    }

    public boolean isNumber() {
        return this.type == 4;
    }

    public boolean isLiteral() {
        return this.type == 5;
    }

    public boolean isArray() {
        return this.type == 6;
    }

    public boolean isDictionary() {
        return this.type == 7;
    }

    public boolean isToken() {
        return this.type == 8;
    }

    public String toString() {
        if (this.type == 1 || this.type == 2) {
            byte[] content = (byte[]) this.value;
            StringBuilder str = new StringBuilder(content.length);
            for (byte b : content) {
                str.append((char) (b & 255));
            }
            return str.toString();
        }
        return this.value.toString();
    }

    public byte[] toHexByteArray() {
        if (this.type == 2) {
            return (byte[]) this.value;
        }
        return null;
    }
}
