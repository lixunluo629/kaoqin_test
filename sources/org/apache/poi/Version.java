package org.apache.poi;

import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: poi-3.17.jar:org/apache/poi/Version.class */
public class Version {
    private static final String VERSION_STRING = "3.17";
    private static final String RELEASE_DATE = "20170915";

    public static String getVersion() {
        return VERSION_STRING;
    }

    public static String getReleaseDate() {
        return RELEASE_DATE;
    }

    public static String getProduct() {
        return "POI";
    }

    public static String getImplementationLanguage() {
        return "Java";
    }

    public static void main(String[] args) {
        System.out.println("Apache " + getProduct() + SymbolConstants.SPACE_SYMBOL + getVersion() + " (" + getReleaseDate() + ")");
    }
}
