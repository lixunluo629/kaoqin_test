package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/PrintOrientation.class */
public enum PrintOrientation {
    DEFAULT(1),
    PORTRAIT(2),
    LANDSCAPE(3);

    private int orientation;
    private static PrintOrientation[] _table = new PrintOrientation[4];

    static {
        PrintOrientation[] arr$ = values();
        for (PrintOrientation c : arr$) {
            _table[c.getValue()] = c;
        }
    }

    PrintOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getValue() {
        return this.orientation;
    }

    public static PrintOrientation valueOf(int value) {
        return _table[value];
    }
}
