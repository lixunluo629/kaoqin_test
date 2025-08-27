package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/FontScheme.class */
public enum FontScheme {
    NONE(1),
    MAJOR(2),
    MINOR(3);

    private int value;
    private static FontScheme[] _table = new FontScheme[4];

    static {
        FontScheme[] arr$ = values();
        for (FontScheme c : arr$) {
            _table[c.getValue()] = c;
        }
    }

    FontScheme(int val) {
        this.value = val;
    }

    public int getValue() {
        return this.value;
    }

    public static FontScheme valueOf(int value) {
        return _table[value];
    }
}
