package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/FontUnderline.class */
public enum FontUnderline {
    SINGLE(1),
    DOUBLE(2),
    SINGLE_ACCOUNTING(3),
    DOUBLE_ACCOUNTING(4),
    NONE(5);

    private int value;
    private static FontUnderline[] _table = new FontUnderline[6];

    static {
        FontUnderline[] arr$ = values();
        for (FontUnderline c : arr$) {
            _table[c.getValue()] = c;
        }
    }

    FontUnderline(int val) {
        this.value = val;
    }

    public int getValue() {
        return this.value;
    }

    public byte getByteValue() {
        switch (this) {
            case DOUBLE:
                return (byte) 2;
            case DOUBLE_ACCOUNTING:
                return (byte) 34;
            case SINGLE_ACCOUNTING:
                return (byte) 33;
            case NONE:
                return (byte) 0;
            case SINGLE:
                return (byte) 1;
            default:
                return (byte) 1;
        }
    }

    public static FontUnderline valueOf(int value) {
        return _table[value];
    }

    public static FontUnderline valueOf(byte value) {
        FontUnderline val;
        switch (value) {
            case 1:
                val = SINGLE;
                break;
            case 2:
                val = DOUBLE;
                break;
            case 33:
                val = SINGLE_ACCOUNTING;
                break;
            case 34:
                val = DOUBLE_ACCOUNTING;
                break;
            default:
                val = NONE;
                break;
        }
        return val;
    }
}
