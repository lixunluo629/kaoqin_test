package com.adobe.xmp.impl;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.impl.xpath.XMPPath;
import com.adobe.xmp.impl.xpath.XMPPathParser;
import com.adobe.xmp.options.PropertyOptions;
import com.adobe.xmp.properties.XMPAliasInfo;
import com.drew.metadata.exif.makernotes.SonyType1MakernoteDirectory;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Iterator;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/XMPUtilsImpl.class */
public class XMPUtilsImpl implements XMPConst {
    private static final int UCK_NORMAL = 0;
    private static final int UCK_SPACE = 1;
    private static final int UCK_COMMA = 2;
    private static final int UCK_SEMICOLON = 3;
    private static final int UCK_QUOTE = 4;
    private static final int UCK_CONTROL = 5;
    private static final String SPACES = " \u3000〿";
    private static final String COMMAS = ",，､﹐﹑、،՝";
    private static final String SEMICOLA = ";；﹔؛;";
    private static final String QUOTES = "\"«»〝〞〟―‹›";
    private static final String CONTROLS = "\u2028\u2029";
    static final /* synthetic */ boolean $assertionsDisabled;

    private XMPUtilsImpl() {
    }

    public static String catenateArrayItems(XMPMeta xMPMeta, String str, String str2, String str3, String str4, boolean z) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertArrayName(str2);
        ParameterAsserts.assertImplementation(xMPMeta);
        if (str3 == null || str3.length() == 0) {
            str3 = "; ";
        }
        if (str4 == null || str4.length() == 0) {
            str4 = SymbolConstants.QUOTES_SYMBOL;
        }
        XMPNode xMPNodeFindNode = XMPNodeUtils.findNode(((XMPMetaImpl) xMPMeta).getRoot(), XMPPathParser.expandXPath(str, str2), false, null);
        if (xMPNodeFindNode == null) {
            return "";
        }
        if (!xMPNodeFindNode.getOptions().isArray() || xMPNodeFindNode.getOptions().isArrayAlternate()) {
            throw new XMPException("Named property must be non-alternate array", 4);
        }
        checkSeparator(str3);
        char cCharAt = str4.charAt(0);
        char cCheckQuotes = checkQuotes(str4, cCharAt);
        StringBuffer stringBuffer = new StringBuffer();
        Iterator itIterateChildren = xMPNodeFindNode.iterateChildren();
        while (itIterateChildren.hasNext()) {
            XMPNode xMPNode = (XMPNode) itIterateChildren.next();
            if (xMPNode.getOptions().isCompositeProperty()) {
                throw new XMPException("Array items must be simple", 4);
            }
            stringBuffer.append(applyQuotes(xMPNode.getValue(), cCharAt, cCheckQuotes, z));
            if (itIterateChildren.hasNext()) {
                stringBuffer.append(str3);
            }
        }
        return stringBuffer.toString();
    }

    public static void separateArrayItems(XMPMeta xMPMeta, String str, String str2, String str3, PropertyOptions propertyOptions, boolean z) throws XMPException {
        String strSubstring;
        char cCharAt;
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertArrayName(str2);
        if (str3 == null) {
            throw new XMPException("Parameter must not be null", 4);
        }
        ParameterAsserts.assertImplementation(xMPMeta);
        XMPNode xMPNodeSeparateFindCreateArray = separateFindCreateArray(str, str2, propertyOptions, (XMPMetaImpl) xMPMeta);
        int iClassifyCharacter = 0;
        char cCharAt2 = 0;
        int i = 0;
        int length = str3.length();
        while (i < length) {
            int i2 = i;
            while (i2 < length) {
                cCharAt2 = str3.charAt(i2);
                iClassifyCharacter = classifyCharacter(cCharAt2);
                if (iClassifyCharacter == 0 || iClassifyCharacter == 4) {
                    break;
                } else {
                    i2++;
                }
            }
            if (i2 >= length) {
                return;
            }
            if (iClassifyCharacter == 4) {
                char c = cCharAt2;
                char closingQuote = getClosingQuote(c);
                strSubstring = "";
                i = i2 + 1;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    cCharAt2 = str3.charAt(i);
                    iClassifyCharacter = classifyCharacter(cCharAt2);
                    if (iClassifyCharacter == 4 && isSurroundingQuote(cCharAt2, c, closingQuote)) {
                        if (i + 1 < length) {
                            cCharAt = str3.charAt(i + 1);
                            classifyCharacter(cCharAt);
                        } else {
                            cCharAt = ';';
                        }
                        if (cCharAt2 != cCharAt) {
                            if (isClosingingQuote(cCharAt2, c, closingQuote)) {
                                i++;
                                break;
                            }
                            strSubstring = strSubstring + cCharAt2;
                        } else {
                            strSubstring = strSubstring + cCharAt2;
                            i++;
                        }
                    } else {
                        strSubstring = strSubstring + cCharAt2;
                    }
                    i++;
                }
            } else {
                i = i2;
                while (i < length) {
                    cCharAt2 = str3.charAt(i);
                    iClassifyCharacter = classifyCharacter(cCharAt2);
                    if (iClassifyCharacter != 0 && iClassifyCharacter != 4 && (iClassifyCharacter != 2 || !z)) {
                        if (iClassifyCharacter != 1 || i + 1 >= length) {
                            break;
                        }
                        cCharAt2 = str3.charAt(i + 1);
                        int iClassifyCharacter2 = classifyCharacter(cCharAt2);
                        if (iClassifyCharacter2 != 0 && iClassifyCharacter2 != 4 && (iClassifyCharacter2 != 2 || !z)) {
                            break;
                        }
                    }
                    i++;
                }
                strSubstring = str3.substring(i2, i);
            }
            int i3 = -1;
            int i4 = 1;
            while (true) {
                if (i4 > xMPNodeSeparateFindCreateArray.getChildrenLength()) {
                    break;
                }
                if (strSubstring.equals(xMPNodeSeparateFindCreateArray.getChild(i4).getValue())) {
                    i3 = i4;
                    break;
                }
                i4++;
            }
            if (i3 < 0) {
                xMPNodeSeparateFindCreateArray.addChild(new XMPNode("[]", strSubstring, null));
            }
        }
    }

    private static XMPNode separateFindCreateArray(String str, String str2, PropertyOptions propertyOptions, XMPMetaImpl xMPMetaImpl) throws XMPException {
        PropertyOptions propertyOptionsVerifySetOptions = XMPNodeUtils.verifySetOptions(propertyOptions, null);
        if (!propertyOptionsVerifySetOptions.isOnlyArrayOptions()) {
            throw new XMPException("Options can only provide array form", 103);
        }
        XMPPath xMPPathExpandXPath = XMPPathParser.expandXPath(str, str2);
        XMPNode xMPNodeFindNode = XMPNodeUtils.findNode(xMPMetaImpl.getRoot(), xMPPathExpandXPath, false, null);
        if (xMPNodeFindNode != null) {
            PropertyOptions options = xMPNodeFindNode.getOptions();
            if (!options.isArray() || options.isArrayAlternate()) {
                throw new XMPException("Named property must be non-alternate array", 102);
            }
            if (propertyOptionsVerifySetOptions.equalArrayTypes(options)) {
                throw new XMPException("Mismatch of specified and existing array form", 102);
            }
        } else {
            xMPNodeFindNode = XMPNodeUtils.findNode(xMPMetaImpl.getRoot(), xMPPathExpandXPath, true, propertyOptionsVerifySetOptions.setArray(true));
            if (xMPNodeFindNode == null) {
                throw new XMPException("Failed to create named array", 102);
            }
        }
        return xMPNodeFindNode;
    }

    public static void removeProperties(XMPMeta xMPMeta, String str, String str2, boolean z, boolean z2) throws XMPException {
        ParameterAsserts.assertImplementation(xMPMeta);
        XMPMetaImpl xMPMetaImpl = (XMPMetaImpl) xMPMeta;
        if (str2 != null && str2.length() > 0) {
            if (str == null || str.length() == 0) {
                throw new XMPException("Property name requires schema namespace", 4);
            }
            XMPPath xMPPathExpandXPath = XMPPathParser.expandXPath(str, str2);
            XMPNode xMPNodeFindNode = XMPNodeUtils.findNode(xMPMetaImpl.getRoot(), xMPPathExpandXPath, false, null);
            if (xMPNodeFindNode != null) {
                if (z || !Utils.isInternalProperty(xMPPathExpandXPath.getSegment(0).getName(), xMPPathExpandXPath.getSegment(1).getName())) {
                    XMPNode parent = xMPNodeFindNode.getParent();
                    parent.removeChild(xMPNodeFindNode);
                    if (!parent.getOptions().isSchemaNode() || parent.hasChildren()) {
                        return;
                    }
                    parent.getParent().removeChild(parent);
                    return;
                }
                return;
            }
            return;
        }
        if (str == null || str.length() <= 0) {
            Iterator itIterateChildren = xMPMetaImpl.getRoot().iterateChildren();
            while (itIterateChildren.hasNext()) {
                if (removeSchemaChildren((XMPNode) itIterateChildren.next(), z)) {
                    itIterateChildren.remove();
                }
            }
            return;
        }
        XMPNode xMPNodeFindSchemaNode = XMPNodeUtils.findSchemaNode(xMPMetaImpl.getRoot(), str, false);
        if (xMPNodeFindSchemaNode != null && removeSchemaChildren(xMPNodeFindSchemaNode, z)) {
            xMPMetaImpl.getRoot().removeChild(xMPNodeFindSchemaNode);
        }
        if (z2) {
            for (XMPAliasInfo xMPAliasInfo : XMPMetaFactory.getSchemaRegistry().findAliases(str)) {
                XMPNode xMPNodeFindNode2 = XMPNodeUtils.findNode(xMPMetaImpl.getRoot(), XMPPathParser.expandXPath(xMPAliasInfo.getNamespace(), xMPAliasInfo.getPropName()), false, null);
                if (xMPNodeFindNode2 != null) {
                    xMPNodeFindNode2.getParent().removeChild(xMPNodeFindNode2);
                }
            }
        }
    }

    public static void appendProperties(XMPMeta xMPMeta, XMPMeta xMPMeta2, boolean z, boolean z2, boolean z3) throws XMPException {
        ParameterAsserts.assertImplementation(xMPMeta);
        ParameterAsserts.assertImplementation(xMPMeta2);
        XMPMetaImpl xMPMetaImpl = (XMPMetaImpl) xMPMeta2;
        Iterator itIterateChildren = ((XMPMetaImpl) xMPMeta).getRoot().iterateChildren();
        while (itIterateChildren.hasNext()) {
            XMPNode xMPNode = (XMPNode) itIterateChildren.next();
            XMPNode xMPNodeFindSchemaNode = XMPNodeUtils.findSchemaNode(xMPMetaImpl.getRoot(), xMPNode.getName(), false);
            boolean z4 = false;
            if (xMPNodeFindSchemaNode == null) {
                xMPNodeFindSchemaNode = new XMPNode(xMPNode.getName(), xMPNode.getValue(), new PropertyOptions().setSchemaNode(true));
                xMPMetaImpl.getRoot().addChild(xMPNodeFindSchemaNode);
                z4 = true;
            }
            Iterator itIterateChildren2 = xMPNode.iterateChildren();
            while (itIterateChildren2.hasNext()) {
                XMPNode xMPNode2 = (XMPNode) itIterateChildren2.next();
                if (z || !Utils.isInternalProperty(xMPNode.getName(), xMPNode2.getName())) {
                    appendSubtree(xMPMetaImpl, xMPNode2, xMPNodeFindSchemaNode, z2, z3);
                }
            }
            if (!xMPNodeFindSchemaNode.hasChildren() && (z4 || z3)) {
                xMPMetaImpl.getRoot().removeChild(xMPNodeFindSchemaNode);
            }
        }
    }

    private static boolean removeSchemaChildren(XMPNode xMPNode, boolean z) {
        Iterator itIterateChildren = xMPNode.iterateChildren();
        while (itIterateChildren.hasNext()) {
            XMPNode xMPNode2 = (XMPNode) itIterateChildren.next();
            if (z || !Utils.isInternalProperty(xMPNode.getName(), xMPNode2.getName())) {
                itIterateChildren.remove();
            }
        }
        return !xMPNode.hasChildren();
    }

    private static void appendSubtree(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, XMPNode xMPNode2, boolean z, boolean z2) throws XMPException {
        XMPNode xMPNodeFindChildNode = XMPNodeUtils.findChildNode(xMPNode2, xMPNode.getName(), false);
        boolean z3 = false;
        if (z2) {
            z3 = xMPNode.getOptions().isSimple() ? xMPNode.getValue() == null || xMPNode.getValue().length() == 0 : !xMPNode.hasChildren();
        }
        if (z2 && z3) {
            if (xMPNodeFindChildNode != null) {
                xMPNode2.removeChild(xMPNodeFindChildNode);
                return;
            }
            return;
        }
        if (xMPNodeFindChildNode == null) {
            xMPNode2.addChild((XMPNode) xMPNode.clone());
            return;
        }
        if (z) {
            xMPMetaImpl.setNode(xMPNodeFindChildNode, xMPNode.getValue(), xMPNode.getOptions(), true);
            xMPNode2.removeChild(xMPNodeFindChildNode);
            xMPNode2.addChild((XMPNode) xMPNode.clone());
            return;
        }
        PropertyOptions options = xMPNode.getOptions();
        if (options != xMPNodeFindChildNode.getOptions()) {
            return;
        }
        if (options.isStruct()) {
            Iterator itIterateChildren = xMPNode.iterateChildren();
            while (itIterateChildren.hasNext()) {
                appendSubtree(xMPMetaImpl, (XMPNode) itIterateChildren.next(), xMPNodeFindChildNode, z, z2);
                if (z2 && !xMPNodeFindChildNode.hasChildren()) {
                    xMPNode2.removeChild(xMPNodeFindChildNode);
                }
            }
            return;
        }
        if (!options.isArrayAltText()) {
            if (options.isArray()) {
                Iterator itIterateChildren2 = xMPNode.iterateChildren();
                while (itIterateChildren2.hasNext()) {
                    XMPNode xMPNode3 = (XMPNode) itIterateChildren2.next();
                    boolean z4 = false;
                    Iterator itIterateChildren3 = xMPNodeFindChildNode.iterateChildren();
                    while (itIterateChildren3.hasNext()) {
                        if (itemValuesMatch(xMPNode3, (XMPNode) itIterateChildren3.next())) {
                            z4 = true;
                        }
                    }
                    if (!z4) {
                        xMPNodeFindChildNode = (XMPNode) xMPNode3.clone();
                        xMPNode2.addChild(xMPNodeFindChildNode);
                    }
                }
                return;
            }
            return;
        }
        Iterator itIterateChildren4 = xMPNode.iterateChildren();
        while (itIterateChildren4.hasNext()) {
            XMPNode xMPNode4 = (XMPNode) itIterateChildren4.next();
            if (xMPNode4.hasQualifier() && "xml:lang".equals(xMPNode4.getQualifier(1).getName())) {
                int iLookupLanguageItem = XMPNodeUtils.lookupLanguageItem(xMPNodeFindChildNode, xMPNode4.getQualifier(1).getValue());
                if (z2 && (xMPNode4.getValue() == null || xMPNode4.getValue().length() == 0)) {
                    if (iLookupLanguageItem != -1) {
                        xMPNodeFindChildNode.removeChild(iLookupLanguageItem);
                        if (!xMPNodeFindChildNode.hasChildren()) {
                            xMPNode2.removeChild(xMPNodeFindChildNode);
                        }
                    }
                } else if (iLookupLanguageItem == -1) {
                    if ("x-default".equals(xMPNode4.getQualifier(1).getValue()) && xMPNodeFindChildNode.hasChildren()) {
                        XMPNode xMPNode5 = new XMPNode(xMPNode4.getName(), xMPNode4.getValue(), xMPNode4.getOptions());
                        xMPNode4.cloneSubtree(xMPNode5);
                        xMPNodeFindChildNode.addChild(1, xMPNode5);
                    } else {
                        xMPNode4.cloneSubtree(xMPNodeFindChildNode);
                    }
                }
            }
        }
    }

    private static boolean itemValuesMatch(XMPNode xMPNode, XMPNode xMPNode2) throws XMPException {
        PropertyOptions options = xMPNode.getOptions();
        if (options.equals(xMPNode2.getOptions())) {
            return false;
        }
        if (options.getOptions() == 0) {
            if (xMPNode.getValue().equals(xMPNode2.getValue()) && xMPNode.getOptions().getHasLanguage() == xMPNode2.getOptions().getHasLanguage()) {
                return !xMPNode.getOptions().getHasLanguage() || xMPNode.getQualifier(1).getValue().equals(xMPNode2.getQualifier(1).getValue());
            }
            return false;
        }
        if (options.isStruct()) {
            if (xMPNode.getChildrenLength() != xMPNode2.getChildrenLength()) {
                return false;
            }
            Iterator itIterateChildren = xMPNode.iterateChildren();
            while (itIterateChildren.hasNext()) {
                XMPNode xMPNode3 = (XMPNode) itIterateChildren.next();
                XMPNode xMPNodeFindChildNode = XMPNodeUtils.findChildNode(xMPNode2, xMPNode3.getName(), false);
                if (xMPNodeFindChildNode == null || !itemValuesMatch(xMPNode3, xMPNodeFindChildNode)) {
                    return false;
                }
            }
            return true;
        }
        if (!$assertionsDisabled && !options.isArray()) {
            throw new AssertionError();
        }
        Iterator itIterateChildren2 = xMPNode.iterateChildren();
        while (itIterateChildren2.hasNext()) {
            XMPNode xMPNode4 = (XMPNode) itIterateChildren2.next();
            boolean z = false;
            Iterator itIterateChildren3 = xMPNode2.iterateChildren();
            while (true) {
                if (!itIterateChildren3.hasNext()) {
                    break;
                }
                if (itemValuesMatch(xMPNode4, (XMPNode) itIterateChildren3.next())) {
                    z = true;
                    break;
                }
            }
            if (!z) {
                return false;
            }
        }
        return true;
    }

    private static void checkSeparator(String str) throws XMPException {
        boolean z = false;
        for (int i = 0; i < str.length(); i++) {
            int iClassifyCharacter = classifyCharacter(str.charAt(i));
            if (iClassifyCharacter == 3) {
                if (z) {
                    throw new XMPException("Separator can have only one semicolon", 4);
                }
                z = true;
            } else if (iClassifyCharacter != 1) {
                throw new XMPException("Separator can have only spaces and one semicolon", 4);
            }
        }
        if (!z) {
            throw new XMPException("Separator must have one semicolon", 4);
        }
    }

    private static char checkQuotes(String str, char c) throws XMPException {
        char cCharAt;
        if (classifyCharacter(c) != 4) {
            throw new XMPException("Invalid quoting character", 4);
        }
        if (str.length() == 1) {
            cCharAt = c;
        } else {
            cCharAt = str.charAt(1);
            if (classifyCharacter(cCharAt) != 4) {
                throw new XMPException("Invalid quoting character", 4);
            }
        }
        if (cCharAt != getClosingQuote(c)) {
            throw new XMPException("Mismatched quote pair", 4);
        }
        return cCharAt;
    }

    private static int classifyCharacter(char c) {
        if (SPACES.indexOf(c) >= 0) {
            return 1;
        }
        if (8192 <= c && c <= 8203) {
            return 1;
        }
        if (COMMAS.indexOf(c) >= 0) {
            return 2;
        }
        if (SEMICOLA.indexOf(c) >= 0) {
            return 3;
        }
        if (QUOTES.indexOf(c) >= 0) {
            return 4;
        }
        if (12296 <= c && c <= 12303) {
            return 4;
        }
        if (8216 > c || c > 8223) {
            return (c < ' ' || CONTROLS.indexOf(c) >= 0) ? 5 : 0;
        }
        return 4;
    }

    private static char getClosingQuote(char c) {
        switch (c) {
            case '\"':
                return '\"';
            case 171:
                return (char) 187;
            case 187:
                return (char) 171;
            case 8213:
                return (char) 8213;
            case 8216:
                return (char) 8217;
            case 8218:
                return (char) 8219;
            case 8220:
                return (char) 8221;
            case SonyType1MakernoteDirectory.TAG_AF_POINT_SELECTED /* 8222 */:
                return (char) 8223;
            case 8249:
                return (char) 8250;
            case 8250:
                return (char) 8249;
            case 12296:
                return (char) 12297;
            case 12298:
                return (char) 12299;
            case 12300:
                return (char) 12301;
            case 12302:
                return (char) 12303;
            case 12317:
                return (char) 12319;
            default:
                return (char) 0;
        }
    }

    private static String applyQuotes(String str, char c, char c2, boolean z) {
        if (str == null) {
            str = "";
        }
        boolean z2 = false;
        int i = 0;
        while (i < str.length()) {
            int iClassifyCharacter = classifyCharacter(str.charAt(i));
            if (i == 0 && iClassifyCharacter == 4) {
                break;
            }
            if (iClassifyCharacter != 1) {
                z2 = false;
                if (iClassifyCharacter == 3 || iClassifyCharacter == 5 || (iClassifyCharacter == 2 && !z)) {
                    break;
                }
                i++;
            } else {
                if (z2) {
                    break;
                }
                z2 = true;
                i++;
            }
        }
        if (i < str.length()) {
            StringBuffer stringBuffer = new StringBuffer(str.length() + 2);
            int i2 = 0;
            while (i2 <= i && classifyCharacter(str.charAt(i)) != 4) {
                i2++;
            }
            stringBuffer.append(c).append(str.substring(0, i2));
            for (int i3 = i2; i3 < str.length(); i3++) {
                stringBuffer.append(str.charAt(i3));
                if (classifyCharacter(str.charAt(i3)) == 4 && isSurroundingQuote(str.charAt(i3), c, c2)) {
                    stringBuffer.append(str.charAt(i3));
                }
            }
            stringBuffer.append(c2);
            str = stringBuffer.toString();
        }
        return str;
    }

    private static boolean isSurroundingQuote(char c, char c2, char c3) {
        return c == c2 || isClosingingQuote(c, c2, c3);
    }

    private static boolean isClosingingQuote(char c, char c2, char c3) {
        return c == c3 || (c2 == 12317 && c == 12318) || c == 12319;
    }

    static {
        $assertionsDisabled = !XMPUtilsImpl.class.desiredAssertionStatus();
    }
}
