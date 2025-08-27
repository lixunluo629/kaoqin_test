package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTTableCellImpl.class */
public class CTTableCellImpl extends XmlComplexContentImpl implements CTTableCell {
    private static final QName TXBODY$0 = new QName(XSSFRelation.NS_DRAWINGML, "txBody");
    private static final QName TCPR$2 = new QName(XSSFRelation.NS_DRAWINGML, "tcPr");
    private static final QName EXTLST$4 = new QName(XSSFRelation.NS_DRAWINGML, "extLst");
    private static final QName ROWSPAN$6 = new QName("", "rowSpan");
    private static final QName GRIDSPAN$8 = new QName("", "gridSpan");
    private static final QName HMERGE$10 = new QName("", "hMerge");
    private static final QName VMERGE$12 = new QName("", "vMerge");

    public CTTableCellImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public CTTextBody getTxBody() {
        synchronized (monitor()) {
            check_orphaned();
            CTTextBody cTTextBody = (CTTextBody) get_store().find_element_user(TXBODY$0, 0);
            if (cTTextBody == null) {
                return null;
            }
            return cTTextBody;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public boolean isSetTxBody() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(TXBODY$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void setTxBody(CTTextBody cTTextBody) {
        synchronized (monitor()) {
            check_orphaned();
            CTTextBody cTTextBody2 = (CTTextBody) get_store().find_element_user(TXBODY$0, 0);
            if (cTTextBody2 == null) {
                cTTextBody2 = (CTTextBody) get_store().add_element_user(TXBODY$0);
            }
            cTTextBody2.set(cTTextBody);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public CTTextBody addNewTxBody() {
        CTTextBody cTTextBody;
        synchronized (monitor()) {
            check_orphaned();
            cTTextBody = (CTTextBody) get_store().add_element_user(TXBODY$0);
        }
        return cTTextBody;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void unsetTxBody() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TXBODY$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public CTTableCellProperties getTcPr() {
        synchronized (monitor()) {
            check_orphaned();
            CTTableCellProperties cTTableCellProperties = (CTTableCellProperties) get_store().find_element_user(TCPR$2, 0);
            if (cTTableCellProperties == null) {
                return null;
            }
            return cTTableCellProperties;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public boolean isSetTcPr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(TCPR$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void setTcPr(CTTableCellProperties cTTableCellProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTTableCellProperties cTTableCellProperties2 = (CTTableCellProperties) get_store().find_element_user(TCPR$2, 0);
            if (cTTableCellProperties2 == null) {
                cTTableCellProperties2 = (CTTableCellProperties) get_store().add_element_user(TCPR$2);
            }
            cTTableCellProperties2.set(cTTableCellProperties);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public CTTableCellProperties addNewTcPr() {
        CTTableCellProperties cTTableCellProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTTableCellProperties = (CTTableCellProperties) get_store().add_element_user(TCPR$2);
        }
        return cTTableCellProperties;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void unsetTcPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TCPR$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$4, 0);
            if (cTOfficeArtExtensionList == null) {
                return null;
            }
            return cTOfficeArtExtensionList;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$4, 0);
            if (cTOfficeArtExtensionList2 == null) {
                cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$4);
            }
            cTOfficeArtExtensionList2.set(cTOfficeArtExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public CTOfficeArtExtensionList addNewExtLst() {
        CTOfficeArtExtensionList cTOfficeArtExtensionList;
        synchronized (monitor()) {
            check_orphaned();
            cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$4);
        }
        return cTOfficeArtExtensionList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public int getRowSpan() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ROWSPAN$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ROWSPAN$6);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public XmlInt xgetRowSpan() {
        XmlInt xmlInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlInt xmlInt2 = (XmlInt) get_store().find_attribute_user(ROWSPAN$6);
            if (xmlInt2 == null) {
                xmlInt2 = (XmlInt) get_default_attribute_value(ROWSPAN$6);
            }
            xmlInt = xmlInt2;
        }
        return xmlInt;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public boolean isSetRowSpan() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ROWSPAN$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void setRowSpan(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ROWSPAN$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ROWSPAN$6);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void xsetRowSpan(XmlInt xmlInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlInt xmlInt2 = (XmlInt) get_store().find_attribute_user(ROWSPAN$6);
            if (xmlInt2 == null) {
                xmlInt2 = (XmlInt) get_store().add_attribute_user(ROWSPAN$6);
            }
            xmlInt2.set(xmlInt);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void unsetRowSpan() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ROWSPAN$6);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public int getGridSpan() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(GRIDSPAN$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(GRIDSPAN$8);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public XmlInt xgetGridSpan() {
        XmlInt xmlInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlInt xmlInt2 = (XmlInt) get_store().find_attribute_user(GRIDSPAN$8);
            if (xmlInt2 == null) {
                xmlInt2 = (XmlInt) get_default_attribute_value(GRIDSPAN$8);
            }
            xmlInt = xmlInt2;
        }
        return xmlInt;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public boolean isSetGridSpan() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(GRIDSPAN$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void setGridSpan(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(GRIDSPAN$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(GRIDSPAN$8);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void xsetGridSpan(XmlInt xmlInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlInt xmlInt2 = (XmlInt) get_store().find_attribute_user(GRIDSPAN$8);
            if (xmlInt2 == null) {
                xmlInt2 = (XmlInt) get_store().add_attribute_user(GRIDSPAN$8);
            }
            xmlInt2.set(xmlInt);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void unsetGridSpan() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(GRIDSPAN$8);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public boolean getHMerge() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HMERGE$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(HMERGE$10);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public XmlBoolean xgetHMerge() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HMERGE$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(HMERGE$10);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public boolean isSetHMerge() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HMERGE$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void setHMerge(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HMERGE$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HMERGE$10);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void xsetHMerge(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HMERGE$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(HMERGE$10);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void unsetHMerge() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HMERGE$10);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public boolean getVMerge() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VMERGE$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VMERGE$12);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public XmlBoolean xgetVMerge() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(VMERGE$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(VMERGE$12);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public boolean isSetVMerge() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VMERGE$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void setVMerge(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VMERGE$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VMERGE$12);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void xsetVMerge(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(VMERGE$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(VMERGE$12);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell
    public void unsetVMerge() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VMERGE$12);
        }
    }
}
