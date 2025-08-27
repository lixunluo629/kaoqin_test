package org.apache.poi.xdgf.util;

import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/util/Util.class */
public class Util {
    public static int countLines(String str) {
        int lines = 1;
        int pos = 0;
        while (true) {
            int iIndexOf = str.indexOf(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, pos) + 1;
            pos = iIndexOf;
            if (iIndexOf != 0) {
                lines++;
            } else {
                return lines;
            }
        }
    }

    public static String sanitizeFilename(String name) {
        return name.replaceAll("[:\\\\/*\"?|<>]", "_");
    }
}
