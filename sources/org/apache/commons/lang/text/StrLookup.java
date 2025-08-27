package org.apache.commons.lang.text;

import java.util.Map;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/text/StrLookup.class */
public abstract class StrLookup {
    private static final StrLookup NONE_LOOKUP = new MapStrLookup(null);
    private static final StrLookup SYSTEM_PROPERTIES_LOOKUP;

    public abstract String lookup(String str);

    static {
        StrLookup lookup;
        try {
            lookup = new MapStrLookup(System.getProperties());
        } catch (SecurityException e) {
            lookup = NONE_LOOKUP;
        }
        SYSTEM_PROPERTIES_LOOKUP = lookup;
    }

    public static StrLookup noneLookup() {
        return NONE_LOOKUP;
    }

    public static StrLookup systemPropertiesLookup() {
        return SYSTEM_PROPERTIES_LOOKUP;
    }

    public static StrLookup mapLookup(Map map) {
        return new MapStrLookup(map);
    }

    protected StrLookup() {
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/text/StrLookup$MapStrLookup.class */
    static class MapStrLookup extends StrLookup {
        private final Map map;

        MapStrLookup(Map map) {
            this.map = map;
        }

        @Override // org.apache.commons.lang.text.StrLookup
        public String lookup(String key) {
            Object obj;
            if (this.map == null || (obj = this.map.get(key)) == null) {
                return null;
            }
            return obj.toString();
        }
    }
}
