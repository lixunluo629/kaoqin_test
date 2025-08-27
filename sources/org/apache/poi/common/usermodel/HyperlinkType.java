package org.apache.poi.common.usermodel;

import java.util.HashMap;
import java.util.Map;
import org.apache.poi.util.Internal;

/* loaded from: poi-3.17.jar:org/apache/poi/common/usermodel/HyperlinkType.class */
public enum HyperlinkType {
    NONE(-1),
    URL(1),
    DOCUMENT(2),
    EMAIL(3),
    FILE(4);


    @Internal(since = "3.15 beta 3")
    @Deprecated
    private final int code;
    private static final Map<Integer, HyperlinkType> map = new HashMap();

    static {
        HyperlinkType[] arr$ = values();
        for (HyperlinkType type : arr$) {
            map.put(Integer.valueOf(type.getCode()), type);
        }
    }

    @Internal(since = "3.15 beta 3")
    @Deprecated
    HyperlinkType(int code) {
        this.code = code;
    }

    @Internal(since = "3.15 beta 3")
    @Deprecated
    public int getCode() {
        return this.code;
    }

    @Internal(since = "3.15 beta 3")
    @Deprecated
    public static HyperlinkType forInt(int code) {
        HyperlinkType type = map.get(Integer.valueOf(code));
        if (type == null) {
            throw new IllegalArgumentException("Invalid type: " + code);
        }
        return type;
    }
}
