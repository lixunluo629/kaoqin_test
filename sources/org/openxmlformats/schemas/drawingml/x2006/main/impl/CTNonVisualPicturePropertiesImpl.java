package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTNonVisualPicturePropertiesImpl.class */
public class CTNonVisualPicturePropertiesImpl extends XmlComplexContentImpl implements CTNonVisualPictureProperties {
    private static final QName PICLOCKS$0 = new QName(XSSFRelation.NS_DRAWINGML, "picLocks");
    private static final QName EXTLST$2 = new QName(XSSFRelation.NS_DRAWINGML, "extLst");
    private static final QName PREFERRELATIVERESIZE$4 = new QName("", "preferRelativeResize");

    public CTNonVisualPicturePropertiesImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public CTPictureLocking getPicLocks() {
        synchronized (monitor()) {
            check_orphaned();
            CTPictureLocking cTPictureLocking = (CTPictureLocking) get_store().find_element_user(PICLOCKS$0, 0);
            if (cTPictureLocking == null) {
                return null;
            }
            return cTPictureLocking;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public boolean isSetPicLocks() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PICLOCKS$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public void setPicLocks(CTPictureLocking cTPictureLocking) {
        synchronized (monitor()) {
            check_orphaned();
            CTPictureLocking cTPictureLocking2 = (CTPictureLocking) get_store().find_element_user(PICLOCKS$0, 0);
            if (cTPictureLocking2 == null) {
                cTPictureLocking2 = (CTPictureLocking) get_store().add_element_user(PICLOCKS$0);
            }
            cTPictureLocking2.set(cTPictureLocking);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public CTPictureLocking addNewPicLocks() {
        CTPictureLocking cTPictureLocking;
        synchronized (monitor()) {
            check_orphaned();
            cTPictureLocking = (CTPictureLocking) get_store().add_element_user(PICLOCKS$0);
        }
        return cTPictureLocking;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public void unsetPicLocks() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PICLOCKS$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$2, 0);
            if (cTOfficeArtExtensionList == null) {
                return null;
            }
            return cTOfficeArtExtensionList;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$2, 0);
            if (cTOfficeArtExtensionList2 == null) {
                cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$2);
            }
            cTOfficeArtExtensionList2.set(cTOfficeArtExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public CTOfficeArtExtensionList addNewExtLst() {
        CTOfficeArtExtensionList cTOfficeArtExtensionList;
        synchronized (monitor()) {
            check_orphaned();
            cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$2);
        }
        return cTOfficeArtExtensionList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public boolean getPreferRelativeResize() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PREFERRELATIVERESIZE$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PREFERRELATIVERESIZE$4);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public XmlBoolean xgetPreferRelativeResize() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PREFERRELATIVERESIZE$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PREFERRELATIVERESIZE$4);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public boolean isSetPreferRelativeResize() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PREFERRELATIVERESIZE$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public void setPreferRelativeResize(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PREFERRELATIVERESIZE$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PREFERRELATIVERESIZE$4);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public void xsetPreferRelativeResize(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PREFERRELATIVERESIZE$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PREFERRELATIVERESIZE$4);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
    public void unsetPreferRelativeResize() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PREFERRELATIVERESIZE$4);
        }
    }
}
