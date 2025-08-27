package com.adobe.xmp.impl;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPDateTime;
import com.adobe.xmp.XMPDateTimeFactory;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.XMPUtils;
import com.adobe.xmp.impl.xpath.XMPPath;
import com.adobe.xmp.impl.xpath.XMPPathSegment;
import com.adobe.xmp.options.PropertyOptions;
import java.util.GregorianCalendar;
import java.util.Iterator;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/XMPNodeUtils.class */
public class XMPNodeUtils implements XMPConst {
    static final int CLT_NO_VALUES = 0;
    static final int CLT_SPECIFIC_MATCH = 1;
    static final int CLT_SINGLE_GENERIC = 2;
    static final int CLT_MULTIPLE_GENERIC = 3;
    static final int CLT_XDEFAULT = 4;
    static final int CLT_FIRST_ITEM = 5;
    static final /* synthetic */ boolean $assertionsDisabled;

    private XMPNodeUtils() {
    }

    static XMPNode findSchemaNode(XMPNode xMPNode, String str, boolean z) throws XMPException {
        return findSchemaNode(xMPNode, str, null, z);
    }

    static XMPNode findSchemaNode(XMPNode xMPNode, String str, String str2, boolean z) throws XMPException {
        if (!$assertionsDisabled && xMPNode.getParent() != null) {
            throw new AssertionError();
        }
        XMPNode xMPNodeFindChildByName = xMPNode.findChildByName(str);
        if (xMPNodeFindChildByName == null && z) {
            xMPNodeFindChildByName = new XMPNode(str, new PropertyOptions().setSchemaNode(true));
            xMPNodeFindChildByName.setImplicit(true);
            String namespacePrefix = XMPMetaFactory.getSchemaRegistry().getNamespacePrefix(str);
            if (namespacePrefix == null) {
                if (str2 == null || str2.length() == 0) {
                    throw new XMPException("Unregistered schema namespace URI", 101);
                }
                namespacePrefix = XMPMetaFactory.getSchemaRegistry().registerNamespace(str, str2);
            }
            xMPNodeFindChildByName.setValue(namespacePrefix);
            xMPNode.addChild(xMPNodeFindChildByName);
        }
        return xMPNodeFindChildByName;
    }

    static XMPNode findChildNode(XMPNode xMPNode, String str, boolean z) throws XMPException {
        if (!xMPNode.getOptions().isSchemaNode() && !xMPNode.getOptions().isStruct()) {
            if (!xMPNode.isImplicit()) {
                throw new XMPException("Named children only allowed for schemas and structs", 102);
            }
            if (xMPNode.getOptions().isArray()) {
                throw new XMPException("Named children not allowed for arrays", 102);
            }
            if (z) {
                xMPNode.getOptions().setStruct(true);
            }
        }
        XMPNode xMPNodeFindChildByName = xMPNode.findChildByName(str);
        if (xMPNodeFindChildByName == null && z) {
            xMPNodeFindChildByName = new XMPNode(str, new PropertyOptions());
            xMPNodeFindChildByName.setImplicit(true);
            xMPNode.addChild(xMPNodeFindChildByName);
        }
        if (!$assertionsDisabled && xMPNodeFindChildByName == null && z) {
            throw new AssertionError();
        }
        return xMPNodeFindChildByName;
    }

    static XMPNode findNode(XMPNode xMPNode, XMPPath xMPPath, boolean z, PropertyOptions propertyOptions) throws XMPException {
        if (xMPPath == null || xMPPath.size() == 0) {
            throw new XMPException("Empty XMPPath", 102);
        }
        XMPNode xMPNode2 = null;
        XMPNode xMPNodeFindSchemaNode = findSchemaNode(xMPNode, xMPPath.getSegment(0).getName(), z);
        if (xMPNodeFindSchemaNode == null) {
            return null;
        }
        if (xMPNodeFindSchemaNode.isImplicit()) {
            xMPNodeFindSchemaNode.setImplicit(false);
            xMPNode2 = xMPNodeFindSchemaNode;
        }
        for (int i = 1; i < xMPPath.size(); i++) {
            try {
                xMPNodeFindSchemaNode = followXPathStep(xMPNodeFindSchemaNode, xMPPath.getSegment(i), z);
                if (xMPNodeFindSchemaNode == null) {
                    if (!z) {
                        return null;
                    }
                    deleteNode(xMPNode2);
                    return null;
                }
                if (xMPNodeFindSchemaNode.isImplicit()) {
                    xMPNodeFindSchemaNode.setImplicit(false);
                    if (i == 1 && xMPPath.getSegment(i).isAlias() && xMPPath.getSegment(i).getAliasForm() != 0) {
                        xMPNodeFindSchemaNode.getOptions().setOption(xMPPath.getSegment(i).getAliasForm(), true);
                    } else if (i < xMPPath.size() - 1 && xMPPath.getSegment(i).getKind() == 1 && !xMPNodeFindSchemaNode.getOptions().isCompositeProperty()) {
                        xMPNodeFindSchemaNode.getOptions().setStruct(true);
                    }
                    if (xMPNode2 == null) {
                        xMPNode2 = xMPNodeFindSchemaNode;
                    }
                }
            } catch (XMPException e) {
                if (xMPNode2 != null) {
                    deleteNode(xMPNode2);
                }
                throw e;
            }
        }
        if (xMPNode2 != null) {
            xMPNodeFindSchemaNode.getOptions().mergeWith(propertyOptions);
            xMPNodeFindSchemaNode.setOptions(xMPNodeFindSchemaNode.getOptions());
        }
        return xMPNodeFindSchemaNode;
    }

    static void deleteNode(XMPNode xMPNode) {
        XMPNode parent = xMPNode.getParent();
        if (xMPNode.getOptions().isQualifier()) {
            parent.removeQualifier(xMPNode);
        } else {
            parent.removeChild(xMPNode);
        }
        if (parent.hasChildren() || !parent.getOptions().isSchemaNode()) {
            return;
        }
        parent.getParent().removeChild(parent);
    }

    static void setNodeValue(XMPNode xMPNode, Object obj) {
        String strSerializeNodeValue = serializeNodeValue(obj);
        if (xMPNode.getOptions().isQualifier() && "xml:lang".equals(xMPNode.getName())) {
            xMPNode.setValue(Utils.normalizeLangValue(strSerializeNodeValue));
        } else {
            xMPNode.setValue(strSerializeNodeValue);
        }
    }

    static PropertyOptions verifySetOptions(PropertyOptions propertyOptions, Object obj) throws XMPException {
        if (propertyOptions == null) {
            propertyOptions = new PropertyOptions();
        }
        if (propertyOptions.isArrayAltText()) {
            propertyOptions.setArrayAlternate(true);
        }
        if (propertyOptions.isArrayAlternate()) {
            propertyOptions.setArrayOrdered(true);
        }
        if (propertyOptions.isArrayOrdered()) {
            propertyOptions.setArray(true);
        }
        if (propertyOptions.isCompositeProperty() && obj != null && obj.toString().length() > 0) {
            throw new XMPException("Structs and arrays can't have values", 103);
        }
        propertyOptions.assertConsistency(propertyOptions.getOptions());
        return propertyOptions;
    }

    static String serializeNodeValue(Object obj) {
        String strConvertFromBoolean = obj == null ? null : obj instanceof Boolean ? XMPUtils.convertFromBoolean(((Boolean) obj).booleanValue()) : obj instanceof Integer ? XMPUtils.convertFromInteger(((Integer) obj).intValue()) : obj instanceof Long ? XMPUtils.convertFromLong(((Long) obj).longValue()) : obj instanceof Double ? XMPUtils.convertFromDouble(((Double) obj).doubleValue()) : obj instanceof XMPDateTime ? XMPUtils.convertFromDate((XMPDateTime) obj) : obj instanceof GregorianCalendar ? XMPUtils.convertFromDate(XMPDateTimeFactory.createFromCalendar((GregorianCalendar) obj)) : obj instanceof byte[] ? XMPUtils.encodeBase64((byte[]) obj) : obj.toString();
        if (strConvertFromBoolean != null) {
            return Utils.removeControlChars(strConvertFromBoolean);
        }
        return null;
    }

    private static XMPNode followXPathStep(XMPNode xMPNode, XMPPathSegment xMPPathSegment, boolean z) throws XMPException, NumberFormatException {
        int iLookupQualSelector;
        XMPNode child = null;
        int kind = xMPPathSegment.getKind();
        if (kind == 1) {
            child = findChildNode(xMPNode, xMPPathSegment.getName(), z);
        } else if (kind == 2) {
            child = findQualifierNode(xMPNode, xMPPathSegment.getName().substring(1), z);
        } else {
            if (!xMPNode.getOptions().isArray()) {
                throw new XMPException("Indexing applied to non-array", 102);
            }
            if (kind == 3) {
                iLookupQualSelector = findIndexedItem(xMPNode, xMPPathSegment.getName(), z);
            } else if (kind == 4) {
                iLookupQualSelector = xMPNode.getChildrenLength();
            } else if (kind == 6) {
                String[] strArrSplitNameAndValue = Utils.splitNameAndValue(xMPPathSegment.getName());
                iLookupQualSelector = lookupFieldSelector(xMPNode, strArrSplitNameAndValue[0], strArrSplitNameAndValue[1]);
            } else {
                if (kind != 5) {
                    throw new XMPException("Unknown array indexing step in FollowXPathStep", 9);
                }
                String[] strArrSplitNameAndValue2 = Utils.splitNameAndValue(xMPPathSegment.getName());
                iLookupQualSelector = lookupQualSelector(xMPNode, strArrSplitNameAndValue2[0], strArrSplitNameAndValue2[1], xMPPathSegment.getAliasForm());
            }
            if (1 <= iLookupQualSelector && iLookupQualSelector <= xMPNode.getChildrenLength()) {
                child = xMPNode.getChild(iLookupQualSelector);
            }
        }
        return child;
    }

    private static XMPNode findQualifierNode(XMPNode xMPNode, String str, boolean z) throws XMPException {
        if (!$assertionsDisabled && str.startsWith("?")) {
            throw new AssertionError();
        }
        XMPNode xMPNodeFindQualifierByName = xMPNode.findQualifierByName(str);
        if (xMPNodeFindQualifierByName == null && z) {
            xMPNodeFindQualifierByName = new XMPNode(str, null);
            xMPNodeFindQualifierByName.setImplicit(true);
            xMPNode.addQualifier(xMPNodeFindQualifierByName);
        }
        return xMPNodeFindQualifierByName;
    }

    private static int findIndexedItem(XMPNode xMPNode, String str, boolean z) throws XMPException, NumberFormatException {
        try {
            int i = Integer.parseInt(str.substring(1, str.length() - 1));
            if (i < 1) {
                throw new XMPException("Array index must be larger than zero", 102);
            }
            if (z && i == xMPNode.getChildrenLength() + 1) {
                XMPNode xMPNode2 = new XMPNode("[]", null);
                xMPNode2.setImplicit(true);
                xMPNode.addChild(xMPNode2);
            }
            return i;
        } catch (NumberFormatException e) {
            throw new XMPException("Array index not digits.", 102);
        }
    }

    private static int lookupFieldSelector(XMPNode xMPNode, String str, String str2) throws XMPException {
        int i = -1;
        for (int i2 = 1; i2 <= xMPNode.getChildrenLength() && i < 0; i2++) {
            XMPNode child = xMPNode.getChild(i2);
            if (!child.getOptions().isStruct()) {
                throw new XMPException("Field selector must be used on array of struct", 102);
            }
            int i3 = 1;
            while (true) {
                if (i3 <= child.getChildrenLength()) {
                    XMPNode child2 = child.getChild(i3);
                    if (str.equals(child2.getName()) && str2.equals(child2.getValue())) {
                        i = i2;
                        break;
                    }
                    i3++;
                }
            }
        }
        return i;
    }

    private static int lookupQualSelector(XMPNode xMPNode, String str, String str2, int i) throws XMPException {
        if ("xml:lang".equals(str)) {
            int iLookupLanguageItem = lookupLanguageItem(xMPNode, Utils.normalizeLangValue(str2));
            if (iLookupLanguageItem >= 0 || (i & 4096) <= 0) {
                return iLookupLanguageItem;
            }
            XMPNode xMPNode2 = new XMPNode("[]", null);
            xMPNode2.addQualifier(new XMPNode("xml:lang", "x-default", null));
            xMPNode.addChild(1, xMPNode2);
            return 1;
        }
        for (int i2 = 1; i2 < xMPNode.getChildrenLength(); i2++) {
            Iterator itIterateQualifier = xMPNode.getChild(i2).iterateQualifier();
            while (itIterateQualifier.hasNext()) {
                XMPNode xMPNode3 = (XMPNode) itIterateQualifier.next();
                if (str.equals(xMPNode3.getName()) && str2.equals(xMPNode3.getValue())) {
                    return i2;
                }
            }
        }
        return -1;
    }

    static void normalizeLangArray(XMPNode xMPNode) {
        if (xMPNode.getOptions().isArrayAltText()) {
            for (int i = 2; i <= xMPNode.getChildrenLength(); i++) {
                XMPNode child = xMPNode.getChild(i);
                if (child.hasQualifier() && "x-default".equals(child.getQualifier(1).getValue())) {
                    try {
                        xMPNode.removeChild(i);
                        xMPNode.addChild(1, child);
                    } catch (XMPException e) {
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                    }
                    if (i == 2) {
                        xMPNode.getChild(2).setValue(child.getValue());
                        return;
                    }
                    return;
                }
            }
        }
    }

    static void detectAltText(XMPNode xMPNode) {
        if (xMPNode.getOptions().isArrayAlternate() && xMPNode.hasChildren()) {
            boolean z = false;
            Iterator itIterateChildren = xMPNode.iterateChildren();
            while (true) {
                if (!itIterateChildren.hasNext()) {
                    break;
                } else if (((XMPNode) itIterateChildren.next()).getOptions().getHasLanguage()) {
                    z = true;
                    break;
                }
            }
            if (z) {
                xMPNode.getOptions().setArrayAltText(true);
                normalizeLangArray(xMPNode);
            }
        }
    }

    static void appendLangItem(XMPNode xMPNode, String str, String str2) throws XMPException {
        XMPNode xMPNode2 = new XMPNode("[]", str2, null);
        XMPNode xMPNode3 = new XMPNode("xml:lang", str, null);
        xMPNode2.addQualifier(xMPNode3);
        if ("x-default".equals(xMPNode3.getValue())) {
            xMPNode.addChild(1, xMPNode2);
        } else {
            xMPNode.addChild(xMPNode2);
        }
    }

    static Object[] chooseLocalizedText(XMPNode xMPNode, String str, String str2) throws XMPException {
        if (!xMPNode.getOptions().isArrayAltText()) {
            throw new XMPException("Localized text array is not alt-text", 102);
        }
        if (!xMPNode.hasChildren()) {
            return new Object[]{new Integer(0), null};
        }
        int i = 0;
        XMPNode xMPNode2 = null;
        XMPNode xMPNode3 = null;
        Iterator itIterateChildren = xMPNode.iterateChildren();
        while (itIterateChildren.hasNext()) {
            XMPNode xMPNode4 = (XMPNode) itIterateChildren.next();
            if (xMPNode4.getOptions().isCompositeProperty()) {
                throw new XMPException("Alt-text array item is not simple", 102);
            }
            if (!xMPNode4.hasQualifier() || !"xml:lang".equals(xMPNode4.getQualifier(1).getName())) {
                throw new XMPException("Alt-text array item has no language qualifier", 102);
            }
            String value = xMPNode4.getQualifier(1).getValue();
            if (str2.equals(value)) {
                return new Object[]{new Integer(1), xMPNode4};
            }
            if (str != null && value.startsWith(str)) {
                if (xMPNode2 == null) {
                    xMPNode2 = xMPNode4;
                }
                i++;
            } else if ("x-default".equals(value)) {
                xMPNode3 = xMPNode4;
            }
        }
        return i == 1 ? new Object[]{new Integer(2), xMPNode2} : i > 1 ? new Object[]{new Integer(3), xMPNode2} : xMPNode3 != null ? new Object[]{new Integer(4), xMPNode3} : new Object[]{new Integer(5), xMPNode.getChild(1)};
    }

    static int lookupLanguageItem(XMPNode xMPNode, String str) throws XMPException {
        if (!xMPNode.getOptions().isArray()) {
            throw new XMPException("Language item must be used on array", 102);
        }
        for (int i = 1; i <= xMPNode.getChildrenLength(); i++) {
            XMPNode child = xMPNode.getChild(i);
            if (child.hasQualifier() && "xml:lang".equals(child.getQualifier(1).getName()) && str.equals(child.getQualifier(1).getValue())) {
                return i;
            }
        }
        return -1;
    }

    static {
        $assertionsDisabled = !XMPNodeUtils.class.desiredAssertionStatus();
    }
}
