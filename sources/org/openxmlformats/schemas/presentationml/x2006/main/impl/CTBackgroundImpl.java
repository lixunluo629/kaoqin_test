package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference;
import org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode;
import org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode$Enum;
import org.openxmlformats.schemas.presentationml.x2006.main.CTBackground;
import org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/impl/CTBackgroundImpl.class */
public class CTBackgroundImpl extends XmlComplexContentImpl implements CTBackground {
    private static final QName BGPR$0 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bgPr");
    private static final QName BGREF$2 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bgRef");
    private static final QName BWMODE$4 = new QName("", "bwMode");

    public CTBackgroundImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public CTBackgroundProperties getBgPr() {
        synchronized (monitor()) {
            check_orphaned();
            CTBackgroundProperties cTBackgroundProperties = (CTBackgroundProperties) get_store().find_element_user(BGPR$0, 0);
            if (cTBackgroundProperties == null) {
                return null;
            }
            return cTBackgroundProperties;
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public boolean isSetBgPr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(BGPR$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public void setBgPr(CTBackgroundProperties cTBackgroundProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTBackgroundProperties cTBackgroundProperties2 = (CTBackgroundProperties) get_store().find_element_user(BGPR$0, 0);
            if (cTBackgroundProperties2 == null) {
                cTBackgroundProperties2 = (CTBackgroundProperties) get_store().add_element_user(BGPR$0);
            }
            cTBackgroundProperties2.set(cTBackgroundProperties);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public CTBackgroundProperties addNewBgPr() {
        CTBackgroundProperties cTBackgroundProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTBackgroundProperties = (CTBackgroundProperties) get_store().add_element_user(BGPR$0);
        }
        return cTBackgroundProperties;
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public void unsetBgPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(BGPR$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public CTStyleMatrixReference getBgRef() {
        synchronized (monitor()) {
            check_orphaned();
            CTStyleMatrixReference cTStyleMatrixReference = (CTStyleMatrixReference) get_store().find_element_user(BGREF$2, 0);
            if (cTStyleMatrixReference == null) {
                return null;
            }
            return cTStyleMatrixReference;
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public boolean isSetBgRef() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(BGREF$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public void setBgRef(CTStyleMatrixReference cTStyleMatrixReference) {
        synchronized (monitor()) {
            check_orphaned();
            CTStyleMatrixReference cTStyleMatrixReference2 = (CTStyleMatrixReference) get_store().find_element_user(BGREF$2, 0);
            if (cTStyleMatrixReference2 == null) {
                cTStyleMatrixReference2 = (CTStyleMatrixReference) get_store().add_element_user(BGREF$2);
            }
            cTStyleMatrixReference2.set(cTStyleMatrixReference);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public CTStyleMatrixReference addNewBgRef() {
        CTStyleMatrixReference cTStyleMatrixReference;
        synchronized (monitor()) {
            check_orphaned();
            cTStyleMatrixReference = (CTStyleMatrixReference) get_store().add_element_user(BGREF$2);
        }
        return cTStyleMatrixReference;
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public void unsetBgRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(BGREF$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public STBlackWhiteMode$Enum getBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BWMODE$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(BWMODE$4);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STBlackWhiteMode$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public STBlackWhiteMode xgetBwMode() {
        STBlackWhiteMode sTBlackWhiteMode;
        synchronized (monitor()) {
            check_orphaned();
            STBlackWhiteMode sTBlackWhiteModeFind_attribute_user = get_store().find_attribute_user(BWMODE$4);
            if (sTBlackWhiteModeFind_attribute_user == null) {
                sTBlackWhiteModeFind_attribute_user = (STBlackWhiteMode) get_default_attribute_value(BWMODE$4);
            }
            sTBlackWhiteMode = sTBlackWhiteModeFind_attribute_user;
        }
        return sTBlackWhiteMode;
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public boolean isSetBwMode() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BWMODE$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public void setBwMode(STBlackWhiteMode$Enum sTBlackWhiteMode$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BWMODE$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BWMODE$4);
            }
            simpleValue.setEnumValue(sTBlackWhiteMode$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public void xsetBwMode(STBlackWhiteMode sTBlackWhiteMode) {
        synchronized (monitor()) {
            check_orphaned();
            STBlackWhiteMode sTBlackWhiteModeFind_attribute_user = get_store().find_attribute_user(BWMODE$4);
            if (sTBlackWhiteModeFind_attribute_user == null) {
                sTBlackWhiteModeFind_attribute_user = (STBlackWhiteMode) get_store().add_attribute_user(BWMODE$4);
            }
            sTBlackWhiteModeFind_attribute_user.set(sTBlackWhiteMode);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
    public void unsetBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BWMODE$4);
        }
    }
}
