package org.apache.poi.ss.usermodel;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/PageOrder.class */
public enum PageOrder {
    DOWN_THEN_OVER(1),
    OVER_THEN_DOWN(2);

    private final int order;
    private static PageOrder[] _table = new PageOrder[3];

    static {
        PageOrder[] arr$ = values();
        for (PageOrder c : arr$) {
            _table[c.getValue()] = c;
        }
    }

    PageOrder(int order) {
        this.order = order;
    }

    public int getValue() {
        return this.order;
    }

    public static PageOrder valueOf(int value) {
        return _table[value];
    }
}
