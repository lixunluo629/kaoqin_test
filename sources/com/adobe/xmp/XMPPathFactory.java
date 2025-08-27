package com.adobe.xmp;

import com.adobe.xmp.impl.Utils;
import com.adobe.xmp.impl.xpath.XMPPath;
import com.adobe.xmp.impl.xpath.XMPPathParser;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/XMPPathFactory.class */
public final class XMPPathFactory {
    private XMPPathFactory() {
    }

    public static String composeArrayItemPath(String str, int i) throws XMPException {
        if (i > 0) {
            return str + '[' + i + ']';
        }
        if (i == -1) {
            return str + "[last()]";
        }
        throw new XMPException("Array index must be larger than zero", 104);
    }

    public static String composeStructFieldPath(String str, String str2) throws XMPException {
        assertFieldNS(str);
        assertFieldName(str2);
        XMPPath xMPPathExpandXPath = XMPPathParser.expandXPath(str, str2);
        if (xMPPathExpandXPath.size() != 2) {
            throw new XMPException("The field name must be simple", 102);
        }
        return '/' + xMPPathExpandXPath.getSegment(1).getName();
    }

    public static String composeQualifierPath(String str, String str2) throws XMPException {
        assertQualNS(str);
        assertQualName(str2);
        XMPPath xMPPathExpandXPath = XMPPathParser.expandXPath(str, str2);
        if (xMPPathExpandXPath.size() != 2) {
            throw new XMPException("The qualifier name must be simple", 102);
        }
        return "/?" + xMPPathExpandXPath.getSegment(1).getName();
    }

    public static String composeLangSelector(String str, String str2) {
        return str + "[?xml:lang=\"" + Utils.normalizeLangValue(str2) + "\"]";
    }

    public static String composeFieldSelector(String str, String str2, String str3, String str4) throws XMPException {
        XMPPath xMPPathExpandXPath = XMPPathParser.expandXPath(str2, str3);
        if (xMPPathExpandXPath.size() != 2) {
            throw new XMPException("The fieldName name must be simple", 102);
        }
        return str + '[' + xMPPathExpandXPath.getSegment(1).getName() + "=\"" + str4 + "\"]";
    }

    private static void assertQualNS(String str) throws XMPException {
        if (str == null || str.length() == 0) {
            throw new XMPException("Empty qualifier namespace URI", 101);
        }
    }

    private static void assertQualName(String str) throws XMPException {
        if (str == null || str.length() == 0) {
            throw new XMPException("Empty qualifier name", 102);
        }
    }

    private static void assertFieldNS(String str) throws XMPException {
        if (str == null || str.length() == 0) {
            throw new XMPException("Empty field namespace URI", 101);
        }
    }

    private static void assertFieldName(String str) throws XMPException {
        if (str == null || str.length() == 0) {
            throw new XMPException("Empty f name", 102);
        }
    }
}
