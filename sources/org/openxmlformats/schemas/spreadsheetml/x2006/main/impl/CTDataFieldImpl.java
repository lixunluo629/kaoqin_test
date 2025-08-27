package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataConsolidateFunction;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STNumFmtId;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STShowDataAs;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STShowDataAs$Enum;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STXstring;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTDataFieldImpl.class */
public class CTDataFieldImpl extends XmlComplexContentImpl implements CTDataField {
    private static final QName EXTLST$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "extLst");
    private static final QName NAME$2 = new QName("", "name");
    private static final QName FLD$4 = new QName("", "fld");
    private static final QName SUBTOTAL$6 = new QName("", "subtotal");
    private static final QName SHOWDATAAS$8 = new QName("", "showDataAs");
    private static final QName BASEFIELD$10 = new QName("", "baseField");
    private static final QName BASEITEM$12 = new QName("", "baseItem");
    private static final QName NUMFMTID$14 = new QName("", "numFmtId");

    public CTDataFieldImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$0, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$0, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$0);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$0);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$2);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public STXstring xgetName() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(NAME$2);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void setName(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NAME$2);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void xsetName(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(NAME$2);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(NAME$2);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$2);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public long getFld() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FLD$4);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public XmlUnsignedInt xgetFld() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(FLD$4);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void setFld(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FLD$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FLD$4);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void xsetFld(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(FLD$4);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(FLD$4);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public STDataConsolidateFunction.Enum getSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SUBTOTAL$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SUBTOTAL$6);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STDataConsolidateFunction.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public STDataConsolidateFunction xgetSubtotal() {
        STDataConsolidateFunction sTDataConsolidateFunction;
        synchronized (monitor()) {
            check_orphaned();
            STDataConsolidateFunction sTDataConsolidateFunction2 = (STDataConsolidateFunction) get_store().find_attribute_user(SUBTOTAL$6);
            if (sTDataConsolidateFunction2 == null) {
                sTDataConsolidateFunction2 = (STDataConsolidateFunction) get_default_attribute_value(SUBTOTAL$6);
            }
            sTDataConsolidateFunction = sTDataConsolidateFunction2;
        }
        return sTDataConsolidateFunction;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public boolean isSetSubtotal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SUBTOTAL$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void setSubtotal(STDataConsolidateFunction.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SUBTOTAL$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SUBTOTAL$6);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void xsetSubtotal(STDataConsolidateFunction sTDataConsolidateFunction) {
        synchronized (monitor()) {
            check_orphaned();
            STDataConsolidateFunction sTDataConsolidateFunction2 = (STDataConsolidateFunction) get_store().find_attribute_user(SUBTOTAL$6);
            if (sTDataConsolidateFunction2 == null) {
                sTDataConsolidateFunction2 = (STDataConsolidateFunction) get_store().add_attribute_user(SUBTOTAL$6);
            }
            sTDataConsolidateFunction2.set(sTDataConsolidateFunction);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void unsetSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SUBTOTAL$6);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public STShowDataAs$Enum getShowDataAs() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWDATAAS$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWDATAAS$8);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STShowDataAs$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public STShowDataAs xgetShowDataAs() {
        STShowDataAs sTShowDataAs;
        synchronized (monitor()) {
            check_orphaned();
            STShowDataAs sTShowDataAsFind_attribute_user = get_store().find_attribute_user(SHOWDATAAS$8);
            if (sTShowDataAsFind_attribute_user == null) {
                sTShowDataAsFind_attribute_user = (STShowDataAs) get_default_attribute_value(SHOWDATAAS$8);
            }
            sTShowDataAs = sTShowDataAsFind_attribute_user;
        }
        return sTShowDataAs;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public boolean isSetShowDataAs() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWDATAAS$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void setShowDataAs(STShowDataAs$Enum sTShowDataAs$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWDATAAS$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWDATAAS$8);
            }
            simpleValue.setEnumValue(sTShowDataAs$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void xsetShowDataAs(STShowDataAs sTShowDataAs) {
        synchronized (monitor()) {
            check_orphaned();
            STShowDataAs sTShowDataAsFind_attribute_user = get_store().find_attribute_user(SHOWDATAAS$8);
            if (sTShowDataAsFind_attribute_user == null) {
                sTShowDataAsFind_attribute_user = (STShowDataAs) get_store().add_attribute_user(SHOWDATAAS$8);
            }
            sTShowDataAsFind_attribute_user.set(sTShowDataAs);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void unsetShowDataAs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWDATAAS$8);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public int getBaseField() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BASEFIELD$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(BASEFIELD$10);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public XmlInt xgetBaseField() {
        XmlInt xmlInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlInt xmlInt2 = (XmlInt) get_store().find_attribute_user(BASEFIELD$10);
            if (xmlInt2 == null) {
                xmlInt2 = (XmlInt) get_default_attribute_value(BASEFIELD$10);
            }
            xmlInt = xmlInt2;
        }
        return xmlInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public boolean isSetBaseField() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BASEFIELD$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void setBaseField(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BASEFIELD$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BASEFIELD$10);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void xsetBaseField(XmlInt xmlInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlInt xmlInt2 = (XmlInt) get_store().find_attribute_user(BASEFIELD$10);
            if (xmlInt2 == null) {
                xmlInt2 = (XmlInt) get_store().add_attribute_user(BASEFIELD$10);
            }
            xmlInt2.set(xmlInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void unsetBaseField() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BASEFIELD$10);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public long getBaseItem() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BASEITEM$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(BASEITEM$12);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public XmlUnsignedInt xgetBaseItem() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(BASEITEM$12);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(BASEITEM$12);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public boolean isSetBaseItem() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BASEITEM$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void setBaseItem(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BASEITEM$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BASEITEM$12);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void xsetBaseItem(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(BASEITEM$12);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(BASEITEM$12);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void unsetBaseItem() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BASEITEM$12);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public long getNumFmtId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NUMFMTID$14);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public STNumFmtId xgetNumFmtId() {
        STNumFmtId sTNumFmtId;
        synchronized (monitor()) {
            check_orphaned();
            sTNumFmtId = (STNumFmtId) get_store().find_attribute_user(NUMFMTID$14);
        }
        return sTNumFmtId;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public boolean isSetNumFmtId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NUMFMTID$14) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void setNumFmtId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NUMFMTID$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NUMFMTID$14);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void xsetNumFmtId(STNumFmtId sTNumFmtId) {
        synchronized (monitor()) {
            check_orphaned();
            STNumFmtId sTNumFmtId2 = (STNumFmtId) get_store().find_attribute_user(NUMFMTID$14);
            if (sTNumFmtId2 == null) {
                sTNumFmtId2 = (STNumFmtId) get_store().add_attribute_user(NUMFMTID$14);
            }
            sTNumFmtId2.set(sTNumFmtId);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField
    public void unsetNumFmtId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NUMFMTID$14);
        }
    }
}
