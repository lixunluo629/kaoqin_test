package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.ss.util.CellUtil;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontId;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment$Enum;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType$Enum;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTPhoneticPrImpl.class */
public class CTPhoneticPrImpl extends XmlComplexContentImpl implements CTPhoneticPr {
    private static final QName FONTID$0 = new QName("", "fontId");
    private static final QName TYPE$2 = new QName("", "type");
    private static final QName ALIGNMENT$4 = new QName("", CellUtil.ALIGNMENT);

    public CTPhoneticPrImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public long getFontId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FONTID$0);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public STFontId xgetFontId() {
        STFontId sTFontId;
        synchronized (monitor()) {
            check_orphaned();
            sTFontId = (STFontId) get_store().find_attribute_user(FONTID$0);
        }
        return sTFontId;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public void setFontId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FONTID$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FONTID$0);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public void xsetFontId(STFontId sTFontId) {
        synchronized (monitor()) {
            check_orphaned();
            STFontId sTFontId2 = (STFontId) get_store().find_attribute_user(FONTID$0);
            if (sTFontId2 == null) {
                sTFontId2 = (STFontId) get_store().add_attribute_user(FONTID$0);
            }
            sTFontId2.set(sTFontId);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public STPhoneticType$Enum getType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPE$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(TYPE$2);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STPhoneticType$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public STPhoneticType xgetType() {
        STPhoneticType sTPhoneticType;
        synchronized (monitor()) {
            check_orphaned();
            STPhoneticType sTPhoneticTypeFind_attribute_user = get_store().find_attribute_user(TYPE$2);
            if (sTPhoneticTypeFind_attribute_user == null) {
                sTPhoneticTypeFind_attribute_user = (STPhoneticType) get_default_attribute_value(TYPE$2);
            }
            sTPhoneticType = sTPhoneticTypeFind_attribute_user;
        }
        return sTPhoneticType;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public boolean isSetType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TYPE$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public void setType(STPhoneticType$Enum sTPhoneticType$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TYPE$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TYPE$2);
            }
            simpleValue.setEnumValue(sTPhoneticType$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public void xsetType(STPhoneticType sTPhoneticType) {
        synchronized (monitor()) {
            check_orphaned();
            STPhoneticType sTPhoneticTypeFind_attribute_user = get_store().find_attribute_user(TYPE$2);
            if (sTPhoneticTypeFind_attribute_user == null) {
                sTPhoneticTypeFind_attribute_user = (STPhoneticType) get_store().add_attribute_user(TYPE$2);
            }
            sTPhoneticTypeFind_attribute_user.set(sTPhoneticType);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public void unsetType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TYPE$2);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public STPhoneticAlignment$Enum getAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALIGNMENT$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ALIGNMENT$4);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STPhoneticAlignment$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public STPhoneticAlignment xgetAlignment() {
        STPhoneticAlignment sTPhoneticAlignment;
        synchronized (monitor()) {
            check_orphaned();
            STPhoneticAlignment sTPhoneticAlignmentFind_attribute_user = get_store().find_attribute_user(ALIGNMENT$4);
            if (sTPhoneticAlignmentFind_attribute_user == null) {
                sTPhoneticAlignmentFind_attribute_user = (STPhoneticAlignment) get_default_attribute_value(ALIGNMENT$4);
            }
            sTPhoneticAlignment = sTPhoneticAlignmentFind_attribute_user;
        }
        return sTPhoneticAlignment;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public boolean isSetAlignment() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ALIGNMENT$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public void setAlignment(STPhoneticAlignment$Enum sTPhoneticAlignment$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALIGNMENT$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ALIGNMENT$4);
            }
            simpleValue.setEnumValue(sTPhoneticAlignment$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public void xsetAlignment(STPhoneticAlignment sTPhoneticAlignment) {
        synchronized (monitor()) {
            check_orphaned();
            STPhoneticAlignment sTPhoneticAlignmentFind_attribute_user = get_store().find_attribute_user(ALIGNMENT$4);
            if (sTPhoneticAlignmentFind_attribute_user == null) {
                sTPhoneticAlignmentFind_attribute_user = (STPhoneticAlignment) get_store().add_attribute_user(ALIGNMENT$4);
            }
            sTPhoneticAlignmentFind_attribute_user.set(sTPhoneticAlignment);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
    public void unsetAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ALIGNMENT$4);
        }
    }
}
