package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTIconSetImpl.class */
public class CTIconSetImpl extends XmlComplexContentImpl implements CTIconSet {
    private static final QName CFVO$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "cfvo");
    private static final QName ICONSET$2 = new QName("", "iconSet");
    private static final QName SHOWVALUE$4 = new QName("", "showValue");
    private static final QName PERCENT$6 = new QName("", "percent");
    private static final QName REVERSE$8 = new QName("", "reverse");

    public CTIconSetImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public List<CTCfvo> getCfvoList() {
        1CfvoList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1CfvoList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public CTCfvo[] getCfvoArray() {
        CTCfvo[] cTCfvoArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(CFVO$0, arrayList);
            cTCfvoArr = new CTCfvo[arrayList.size()];
            arrayList.toArray(cTCfvoArr);
        }
        return cTCfvoArr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public CTCfvo getCfvoArray(int i) {
        CTCfvo cTCfvo;
        synchronized (monitor()) {
            check_orphaned();
            cTCfvo = (CTCfvo) get_store().find_element_user(CFVO$0, i);
            if (cTCfvo == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTCfvo;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public int sizeOfCfvoArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(CFVO$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void setCfvoArray(CTCfvo[] cTCfvoArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTCfvoArr, CFVO$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void setCfvoArray(int i, CTCfvo cTCfvo) {
        synchronized (monitor()) {
            check_orphaned();
            CTCfvo cTCfvo2 = (CTCfvo) get_store().find_element_user(CFVO$0, i);
            if (cTCfvo2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTCfvo2.set(cTCfvo);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public CTCfvo insertNewCfvo(int i) {
        CTCfvo cTCfvo;
        synchronized (monitor()) {
            check_orphaned();
            cTCfvo = (CTCfvo) get_store().insert_element_user(CFVO$0, i);
        }
        return cTCfvo;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public CTCfvo addNewCfvo() {
        CTCfvo cTCfvo;
        synchronized (monitor()) {
            check_orphaned();
            cTCfvo = (CTCfvo) get_store().add_element_user(CFVO$0);
        }
        return cTCfvo;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void removeCfvo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CFVO$0, i);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public STIconSetType.Enum getIconSet() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ICONSET$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ICONSET$2);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STIconSetType.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public STIconSetType xgetIconSet() {
        STIconSetType sTIconSetType;
        synchronized (monitor()) {
            check_orphaned();
            STIconSetType sTIconSetType2 = (STIconSetType) get_store().find_attribute_user(ICONSET$2);
            if (sTIconSetType2 == null) {
                sTIconSetType2 = (STIconSetType) get_default_attribute_value(ICONSET$2);
            }
            sTIconSetType = sTIconSetType2;
        }
        return sTIconSetType;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public boolean isSetIconSet() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ICONSET$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void setIconSet(STIconSetType.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ICONSET$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ICONSET$2);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void xsetIconSet(STIconSetType sTIconSetType) {
        synchronized (monitor()) {
            check_orphaned();
            STIconSetType sTIconSetType2 = (STIconSetType) get_store().find_attribute_user(ICONSET$2);
            if (sTIconSetType2 == null) {
                sTIconSetType2 = (STIconSetType) get_store().add_attribute_user(ICONSET$2);
            }
            sTIconSetType2.set(sTIconSetType);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void unsetIconSet() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ICONSET$2);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public boolean getShowValue() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWVALUE$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWVALUE$4);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public XmlBoolean xgetShowValue() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWVALUE$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWVALUE$4);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public boolean isSetShowValue() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWVALUE$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void setShowValue(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWVALUE$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWVALUE$4);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void xsetShowValue(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWVALUE$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWVALUE$4);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void unsetShowValue() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWVALUE$4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public boolean getPercent() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PERCENT$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PERCENT$6);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public XmlBoolean xgetPercent() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PERCENT$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PERCENT$6);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public boolean isSetPercent() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PERCENT$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void setPercent(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PERCENT$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PERCENT$6);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void xsetPercent(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PERCENT$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PERCENT$6);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void unsetPercent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PERCENT$6);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public boolean getReverse() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(REVERSE$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(REVERSE$8);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public XmlBoolean xgetReverse() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(REVERSE$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(REVERSE$8);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public boolean isSetReverse() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(REVERSE$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void setReverse(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(REVERSE$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(REVERSE$8);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void xsetReverse(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(REVERSE$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(REVERSE$8);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
    public void unsetReverse() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(REVERSE$8);
        }
    }
}
