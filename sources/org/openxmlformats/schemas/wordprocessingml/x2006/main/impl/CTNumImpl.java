package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTNumImpl.class */
public class CTNumImpl extends XmlComplexContentImpl implements CTNum {
    private static final QName ABSTRACTNUMID$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "abstractNumId");
    private static final QName LVLOVERRIDE$2 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lvlOverride");
    private static final QName NUMID$4 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numId");

    public CTNumImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public CTDecimalNumber getAbstractNumId() {
        synchronized (monitor()) {
            check_orphaned();
            CTDecimalNumber cTDecimalNumber = (CTDecimalNumber) get_store().find_element_user(ABSTRACTNUMID$0, 0);
            if (cTDecimalNumber == null) {
                return null;
            }
            return cTDecimalNumber;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public void setAbstractNumId(CTDecimalNumber cTDecimalNumber) {
        synchronized (monitor()) {
            check_orphaned();
            CTDecimalNumber cTDecimalNumber2 = (CTDecimalNumber) get_store().find_element_user(ABSTRACTNUMID$0, 0);
            if (cTDecimalNumber2 == null) {
                cTDecimalNumber2 = (CTDecimalNumber) get_store().add_element_user(ABSTRACTNUMID$0);
            }
            cTDecimalNumber2.set(cTDecimalNumber);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public CTDecimalNumber addNewAbstractNumId() {
        CTDecimalNumber cTDecimalNumber;
        synchronized (monitor()) {
            check_orphaned();
            cTDecimalNumber = (CTDecimalNumber) get_store().add_element_user(ABSTRACTNUMID$0);
        }
        return cTDecimalNumber;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public List<CTNumLvl> getLvlOverrideList() {
        1LvlOverrideList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1LvlOverrideList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public CTNumLvl[] getLvlOverrideArray() {
        CTNumLvl[] cTNumLvlArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(LVLOVERRIDE$2, arrayList);
            cTNumLvlArr = new CTNumLvl[arrayList.size()];
            arrayList.toArray(cTNumLvlArr);
        }
        return cTNumLvlArr;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public CTNumLvl getLvlOverrideArray(int i) {
        CTNumLvl cTNumLvl;
        synchronized (monitor()) {
            check_orphaned();
            cTNumLvl = (CTNumLvl) get_store().find_element_user(LVLOVERRIDE$2, i);
            if (cTNumLvl == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTNumLvl;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public int sizeOfLvlOverrideArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(LVLOVERRIDE$2);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public void setLvlOverrideArray(CTNumLvl[] cTNumLvlArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTNumLvlArr, LVLOVERRIDE$2);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public void setLvlOverrideArray(int i, CTNumLvl cTNumLvl) {
        synchronized (monitor()) {
            check_orphaned();
            CTNumLvl cTNumLvl2 = (CTNumLvl) get_store().find_element_user(LVLOVERRIDE$2, i);
            if (cTNumLvl2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTNumLvl2.set(cTNumLvl);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public CTNumLvl insertNewLvlOverride(int i) {
        CTNumLvl cTNumLvl;
        synchronized (monitor()) {
            check_orphaned();
            cTNumLvl = (CTNumLvl) get_store().insert_element_user(LVLOVERRIDE$2, i);
        }
        return cTNumLvl;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public CTNumLvl addNewLvlOverride() {
        CTNumLvl cTNumLvl;
        synchronized (monitor()) {
            check_orphaned();
            cTNumLvl = (CTNumLvl) get_store().add_element_user(LVLOVERRIDE$2);
        }
        return cTNumLvl;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public void removeLvlOverride(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(LVLOVERRIDE$2, i);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public BigInteger getNumId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NUMID$4);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getBigIntegerValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public STDecimalNumber xgetNumId() {
        STDecimalNumber sTDecimalNumber;
        synchronized (monitor()) {
            check_orphaned();
            sTDecimalNumber = (STDecimalNumber) get_store().find_attribute_user(NUMID$4);
        }
        return sTDecimalNumber;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public void setNumId(BigInteger bigInteger) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NUMID$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NUMID$4);
            }
            simpleValue.setBigIntegerValue(bigInteger);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
    public void xsetNumId(STDecimalNumber sTDecimalNumber) {
        synchronized (monitor()) {
            check_orphaned();
            STDecimalNumber sTDecimalNumber2 = (STDecimalNumber) get_store().find_attribute_user(NUMID$4);
            if (sTDecimalNumber2 == null) {
                sTDecimalNumber2 = (STDecimalNumber) get_store().add_attribute_user(NUMID$4);
            }
            sTDecimalNumber2.set(sTDecimalNumber);
        }
    }
}
