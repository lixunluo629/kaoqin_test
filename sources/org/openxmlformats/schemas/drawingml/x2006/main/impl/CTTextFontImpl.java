package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlByte;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
import org.openxmlformats.schemas.drawingml.x2006.main.STPanose;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextTypeface;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTTextFontImpl.class */
public class CTTextFontImpl extends XmlComplexContentImpl implements CTTextFont {
    private static final QName TYPEFACE$0 = new QName("", "typeface");
    private static final QName PANOSE$2 = new QName("", "panose");
    private static final QName PITCHFAMILY$4 = new QName("", "pitchFamily");
    private static final QName CHARSET$6 = new QName("", "charset");

    public CTTextFontImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public String getTypeface() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPEFACE$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public STTextTypeface xgetTypeface() {
        STTextTypeface sTTextTypeface;
        synchronized (monitor()) {
            check_orphaned();
            sTTextTypeface = (STTextTypeface) get_store().find_attribute_user(TYPEFACE$0);
        }
        return sTTextTypeface;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public boolean isSetTypeface() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TYPEFACE$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public void setTypeface(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPEFACE$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TYPEFACE$0);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public void xsetTypeface(STTextTypeface sTTextTypeface) {
        synchronized (monitor()) {
            check_orphaned();
            STTextTypeface sTTextTypeface2 = (STTextTypeface) get_store().find_attribute_user(TYPEFACE$0);
            if (sTTextTypeface2 == null) {
                sTTextTypeface2 = (STTextTypeface) get_store().add_attribute_user(TYPEFACE$0);
            }
            sTTextTypeface2.set(sTTextTypeface);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public void unsetTypeface() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TYPEFACE$0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public byte[] getPanose() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PANOSE$2);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getByteArrayValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public STPanose xgetPanose() {
        STPanose sTPanoseFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTPanoseFind_attribute_user = get_store().find_attribute_user(PANOSE$2);
        }
        return sTPanoseFind_attribute_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public boolean isSetPanose() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PANOSE$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public void setPanose(byte[] bArr) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PANOSE$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PANOSE$2);
            }
            simpleValue.setByteArrayValue(bArr);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public void xsetPanose(STPanose sTPanose) {
        synchronized (monitor()) {
            check_orphaned();
            STPanose sTPanoseFind_attribute_user = get_store().find_attribute_user(PANOSE$2);
            if (sTPanoseFind_attribute_user == null) {
                sTPanoseFind_attribute_user = (STPanose) get_store().add_attribute_user(PANOSE$2);
            }
            sTPanoseFind_attribute_user.set(sTPanose);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public void unsetPanose() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PANOSE$2);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public byte getPitchFamily() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PITCHFAMILY$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PITCHFAMILY$4);
            }
            if (simpleValue == null) {
                return (byte) 0;
            }
            return simpleValue.getByteValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public XmlByte xgetPitchFamily() {
        XmlByte xmlByte;
        synchronized (monitor()) {
            check_orphaned();
            XmlByte xmlByte2 = (XmlByte) get_store().find_attribute_user(PITCHFAMILY$4);
            if (xmlByte2 == null) {
                xmlByte2 = (XmlByte) get_default_attribute_value(PITCHFAMILY$4);
            }
            xmlByte = xmlByte2;
        }
        return xmlByte;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public boolean isSetPitchFamily() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PITCHFAMILY$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public void setPitchFamily(byte b) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PITCHFAMILY$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PITCHFAMILY$4);
            }
            simpleValue.setByteValue(b);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public void xsetPitchFamily(XmlByte xmlByte) {
        synchronized (monitor()) {
            check_orphaned();
            XmlByte xmlByte2 = (XmlByte) get_store().find_attribute_user(PITCHFAMILY$4);
            if (xmlByte2 == null) {
                xmlByte2 = (XmlByte) get_store().add_attribute_user(PITCHFAMILY$4);
            }
            xmlByte2.set(xmlByte);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public void unsetPitchFamily() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PITCHFAMILY$4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public byte getCharset() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CHARSET$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(CHARSET$6);
            }
            if (simpleValue == null) {
                return (byte) 0;
            }
            return simpleValue.getByteValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public XmlByte xgetCharset() {
        XmlByte xmlByte;
        synchronized (monitor()) {
            check_orphaned();
            XmlByte xmlByte2 = (XmlByte) get_store().find_attribute_user(CHARSET$6);
            if (xmlByte2 == null) {
                xmlByte2 = (XmlByte) get_default_attribute_value(CHARSET$6);
            }
            xmlByte = xmlByte2;
        }
        return xmlByte;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public boolean isSetCharset() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CHARSET$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public void setCharset(byte b) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CHARSET$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CHARSET$6);
            }
            simpleValue.setByteValue(b);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public void xsetCharset(XmlByte xmlByte) {
        synchronized (monitor()) {
            check_orphaned();
            XmlByte xmlByte2 = (XmlByte) get_store().find_attribute_user(CHARSET$6);
            if (xmlByte2 == null) {
                xmlByte2 = (XmlByte) get_store().add_attribute_user(CHARSET$6);
            }
            xmlByte2.set(xmlByte);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
    public void unsetCharset() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CHARSET$6);
        }
    }
}
