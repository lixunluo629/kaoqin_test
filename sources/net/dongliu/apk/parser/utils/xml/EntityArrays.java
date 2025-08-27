package net.dongliu.apk.parser.utils.xml;

import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/utils/xml/EntityArrays.class */
public class EntityArrays {
    private static final String[][] BASIC_ESCAPE = {new String[]{SymbolConstants.QUOTES_SYMBOL, "&quot;"}, new String[]{"&", "&amp;"}, new String[]{"<", "&lt;"}, new String[]{">", "&gt;"}};
    private static final String[][] APOS_ESCAPE = {new String[]{"'", "&apos;"}};

    public static String[][] BASIC_ESCAPE() {
        return (String[][]) BASIC_ESCAPE.clone();
    }

    public static String[][] APOS_ESCAPE() {
        return (String[][]) APOS_ESCAPE.clone();
    }
}
