package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlUnsignedByte;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTSheetFormatPrImpl.class */
public class CTSheetFormatPrImpl extends XmlComplexContentImpl implements CTSheetFormatPr {
    private static final QName BASECOLWIDTH$0 = new QName("", "baseColWidth");
    private static final QName DEFAULTCOLWIDTH$2 = new QName("", "defaultColWidth");
    private static final QName DEFAULTROWHEIGHT$4 = new QName("", "defaultRowHeight");
    private static final QName CUSTOMHEIGHT$6 = new QName("", "customHeight");
    private static final QName ZEROHEIGHT$8 = new QName("", "zeroHeight");
    private static final QName THICKTOP$10 = new QName("", "thickTop");
    private static final QName THICKBOTTOM$12 = new QName("", "thickBottom");
    private static final QName OUTLINELEVELROW$14 = new QName("", "outlineLevelRow");
    private static final QName OUTLINELEVELCOL$16 = new QName("", "outlineLevelCol");

    public CTSheetFormatPrImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public long getBaseColWidth() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BASECOLWIDTH$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(BASECOLWIDTH$0);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public XmlUnsignedInt xgetBaseColWidth() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(BASECOLWIDTH$0);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(BASECOLWIDTH$0);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public boolean isSetBaseColWidth() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BASECOLWIDTH$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void setBaseColWidth(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BASECOLWIDTH$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BASECOLWIDTH$0);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void xsetBaseColWidth(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(BASECOLWIDTH$0);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(BASECOLWIDTH$0);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void unsetBaseColWidth() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BASECOLWIDTH$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public double getDefaultColWidth() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEFAULTCOLWIDTH$2);
            if (simpleValue == null) {
                return 0.0d;
            }
            return simpleValue.getDoubleValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public XmlDouble xgetDefaultColWidth() {
        XmlDouble xmlDouble;
        synchronized (monitor()) {
            check_orphaned();
            xmlDouble = (XmlDouble) get_store().find_attribute_user(DEFAULTCOLWIDTH$2);
        }
        return xmlDouble;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public boolean isSetDefaultColWidth() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DEFAULTCOLWIDTH$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void setDefaultColWidth(double d) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEFAULTCOLWIDTH$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DEFAULTCOLWIDTH$2);
            }
            simpleValue.setDoubleValue(d);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void xsetDefaultColWidth(XmlDouble xmlDouble) {
        synchronized (monitor()) {
            check_orphaned();
            XmlDouble xmlDouble2 = (XmlDouble) get_store().find_attribute_user(DEFAULTCOLWIDTH$2);
            if (xmlDouble2 == null) {
                xmlDouble2 = (XmlDouble) get_store().add_attribute_user(DEFAULTCOLWIDTH$2);
            }
            xmlDouble2.set(xmlDouble);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void unsetDefaultColWidth() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DEFAULTCOLWIDTH$2);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public double getDefaultRowHeight() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEFAULTROWHEIGHT$4);
            if (simpleValue == null) {
                return 0.0d;
            }
            return simpleValue.getDoubleValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public XmlDouble xgetDefaultRowHeight() {
        XmlDouble xmlDouble;
        synchronized (monitor()) {
            check_orphaned();
            xmlDouble = (XmlDouble) get_store().find_attribute_user(DEFAULTROWHEIGHT$4);
        }
        return xmlDouble;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void setDefaultRowHeight(double d) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEFAULTROWHEIGHT$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DEFAULTROWHEIGHT$4);
            }
            simpleValue.setDoubleValue(d);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void xsetDefaultRowHeight(XmlDouble xmlDouble) {
        synchronized (monitor()) {
            check_orphaned();
            XmlDouble xmlDouble2 = (XmlDouble) get_store().find_attribute_user(DEFAULTROWHEIGHT$4);
            if (xmlDouble2 == null) {
                xmlDouble2 = (XmlDouble) get_store().add_attribute_user(DEFAULTROWHEIGHT$4);
            }
            xmlDouble2.set(xmlDouble);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public boolean getCustomHeight() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CUSTOMHEIGHT$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(CUSTOMHEIGHT$6);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public XmlBoolean xgetCustomHeight() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(CUSTOMHEIGHT$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(CUSTOMHEIGHT$6);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public boolean isSetCustomHeight() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CUSTOMHEIGHT$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void setCustomHeight(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CUSTOMHEIGHT$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CUSTOMHEIGHT$6);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void xsetCustomHeight(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(CUSTOMHEIGHT$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(CUSTOMHEIGHT$6);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void unsetCustomHeight() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CUSTOMHEIGHT$6);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public boolean getZeroHeight() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ZEROHEIGHT$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ZEROHEIGHT$8);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public XmlBoolean xgetZeroHeight() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ZEROHEIGHT$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(ZEROHEIGHT$8);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public boolean isSetZeroHeight() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ZEROHEIGHT$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void setZeroHeight(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ZEROHEIGHT$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ZEROHEIGHT$8);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void xsetZeroHeight(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ZEROHEIGHT$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ZEROHEIGHT$8);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void unsetZeroHeight() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ZEROHEIGHT$8);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public boolean getThickTop() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(THICKTOP$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(THICKTOP$10);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public XmlBoolean xgetThickTop() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(THICKTOP$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(THICKTOP$10);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public boolean isSetThickTop() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(THICKTOP$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void setThickTop(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(THICKTOP$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(THICKTOP$10);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void xsetThickTop(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(THICKTOP$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(THICKTOP$10);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void unsetThickTop() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(THICKTOP$10);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public boolean getThickBottom() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(THICKBOTTOM$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(THICKBOTTOM$12);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public XmlBoolean xgetThickBottom() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(THICKBOTTOM$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(THICKBOTTOM$12);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public boolean isSetThickBottom() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(THICKBOTTOM$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void setThickBottom(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(THICKBOTTOM$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(THICKBOTTOM$12);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void xsetThickBottom(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(THICKBOTTOM$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(THICKBOTTOM$12);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void unsetThickBottom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(THICKBOTTOM$12);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public short getOutlineLevelRow() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OUTLINELEVELROW$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(OUTLINELEVELROW$14);
            }
            if (simpleValue == null) {
                return (short) 0;
            }
            return simpleValue.getShortValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public XmlUnsignedByte xgetOutlineLevelRow() {
        XmlUnsignedByte xmlUnsignedByte;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedByte xmlUnsignedByte2 = (XmlUnsignedByte) get_store().find_attribute_user(OUTLINELEVELROW$14);
            if (xmlUnsignedByte2 == null) {
                xmlUnsignedByte2 = (XmlUnsignedByte) get_default_attribute_value(OUTLINELEVELROW$14);
            }
            xmlUnsignedByte = xmlUnsignedByte2;
        }
        return xmlUnsignedByte;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public boolean isSetOutlineLevelRow() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(OUTLINELEVELROW$14) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void setOutlineLevelRow(short s) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OUTLINELEVELROW$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(OUTLINELEVELROW$14);
            }
            simpleValue.setShortValue(s);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void xsetOutlineLevelRow(XmlUnsignedByte xmlUnsignedByte) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedByte xmlUnsignedByte2 = (XmlUnsignedByte) get_store().find_attribute_user(OUTLINELEVELROW$14);
            if (xmlUnsignedByte2 == null) {
                xmlUnsignedByte2 = (XmlUnsignedByte) get_store().add_attribute_user(OUTLINELEVELROW$14);
            }
            xmlUnsignedByte2.set(xmlUnsignedByte);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void unsetOutlineLevelRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(OUTLINELEVELROW$14);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public short getOutlineLevelCol() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OUTLINELEVELCOL$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(OUTLINELEVELCOL$16);
            }
            if (simpleValue == null) {
                return (short) 0;
            }
            return simpleValue.getShortValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public XmlUnsignedByte xgetOutlineLevelCol() {
        XmlUnsignedByte xmlUnsignedByte;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedByte xmlUnsignedByte2 = (XmlUnsignedByte) get_store().find_attribute_user(OUTLINELEVELCOL$16);
            if (xmlUnsignedByte2 == null) {
                xmlUnsignedByte2 = (XmlUnsignedByte) get_default_attribute_value(OUTLINELEVELCOL$16);
            }
            xmlUnsignedByte = xmlUnsignedByte2;
        }
        return xmlUnsignedByte;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public boolean isSetOutlineLevelCol() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(OUTLINELEVELCOL$16) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void setOutlineLevelCol(short s) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OUTLINELEVELCOL$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(OUTLINELEVELCOL$16);
            }
            simpleValue.setShortValue(s);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void xsetOutlineLevelCol(XmlUnsignedByte xmlUnsignedByte) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedByte xmlUnsignedByte2 = (XmlUnsignedByte) get_store().find_attribute_user(OUTLINELEVELCOL$16);
            if (xmlUnsignedByte2 == null) {
                xmlUnsignedByte2 = (XmlUnsignedByte) get_store().add_attribute_user(OUTLINELEVELCOL$16);
            }
            xmlUnsignedByte2.set(xmlUnsignedByte);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr
    public void unsetOutlineLevelCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(OUTLINELEVELCOL$16);
        }
    }
}
