package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTGraphicalObjectFrameLockingImpl.class */
public class CTGraphicalObjectFrameLockingImpl extends XmlComplexContentImpl implements CTGraphicalObjectFrameLocking {
    private static final QName EXTLST$0 = new QName(XSSFRelation.NS_DRAWINGML, "extLst");
    private static final QName NOGRP$2 = new QName("", "noGrp");
    private static final QName NODRILLDOWN$4 = new QName("", "noDrilldown");
    private static final QName NOSELECT$6 = new QName("", "noSelect");
    private static final QName NOCHANGEASPECT$8 = new QName("", "noChangeAspect");
    private static final QName NOMOVE$10 = new QName("", "noMove");
    private static final QName NORESIZE$12 = new QName("", "noResize");

    public CTGraphicalObjectFrameLockingImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$0, 0);
            if (cTOfficeArtExtensionList == null) {
                return null;
            }
            return cTOfficeArtExtensionList;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$0, 0);
            if (cTOfficeArtExtensionList2 == null) {
                cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$0);
            }
            cTOfficeArtExtensionList2.set(cTOfficeArtExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public CTOfficeArtExtensionList addNewExtLst() {
        CTOfficeArtExtensionList cTOfficeArtExtensionList;
        synchronized (monitor()) {
            check_orphaned();
            cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$0);
        }
        return cTOfficeArtExtensionList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public boolean getNoGrp() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NOGRP$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(NOGRP$2);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public XmlBoolean xgetNoGrp() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NOGRP$2);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(NOGRP$2);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public boolean isSetNoGrp() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NOGRP$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void setNoGrp(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NOGRP$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NOGRP$2);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void xsetNoGrp(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NOGRP$2);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(NOGRP$2);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void unsetNoGrp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NOGRP$2);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public boolean getNoDrilldown() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NODRILLDOWN$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(NODRILLDOWN$4);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public XmlBoolean xgetNoDrilldown() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NODRILLDOWN$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(NODRILLDOWN$4);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public boolean isSetNoDrilldown() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NODRILLDOWN$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void setNoDrilldown(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NODRILLDOWN$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NODRILLDOWN$4);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void xsetNoDrilldown(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NODRILLDOWN$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(NODRILLDOWN$4);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void unsetNoDrilldown() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NODRILLDOWN$4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public boolean getNoSelect() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NOSELECT$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(NOSELECT$6);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public XmlBoolean xgetNoSelect() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NOSELECT$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(NOSELECT$6);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public boolean isSetNoSelect() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NOSELECT$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void setNoSelect(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NOSELECT$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NOSELECT$6);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void xsetNoSelect(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NOSELECT$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(NOSELECT$6);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void unsetNoSelect() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NOSELECT$6);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public boolean getNoChangeAspect() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NOCHANGEASPECT$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(NOCHANGEASPECT$8);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public XmlBoolean xgetNoChangeAspect() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NOCHANGEASPECT$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(NOCHANGEASPECT$8);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public boolean isSetNoChangeAspect() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NOCHANGEASPECT$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void setNoChangeAspect(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NOCHANGEASPECT$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NOCHANGEASPECT$8);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void xsetNoChangeAspect(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NOCHANGEASPECT$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(NOCHANGEASPECT$8);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void unsetNoChangeAspect() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NOCHANGEASPECT$8);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public boolean getNoMove() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NOMOVE$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(NOMOVE$10);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public XmlBoolean xgetNoMove() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NOMOVE$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(NOMOVE$10);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public boolean isSetNoMove() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NOMOVE$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void setNoMove(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NOMOVE$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NOMOVE$10);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void xsetNoMove(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NOMOVE$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(NOMOVE$10);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void unsetNoMove() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NOMOVE$10);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public boolean getNoResize() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NORESIZE$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(NORESIZE$12);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public XmlBoolean xgetNoResize() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NORESIZE$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(NORESIZE$12);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public boolean isSetNoResize() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NORESIZE$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void setNoResize(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NORESIZE$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NORESIZE$12);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void xsetNoResize(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NORESIZE$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(NORESIZE$12);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking
    public void unsetNoResize() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NORESIZE$12);
        }
    }
}
