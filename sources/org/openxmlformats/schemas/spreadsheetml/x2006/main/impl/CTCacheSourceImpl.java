package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConsolidation;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheetSource;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSourceType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTCacheSourceImpl.class */
public class CTCacheSourceImpl extends XmlComplexContentImpl implements CTCacheSource {
    private static final QName WORKSHEETSOURCE$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "worksheetSource");
    private static final QName CONSOLIDATION$2 = new QName(XSSFRelation.NS_SPREADSHEETML, "consolidation");
    private static final QName EXTLST$4 = new QName(XSSFRelation.NS_SPREADSHEETML, "extLst");
    private static final QName TYPE$6 = new QName("", "type");
    private static final QName CONNECTIONID$8 = new QName("", "connectionId");

    public CTCacheSourceImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public CTWorksheetSource getWorksheetSource() {
        synchronized (monitor()) {
            check_orphaned();
            CTWorksheetSource cTWorksheetSource = (CTWorksheetSource) get_store().find_element_user(WORKSHEETSOURCE$0, 0);
            if (cTWorksheetSource == null) {
                return null;
            }
            return cTWorksheetSource;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public boolean isSetWorksheetSource() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(WORKSHEETSOURCE$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public void setWorksheetSource(CTWorksheetSource cTWorksheetSource) {
        synchronized (monitor()) {
            check_orphaned();
            CTWorksheetSource cTWorksheetSource2 = (CTWorksheetSource) get_store().find_element_user(WORKSHEETSOURCE$0, 0);
            if (cTWorksheetSource2 == null) {
                cTWorksheetSource2 = (CTWorksheetSource) get_store().add_element_user(WORKSHEETSOURCE$0);
            }
            cTWorksheetSource2.set(cTWorksheetSource);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public CTWorksheetSource addNewWorksheetSource() {
        CTWorksheetSource cTWorksheetSource;
        synchronized (monitor()) {
            check_orphaned();
            cTWorksheetSource = (CTWorksheetSource) get_store().add_element_user(WORKSHEETSOURCE$0);
        }
        return cTWorksheetSource;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public void unsetWorksheetSource() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(WORKSHEETSOURCE$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public CTConsolidation getConsolidation() {
        synchronized (monitor()) {
            check_orphaned();
            CTConsolidation cTConsolidationFind_element_user = get_store().find_element_user(CONSOLIDATION$2, 0);
            if (cTConsolidationFind_element_user == null) {
                return null;
            }
            return cTConsolidationFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public boolean isSetConsolidation() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(CONSOLIDATION$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public void setConsolidation(CTConsolidation cTConsolidation) {
        synchronized (monitor()) {
            check_orphaned();
            CTConsolidation cTConsolidationFind_element_user = get_store().find_element_user(CONSOLIDATION$2, 0);
            if (cTConsolidationFind_element_user == null) {
                cTConsolidationFind_element_user = (CTConsolidation) get_store().add_element_user(CONSOLIDATION$2);
            }
            cTConsolidationFind_element_user.set(cTConsolidation);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public CTConsolidation addNewConsolidation() {
        CTConsolidation cTConsolidationAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTConsolidationAdd_element_user = get_store().add_element_user(CONSOLIDATION$2);
        }
        return cTConsolidationAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public void unsetConsolidation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CONSOLIDATION$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$4, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$4, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$4);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$4);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public STSourceType.Enum getType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPE$6);
            if (simpleValue == null) {
                return null;
            }
            return (STSourceType.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public STSourceType xgetType() {
        STSourceType sTSourceType;
        synchronized (monitor()) {
            check_orphaned();
            sTSourceType = (STSourceType) get_store().find_attribute_user(TYPE$6);
        }
        return sTSourceType;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public void setType(STSourceType.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPE$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TYPE$6);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public void xsetType(STSourceType sTSourceType) {
        synchronized (monitor()) {
            check_orphaned();
            STSourceType sTSourceType2 = (STSourceType) get_store().find_attribute_user(TYPE$6);
            if (sTSourceType2 == null) {
                sTSourceType2 = (STSourceType) get_store().add_attribute_user(TYPE$6);
            }
            sTSourceType2.set(sTSourceType);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public long getConnectionId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CONNECTIONID$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(CONNECTIONID$8);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public XmlUnsignedInt xgetConnectionId() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(CONNECTIONID$8);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(CONNECTIONID$8);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public boolean isSetConnectionId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CONNECTIONID$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public void setConnectionId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CONNECTIONID$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CONNECTIONID$8);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public void xsetConnectionId(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(CONNECTIONID$8);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(CONNECTIONID$8);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource
    public void unsetConnectionId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CONNECTIONID$8);
        }
    }
}
