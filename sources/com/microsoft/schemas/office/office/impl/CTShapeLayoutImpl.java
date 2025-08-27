package com.microsoft.schemas.office.office.impl;

import com.microsoft.schemas.office.office.CTIdMap;
import com.microsoft.schemas.office.office.CTRegroupTable;
import com.microsoft.schemas.office.office.CTRules;
import com.microsoft.schemas.office.office.CTShapeLayout;
import com.microsoft.schemas.vml.STExt;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/impl/CTShapeLayoutImpl.class */
public class CTShapeLayoutImpl extends XmlComplexContentImpl implements CTShapeLayout {
    private static final QName IDMAP$0 = new QName("urn:schemas-microsoft-com:office:office", "idmap");
    private static final QName REGROUPTABLE$2 = new QName("urn:schemas-microsoft-com:office:office", "regrouptable");
    private static final QName RULES$4 = new QName("urn:schemas-microsoft-com:office:office", "rules");
    private static final QName EXT$6 = new QName("urn:schemas-microsoft-com:vml", "ext");

    public CTShapeLayoutImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public CTIdMap getIdmap() {
        synchronized (monitor()) {
            check_orphaned();
            CTIdMap cTIdMap = (CTIdMap) get_store().find_element_user(IDMAP$0, 0);
            if (cTIdMap == null) {
                return null;
            }
            return cTIdMap;
        }
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public boolean isSetIdmap() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(IDMAP$0) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public void setIdmap(CTIdMap cTIdMap) {
        synchronized (monitor()) {
            check_orphaned();
            CTIdMap cTIdMap2 = (CTIdMap) get_store().find_element_user(IDMAP$0, 0);
            if (cTIdMap2 == null) {
                cTIdMap2 = (CTIdMap) get_store().add_element_user(IDMAP$0);
            }
            cTIdMap2.set(cTIdMap);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public CTIdMap addNewIdmap() {
        CTIdMap cTIdMap;
        synchronized (monitor()) {
            check_orphaned();
            cTIdMap = (CTIdMap) get_store().add_element_user(IDMAP$0);
        }
        return cTIdMap;
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public void unsetIdmap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(IDMAP$0, 0);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public CTRegroupTable getRegrouptable() {
        synchronized (monitor()) {
            check_orphaned();
            CTRegroupTable cTRegroupTableFind_element_user = get_store().find_element_user(REGROUPTABLE$2, 0);
            if (cTRegroupTableFind_element_user == null) {
                return null;
            }
            return cTRegroupTableFind_element_user;
        }
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public boolean isSetRegrouptable() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(REGROUPTABLE$2) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public void setRegrouptable(CTRegroupTable cTRegroupTable) {
        synchronized (monitor()) {
            check_orphaned();
            CTRegroupTable cTRegroupTableFind_element_user = get_store().find_element_user(REGROUPTABLE$2, 0);
            if (cTRegroupTableFind_element_user == null) {
                cTRegroupTableFind_element_user = (CTRegroupTable) get_store().add_element_user(REGROUPTABLE$2);
            }
            cTRegroupTableFind_element_user.set(cTRegroupTable);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public CTRegroupTable addNewRegrouptable() {
        CTRegroupTable cTRegroupTableAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTRegroupTableAdd_element_user = get_store().add_element_user(REGROUPTABLE$2);
        }
        return cTRegroupTableAdd_element_user;
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public void unsetRegrouptable() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(REGROUPTABLE$2, 0);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public CTRules getRules() {
        synchronized (monitor()) {
            check_orphaned();
            CTRules cTRulesFind_element_user = get_store().find_element_user(RULES$4, 0);
            if (cTRulesFind_element_user == null) {
                return null;
            }
            return cTRulesFind_element_user;
        }
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public boolean isSetRules() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(RULES$4) != 0;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public void setRules(CTRules cTRules) {
        synchronized (monitor()) {
            check_orphaned();
            CTRules cTRulesFind_element_user = get_store().find_element_user(RULES$4, 0);
            if (cTRulesFind_element_user == null) {
                cTRulesFind_element_user = (CTRules) get_store().add_element_user(RULES$4);
            }
            cTRulesFind_element_user.set(cTRules);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public CTRules addNewRules() {
        CTRules cTRulesAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTRulesAdd_element_user = get_store().add_element_user(RULES$4);
        }
        return cTRulesAdd_element_user;
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public void unsetRules() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(RULES$4, 0);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public STExt.Enum getExt() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EXT$6);
            if (simpleValue == null) {
                return null;
            }
            return (STExt.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public STExt xgetExt() {
        STExt sTExt;
        synchronized (monitor()) {
            check_orphaned();
            sTExt = (STExt) get_store().find_attribute_user(EXT$6);
        }
        return sTExt;
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public boolean isSetExt() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(EXT$6) != null;
        }
        return z;
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public void setExt(STExt.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EXT$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(EXT$6);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public void xsetExt(STExt sTExt) {
        synchronized (monitor()) {
            check_orphaned();
            STExt sTExt2 = (STExt) get_store().find_attribute_user(EXT$6);
            if (sTExt2 == null) {
                sTExt2 = (STExt) get_store().add_attribute_user(EXT$6);
            }
            sTExt2.set(sTExt);
        }
    }

    @Override // com.microsoft.schemas.office.office.CTShapeLayout
    public void unsetExt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(EXT$6);
        }
    }
}
