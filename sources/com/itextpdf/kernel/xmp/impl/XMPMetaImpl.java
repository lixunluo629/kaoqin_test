package com.itextpdf.kernel.xmp.impl;

import com.itextpdf.kernel.xmp.XMPConst;
import com.itextpdf.kernel.xmp.XMPDateTime;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPIterator;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPPathFactory;
import com.itextpdf.kernel.xmp.XMPUtils;
import com.itextpdf.kernel.xmp.impl.xpath.XMPPath;
import com.itextpdf.kernel.xmp.impl.xpath.XMPPathParser;
import com.itextpdf.kernel.xmp.options.IteratorOptions;
import com.itextpdf.kernel.xmp.options.ParseOptions;
import com.itextpdf.kernel.xmp.options.PropertyOptions;
import com.itextpdf.kernel.xmp.properties.XMPProperty;
import java.util.Calendar;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/XMPMetaImpl.class */
public class XMPMetaImpl implements XMPConst, XMPMeta {
    private static final int VALUE_STRING = 0;
    private static final int VALUE_BOOLEAN = 1;
    private static final int VALUE_INTEGER = 2;
    private static final int VALUE_LONG = 3;
    private static final int VALUE_DOUBLE = 4;
    private static final int VALUE_DATE = 5;
    private static final int VALUE_CALENDAR = 6;
    private static final int VALUE_BASE64 = 7;
    private XMPNode tree;
    private String packetHeader;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XMPMetaImpl.class.desiredAssertionStatus();
    }

    public XMPMetaImpl() {
        this.packetHeader = null;
        this.tree = new XMPNode(null, null, null);
    }

    public XMPMetaImpl(XMPNode tree) {
        this.packetHeader = null;
        this.tree = tree;
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void appendArrayItem(String schemaNS, String arrayName, PropertyOptions arrayOptions, String itemValue, PropertyOptions itemOptions) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertArrayName(arrayName);
        if (arrayOptions == null) {
            arrayOptions = new PropertyOptions();
        }
        if (!arrayOptions.isOnlyArrayOptions()) {
            throw new XMPException("Only array form flags allowed for arrayOptions", 103);
        }
        PropertyOptions arrayOptions2 = XMPNodeUtils.verifySetOptions(arrayOptions, null);
        XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, arrayName);
        XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, false, null);
        if (arrayNode != null) {
            if (!arrayNode.getOptions().isArray()) {
                throw new XMPException("The named property is not an array", 102);
            }
        } else if (arrayOptions2.isArray()) {
            arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, true, arrayOptions2);
            if (arrayNode == null) {
                throw new XMPException("Failure creating array node", 102);
            }
        } else {
            throw new XMPException("Explicit arrayOptions required to create new array", 103);
        }
        doSetArrayItem(arrayNode, -1, itemValue, itemOptions, true);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void appendArrayItem(String schemaNS, String arrayName, String itemValue) throws XMPException {
        appendArrayItem(schemaNS, arrayName, null, itemValue, null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public int countArrayItems(String schemaNS, String arrayName) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertArrayName(arrayName);
        XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, arrayName);
        XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, false, null);
        if (arrayNode == null) {
            return 0;
        }
        if (arrayNode.getOptions().isArray()) {
            return arrayNode.getChildrenLength();
        }
        throw new XMPException("The named property is not an array", 102);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void deleteArrayItem(String schemaNS, String arrayName, int itemIndex) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertArrayName(arrayName);
            String itemPath = XMPPathFactory.composeArrayItemPath(arrayName, itemIndex);
            deleteProperty(schemaNS, itemPath);
        } catch (XMPException e) {
        }
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void deleteProperty(String schemaNS, String propName) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertPropName(propName);
            XMPPath expPath = XMPPathParser.expandXPath(schemaNS, propName);
            XMPNode propNode = XMPNodeUtils.findNode(this.tree, expPath, false, null);
            if (propNode != null) {
                XMPNodeUtils.deleteNode(propNode);
            }
        } catch (XMPException e) {
        }
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void deleteQualifier(String schemaNS, String propName, String qualNS, String qualName) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertPropName(propName);
            String qualPath = propName + XMPPathFactory.composeQualifierPath(qualNS, qualName);
            deleteProperty(schemaNS, qualPath);
        } catch (XMPException e) {
        }
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void deleteStructField(String schemaNS, String structName, String fieldNS, String fieldName) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertStructName(structName);
            String fieldPath = structName + XMPPathFactory.composeStructFieldPath(fieldNS, fieldName);
            deleteProperty(schemaNS, fieldPath);
        } catch (XMPException e) {
        }
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public boolean doesPropertyExist(String schemaNS, String propName) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertPropName(propName);
            XMPPath expPath = XMPPathParser.expandXPath(schemaNS, propName);
            XMPNode propNode = XMPNodeUtils.findNode(this.tree, expPath, false, null);
            return propNode != null;
        } catch (XMPException e) {
            return false;
        }
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public boolean doesArrayItemExist(String schemaNS, String arrayName, int itemIndex) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertArrayName(arrayName);
            String path = XMPPathFactory.composeArrayItemPath(arrayName, itemIndex);
            return doesPropertyExist(schemaNS, path);
        } catch (XMPException e) {
            return false;
        }
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public boolean doesStructFieldExist(String schemaNS, String structName, String fieldNS, String fieldName) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertStructName(structName);
            String path = XMPPathFactory.composeStructFieldPath(fieldNS, fieldName);
            return doesPropertyExist(schemaNS, structName + path);
        } catch (XMPException e) {
            return false;
        }
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public boolean doesQualifierExist(String schemaNS, String propName, String qualNS, String qualName) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertPropName(propName);
            String path = XMPPathFactory.composeQualifierPath(qualNS, qualName);
            return doesPropertyExist(schemaNS, propName + path);
        } catch (XMPException e) {
            return false;
        }
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public XMPProperty getArrayItem(String schemaNS, String arrayName, int itemIndex) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertArrayName(arrayName);
        String itemPath = XMPPathFactory.composeArrayItemPath(arrayName, itemIndex);
        return getProperty(schemaNS, itemPath);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public XMPProperty getLocalizedText(String schemaNS, String altTextName, String genericLang, String specificLang) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertArrayName(altTextName);
        ParameterAsserts.assertSpecificLang(specificLang);
        String genericLang2 = genericLang != null ? Utils.normalizeLangValue(genericLang) : null;
        String specificLang2 = Utils.normalizeLangValue(specificLang);
        XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, altTextName);
        XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, false, null);
        if (arrayNode == null) {
            return null;
        }
        Object[] result = XMPNodeUtils.chooseLocalizedText(arrayNode, genericLang2, specificLang2);
        int match = ((Integer) result[0]).intValue();
        final XMPNode itemNode = (XMPNode) result[1];
        if (match != 0) {
            return new XMPProperty() { // from class: com.itextpdf.kernel.xmp.impl.XMPMetaImpl.1
                @Override // com.itextpdf.kernel.xmp.properties.XMPProperty
                public String getValue() {
                    return itemNode.getValue();
                }

                @Override // com.itextpdf.kernel.xmp.properties.XMPProperty
                public PropertyOptions getOptions() {
                    return itemNode.getOptions();
                }

                @Override // com.itextpdf.kernel.xmp.properties.XMPProperty
                public String getLanguage() {
                    return itemNode.getQualifier(1).getValue();
                }

                public String toString() {
                    return itemNode.getValue();
                }
            };
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x00cd, code lost:
    
        throw new com.itextpdf.kernel.xmp.XMPException("Language qualifier must be first", 102);
     */
    @Override // com.itextpdf.kernel.xmp.XMPMeta
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setLocalizedText(java.lang.String r8, java.lang.String r9, java.lang.String r10, java.lang.String r11, java.lang.String r12, com.itextpdf.kernel.xmp.options.PropertyOptions r13) throws com.itextpdf.kernel.xmp.XMPException {
        /*
            Method dump skipped, instructions count: 702
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.kernel.xmp.impl.XMPMetaImpl.setLocalizedText(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.itextpdf.kernel.xmp.options.PropertyOptions):void");
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setLocalizedText(String schemaNS, String altTextName, String genericLang, String specificLang, String itemValue) throws XMPException {
        setLocalizedText(schemaNS, altTextName, genericLang, specificLang, itemValue, null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public XMPProperty getProperty(String schemaNS, String propName) throws XMPException {
        return getProperty(schemaNS, propName, 0);
    }

    protected XMPProperty getProperty(String schemaNS, String propName, int valueType) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertPropName(propName);
        XMPPath expPath = XMPPathParser.expandXPath(schemaNS, propName);
        final XMPNode propNode = XMPNodeUtils.findNode(this.tree, expPath, false, null);
        if (propNode != null) {
            if (valueType != 0 && propNode.getOptions().isCompositeProperty()) {
                throw new XMPException("Property must be simple when a value type is requested", 102);
            }
            final Object value = evaluateNodeValue(valueType, propNode);
            return new XMPProperty() { // from class: com.itextpdf.kernel.xmp.impl.XMPMetaImpl.2
                @Override // com.itextpdf.kernel.xmp.properties.XMPProperty
                public String getValue() {
                    if (value != null) {
                        return value.toString();
                    }
                    return null;
                }

                @Override // com.itextpdf.kernel.xmp.properties.XMPProperty
                public PropertyOptions getOptions() {
                    return propNode.getOptions();
                }

                @Override // com.itextpdf.kernel.xmp.properties.XMPProperty
                public String getLanguage() {
                    return null;
                }

                public String toString() {
                    return value.toString();
                }
            };
        }
        return null;
    }

    protected Object getPropertyObject(String schemaNS, String propName, int valueType) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertPropName(propName);
        XMPPath expPath = XMPPathParser.expandXPath(schemaNS, propName);
        XMPNode propNode = XMPNodeUtils.findNode(this.tree, expPath, false, null);
        if (propNode != null) {
            if (valueType != 0 && propNode.getOptions().isCompositeProperty()) {
                throw new XMPException("Property must be simple when a value type is requested", 102);
            }
            return evaluateNodeValue(valueType, propNode);
        }
        return null;
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public Boolean getPropertyBoolean(String schemaNS, String propName) throws XMPException {
        return (Boolean) getPropertyObject(schemaNS, propName, 1);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyBoolean(String schemaNS, String propName, boolean propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, propValue ? "True" : "False", options);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyBoolean(String schemaNS, String propName, boolean propValue) throws XMPException {
        setProperty(schemaNS, propName, propValue ? "True" : "False", null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public Integer getPropertyInteger(String schemaNS, String propName) throws XMPException {
        return (Integer) getPropertyObject(schemaNS, propName, 2);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyInteger(String schemaNS, String propName, int propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, new Integer(propValue), options);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyInteger(String schemaNS, String propName, int propValue) throws XMPException {
        setProperty(schemaNS, propName, new Integer(propValue), null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public Long getPropertyLong(String schemaNS, String propName) throws XMPException {
        return (Long) getPropertyObject(schemaNS, propName, 3);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyLong(String schemaNS, String propName, long propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, new Long(propValue), options);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyLong(String schemaNS, String propName, long propValue) throws XMPException {
        setProperty(schemaNS, propName, new Long(propValue), null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public Double getPropertyDouble(String schemaNS, String propName) throws XMPException {
        return (Double) getPropertyObject(schemaNS, propName, 4);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyDouble(String schemaNS, String propName, double propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, new Double(propValue), options);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyDouble(String schemaNS, String propName, double propValue) throws XMPException {
        setProperty(schemaNS, propName, new Double(propValue), null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public XMPDateTime getPropertyDate(String schemaNS, String propName) throws XMPException {
        return (XMPDateTime) getPropertyObject(schemaNS, propName, 5);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyDate(String schemaNS, String propName, XMPDateTime propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, propValue, options);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyDate(String schemaNS, String propName, XMPDateTime propValue) throws XMPException {
        setProperty(schemaNS, propName, propValue, null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public Calendar getPropertyCalendar(String schemaNS, String propName) throws XMPException {
        return (Calendar) getPropertyObject(schemaNS, propName, 6);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyCalendar(String schemaNS, String propName, Calendar propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, propValue, options);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyCalendar(String schemaNS, String propName, Calendar propValue) throws XMPException {
        setProperty(schemaNS, propName, propValue, null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public byte[] getPropertyBase64(String schemaNS, String propName) throws XMPException {
        return (byte[]) getPropertyObject(schemaNS, propName, 7);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public String getPropertyString(String schemaNS, String propName) throws XMPException {
        return (String) getPropertyObject(schemaNS, propName, 0);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyBase64(String schemaNS, String propName, byte[] propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, propValue, options);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setPropertyBase64(String schemaNS, String propName, byte[] propValue) throws XMPException {
        setProperty(schemaNS, propName, propValue, null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public XMPProperty getQualifier(String schemaNS, String propName, String qualNS, String qualName) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertPropName(propName);
        String qualPath = propName + XMPPathFactory.composeQualifierPath(qualNS, qualName);
        return getProperty(schemaNS, qualPath);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public XMPProperty getStructField(String schemaNS, String structName, String fieldNS, String fieldName) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertStructName(structName);
        String fieldPath = structName + XMPPathFactory.composeStructFieldPath(fieldNS, fieldName);
        return getProperty(schemaNS, fieldPath);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public XMPIterator iterator() throws XMPException {
        return iterator(null, null, null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public XMPIterator iterator(IteratorOptions options) throws XMPException {
        return iterator(null, null, options);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public XMPIterator iterator(String schemaNS, String propName, IteratorOptions options) throws XMPException {
        return new XMPIteratorImpl(this, schemaNS, propName, options);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setArrayItem(String schemaNS, String arrayName, int itemIndex, String itemValue, PropertyOptions options) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertArrayName(arrayName);
        XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, arrayName);
        XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, false, null);
        if (arrayNode != null) {
            doSetArrayItem(arrayNode, itemIndex, itemValue, options, false);
            return;
        }
        throw new XMPException("Specified array does not exist", 102);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setArrayItem(String schemaNS, String arrayName, int itemIndex, String itemValue) throws XMPException {
        setArrayItem(schemaNS, arrayName, itemIndex, itemValue, null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void insertArrayItem(String schemaNS, String arrayName, int itemIndex, String itemValue, PropertyOptions options) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertArrayName(arrayName);
        XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, arrayName);
        XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, false, null);
        if (arrayNode != null) {
            doSetArrayItem(arrayNode, itemIndex, itemValue, options, true);
            return;
        }
        throw new XMPException("Specified array does not exist", 102);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void insertArrayItem(String schemaNS, String arrayName, int itemIndex, String itemValue) throws XMPException {
        insertArrayItem(schemaNS, arrayName, itemIndex, itemValue, null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setProperty(String schemaNS, String propName, Object propValue, PropertyOptions options) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertPropName(propName);
        PropertyOptions options2 = XMPNodeUtils.verifySetOptions(options, propValue);
        XMPPath expPath = XMPPathParser.expandXPath(schemaNS, propName);
        XMPNode propNode = XMPNodeUtils.findNode(this.tree, expPath, true, options2);
        if (propNode != null) {
            setNode(propNode, propValue, options2, false);
            return;
        }
        throw new XMPException("Specified property does not exist", 102);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setProperty(String schemaNS, String propName, Object propValue) throws XMPException {
        setProperty(schemaNS, propName, propValue, null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setQualifier(String schemaNS, String propName, String qualNS, String qualName, String qualValue, PropertyOptions options) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertPropName(propName);
        if (!doesPropertyExist(schemaNS, propName)) {
            throw new XMPException("Specified property does not exist!", 102);
        }
        String qualPath = propName + XMPPathFactory.composeQualifierPath(qualNS, qualName);
        setProperty(schemaNS, qualPath, qualValue, options);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setQualifier(String schemaNS, String propName, String qualNS, String qualName, String qualValue) throws XMPException {
        setQualifier(schemaNS, propName, qualNS, qualName, qualValue, null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setStructField(String schemaNS, String structName, String fieldNS, String fieldName, String fieldValue, PropertyOptions options) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertStructName(structName);
        String fieldPath = structName + XMPPathFactory.composeStructFieldPath(fieldNS, fieldName);
        setProperty(schemaNS, fieldPath, fieldValue, options);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setStructField(String schemaNS, String structName, String fieldNS, String fieldName, String fieldValue) throws XMPException {
        setStructField(schemaNS, structName, fieldNS, fieldName, fieldValue, null);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public String getObjectName() {
        return this.tree.getName() != null ? this.tree.getName() : "";
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void setObjectName(String name) {
        this.tree.setName(name);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public String getPacketHeader() {
        return this.packetHeader;
    }

    public void setPacketHeader(String packetHeader) {
        this.packetHeader = packetHeader;
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public Object clone() {
        XMPNode clonedTree = (XMPNode) this.tree.clone();
        return new XMPMetaImpl(clonedTree);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public String dumpObject() {
        return getRoot().dumpNode(true);
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void sort() {
        this.tree.sort();
    }

    @Override // com.itextpdf.kernel.xmp.XMPMeta
    public void normalize(ParseOptions options) throws XMPException {
        if (options == null) {
            options = new ParseOptions();
        }
        XMPNormalizer.process(this, options);
    }

    public XMPNode getRoot() {
        return this.tree;
    }

    private void doSetArrayItem(XMPNode arrayNode, int itemIndex, String itemValue, PropertyOptions itemOptions, boolean insert) throws XMPException {
        XMPNode itemNode = new XMPNode("[]", null);
        PropertyOptions itemOptions2 = XMPNodeUtils.verifySetOptions(itemOptions, itemValue);
        int maxIndex = insert ? arrayNode.getChildrenLength() + 1 : arrayNode.getChildrenLength();
        if (itemIndex == -1) {
            itemIndex = maxIndex;
        }
        if (1 <= itemIndex && itemIndex <= maxIndex) {
            if (!insert) {
                arrayNode.removeChild(itemIndex);
            }
            arrayNode.addChild(itemIndex, itemNode);
            setNode(itemNode, itemValue, itemOptions2, false);
            return;
        }
        throw new XMPException("Array index out of bounds", 104);
    }

    void setNode(XMPNode node, Object value, PropertyOptions newOptions, boolean deleteExisting) throws XMPException {
        if (deleteExisting) {
            node.clear();
        }
        node.getOptions().mergeWith(newOptions);
        if (!node.getOptions().isCompositeProperty()) {
            XMPNodeUtils.setNodeValue(node, value);
        } else {
            if (value != null && value.toString().length() > 0) {
                throw new XMPException("Composite nodes can't have values", 102);
            }
            node.removeChildren();
        }
    }

    private Object evaluateNodeValue(int valueType, XMPNode propNode) throws XMPException {
        Object value;
        String rawValue = propNode.getValue();
        switch (valueType) {
            case 0:
            default:
                value = (rawValue != null || propNode.getOptions().isCompositeProperty()) ? rawValue : "";
                break;
            case 1:
                value = Boolean.valueOf(XMPUtils.convertToBoolean(rawValue));
                break;
            case 2:
                value = Integer.valueOf(XMPUtils.convertToInteger(rawValue));
                break;
            case 3:
                value = Long.valueOf(XMPUtils.convertToLong(rawValue));
                break;
            case 4:
                value = Double.valueOf(XMPUtils.convertToDouble(rawValue));
                break;
            case 5:
                value = XMPUtils.convertToDate(rawValue);
                break;
            case 6:
                XMPDateTime dt = XMPUtils.convertToDate(rawValue);
                value = dt.getCalendar();
                break;
            case 7:
                value = XMPUtils.decodeBase64(rawValue);
                break;
        }
        return value;
    }
}
