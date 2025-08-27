package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup;
import org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation;
import org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation$Enum;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTPageSetupImpl.class */
public class CTPageSetupImpl extends XmlComplexContentImpl implements CTPageSetup {
    private static final QName PAPERSIZE$0 = new QName("", "paperSize");
    private static final QName FIRSTPAGENUMBER$2 = new QName("", "firstPageNumber");
    private static final QName ORIENTATION$4 = new QName("", "orientation");
    private static final QName BLACKANDWHITE$6 = new QName("", "blackAndWhite");
    private static final QName DRAFT$8 = new QName("", "draft");
    private static final QName USEFIRSTPAGENUMBER$10 = new QName("", "useFirstPageNumber");
    private static final QName HORIZONTALDPI$12 = new QName("", "horizontalDpi");
    private static final QName VERTICALDPI$14 = new QName("", "verticalDpi");
    private static final QName COPIES$16 = new QName("", "copies");

    public CTPageSetupImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public long getPaperSize() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PAPERSIZE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PAPERSIZE$0);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public XmlUnsignedInt xgetPaperSize() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(PAPERSIZE$0);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(PAPERSIZE$0);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public boolean isSetPaperSize() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PAPERSIZE$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void setPaperSize(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PAPERSIZE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PAPERSIZE$0);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void xsetPaperSize(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(PAPERSIZE$0);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(PAPERSIZE$0);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void unsetPaperSize() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PAPERSIZE$0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public long getFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FIRSTPAGENUMBER$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(FIRSTPAGENUMBER$2);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public XmlUnsignedInt xgetFirstPageNumber() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(FIRSTPAGENUMBER$2);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(FIRSTPAGENUMBER$2);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public boolean isSetFirstPageNumber() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FIRSTPAGENUMBER$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void setFirstPageNumber(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FIRSTPAGENUMBER$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FIRSTPAGENUMBER$2);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void xsetFirstPageNumber(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(FIRSTPAGENUMBER$2);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(FIRSTPAGENUMBER$2);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void unsetFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FIRSTPAGENUMBER$2);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public STPageSetupOrientation$Enum getOrientation() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ORIENTATION$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ORIENTATION$4);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STPageSetupOrientation$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public STPageSetupOrientation xgetOrientation() {
        STPageSetupOrientation sTPageSetupOrientation;
        synchronized (monitor()) {
            check_orphaned();
            STPageSetupOrientation sTPageSetupOrientationFind_attribute_user = get_store().find_attribute_user(ORIENTATION$4);
            if (sTPageSetupOrientationFind_attribute_user == null) {
                sTPageSetupOrientationFind_attribute_user = (STPageSetupOrientation) get_default_attribute_value(ORIENTATION$4);
            }
            sTPageSetupOrientation = sTPageSetupOrientationFind_attribute_user;
        }
        return sTPageSetupOrientation;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public boolean isSetOrientation() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ORIENTATION$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void setOrientation(STPageSetupOrientation$Enum sTPageSetupOrientation$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ORIENTATION$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ORIENTATION$4);
            }
            simpleValue.setEnumValue(sTPageSetupOrientation$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void xsetOrientation(STPageSetupOrientation sTPageSetupOrientation) {
        synchronized (monitor()) {
            check_orphaned();
            STPageSetupOrientation sTPageSetupOrientationFind_attribute_user = get_store().find_attribute_user(ORIENTATION$4);
            if (sTPageSetupOrientationFind_attribute_user == null) {
                sTPageSetupOrientationFind_attribute_user = (STPageSetupOrientation) get_store().add_attribute_user(ORIENTATION$4);
            }
            sTPageSetupOrientationFind_attribute_user.set(sTPageSetupOrientation);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void unsetOrientation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ORIENTATION$4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public boolean getBlackAndWhite() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BLACKANDWHITE$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(BLACKANDWHITE$6);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public XmlBoolean xgetBlackAndWhite() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(BLACKANDWHITE$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(BLACKANDWHITE$6);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public boolean isSetBlackAndWhite() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BLACKANDWHITE$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void setBlackAndWhite(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BLACKANDWHITE$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BLACKANDWHITE$6);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void xsetBlackAndWhite(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(BLACKANDWHITE$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(BLACKANDWHITE$6);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void unsetBlackAndWhite() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BLACKANDWHITE$6);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public boolean getDraft() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAFT$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DRAFT$8);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public XmlBoolean xgetDraft() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAFT$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DRAFT$8);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public boolean isSetDraft() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DRAFT$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void setDraft(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAFT$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DRAFT$8);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void xsetDraft(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAFT$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DRAFT$8);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void unsetDraft() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DRAFT$8);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public boolean getUseFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(USEFIRSTPAGENUMBER$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(USEFIRSTPAGENUMBER$10);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public XmlBoolean xgetUseFirstPageNumber() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(USEFIRSTPAGENUMBER$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(USEFIRSTPAGENUMBER$10);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public boolean isSetUseFirstPageNumber() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(USEFIRSTPAGENUMBER$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void setUseFirstPageNumber(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(USEFIRSTPAGENUMBER$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(USEFIRSTPAGENUMBER$10);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void xsetUseFirstPageNumber(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(USEFIRSTPAGENUMBER$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(USEFIRSTPAGENUMBER$10);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void unsetUseFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(USEFIRSTPAGENUMBER$10);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public int getHorizontalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HORIZONTALDPI$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(HORIZONTALDPI$12);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public XmlInt xgetHorizontalDpi() {
        XmlInt xmlInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlInt xmlInt2 = (XmlInt) get_store().find_attribute_user(HORIZONTALDPI$12);
            if (xmlInt2 == null) {
                xmlInt2 = (XmlInt) get_default_attribute_value(HORIZONTALDPI$12);
            }
            xmlInt = xmlInt2;
        }
        return xmlInt;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public boolean isSetHorizontalDpi() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HORIZONTALDPI$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void setHorizontalDpi(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HORIZONTALDPI$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HORIZONTALDPI$12);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void xsetHorizontalDpi(XmlInt xmlInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlInt xmlInt2 = (XmlInt) get_store().find_attribute_user(HORIZONTALDPI$12);
            if (xmlInt2 == null) {
                xmlInt2 = (XmlInt) get_store().add_attribute_user(HORIZONTALDPI$12);
            }
            xmlInt2.set(xmlInt);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void unsetHorizontalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HORIZONTALDPI$12);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public int getVerticalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VERTICALDPI$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VERTICALDPI$14);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public XmlInt xgetVerticalDpi() {
        XmlInt xmlInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlInt xmlInt2 = (XmlInt) get_store().find_attribute_user(VERTICALDPI$14);
            if (xmlInt2 == null) {
                xmlInt2 = (XmlInt) get_default_attribute_value(VERTICALDPI$14);
            }
            xmlInt = xmlInt2;
        }
        return xmlInt;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public boolean isSetVerticalDpi() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VERTICALDPI$14) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void setVerticalDpi(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VERTICALDPI$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VERTICALDPI$14);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void xsetVerticalDpi(XmlInt xmlInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlInt xmlInt2 = (XmlInt) get_store().find_attribute_user(VERTICALDPI$14);
            if (xmlInt2 == null) {
                xmlInt2 = (XmlInt) get_store().add_attribute_user(VERTICALDPI$14);
            }
            xmlInt2.set(xmlInt);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void unsetVerticalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VERTICALDPI$14);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public long getCopies() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COPIES$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(COPIES$16);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public XmlUnsignedInt xgetCopies() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(COPIES$16);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(COPIES$16);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public boolean isSetCopies() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COPIES$16) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void setCopies(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COPIES$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COPIES$16);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void xsetCopies(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(COPIES$16);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(COPIES$16);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
    public void unsetCopies() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COPIES$16);
        }
    }
}
