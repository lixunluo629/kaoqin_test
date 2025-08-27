package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;
import org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTColorMappingImpl.class */
public class CTColorMappingImpl extends XmlComplexContentImpl implements CTColorMapping {
    private static final QName EXTLST$0 = new QName(XSSFRelation.NS_DRAWINGML, "extLst");
    private static final QName BG1$2 = new QName("", "bg1");
    private static final QName TX1$4 = new QName("", "tx1");
    private static final QName BG2$6 = new QName("", "bg2");
    private static final QName TX2$8 = new QName("", "tx2");
    private static final QName ACCENT1$10 = new QName("", "accent1");
    private static final QName ACCENT2$12 = new QName("", "accent2");
    private static final QName ACCENT3$14 = new QName("", "accent3");
    private static final QName ACCENT4$16 = new QName("", "accent4");
    private static final QName ACCENT5$18 = new QName("", "accent5");
    private static final QName ACCENT6$20 = new QName("", "accent6");
    private static final QName HLINK$22 = new QName("", "hlink");
    private static final QName FOLHLINK$24 = new QName("", "folHlink");

    public CTColorMappingImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public CTOfficeArtExtensionList addNewExtLst() {
        CTOfficeArtExtensionList cTOfficeArtExtensionList;
        synchronized (monitor()) {
            check_orphaned();
            cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$0);
        }
        return cTOfficeArtExtensionList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex.Enum getBg1() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BG1$2);
            if (simpleValue == null) {
                return null;
            }
            return (STColorSchemeIndex.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex xgetBg1() {
        STColorSchemeIndex sTColorSchemeIndex;
        synchronized (monitor()) {
            check_orphaned();
            sTColorSchemeIndex = (STColorSchemeIndex) get_store().find_attribute_user(BG1$2);
        }
        return sTColorSchemeIndex;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void setBg1(STColorSchemeIndex.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BG1$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BG1$2);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void xsetBg1(STColorSchemeIndex sTColorSchemeIndex) {
        synchronized (monitor()) {
            check_orphaned();
            STColorSchemeIndex sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().find_attribute_user(BG1$2);
            if (sTColorSchemeIndex2 == null) {
                sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().add_attribute_user(BG1$2);
            }
            sTColorSchemeIndex2.set(sTColorSchemeIndex);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex.Enum getTx1() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TX1$4);
            if (simpleValue == null) {
                return null;
            }
            return (STColorSchemeIndex.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex xgetTx1() {
        STColorSchemeIndex sTColorSchemeIndex;
        synchronized (monitor()) {
            check_orphaned();
            sTColorSchemeIndex = (STColorSchemeIndex) get_store().find_attribute_user(TX1$4);
        }
        return sTColorSchemeIndex;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void setTx1(STColorSchemeIndex.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TX1$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TX1$4);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void xsetTx1(STColorSchemeIndex sTColorSchemeIndex) {
        synchronized (monitor()) {
            check_orphaned();
            STColorSchemeIndex sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().find_attribute_user(TX1$4);
            if (sTColorSchemeIndex2 == null) {
                sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().add_attribute_user(TX1$4);
            }
            sTColorSchemeIndex2.set(sTColorSchemeIndex);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex.Enum getBg2() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BG2$6);
            if (simpleValue == null) {
                return null;
            }
            return (STColorSchemeIndex.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex xgetBg2() {
        STColorSchemeIndex sTColorSchemeIndex;
        synchronized (monitor()) {
            check_orphaned();
            sTColorSchemeIndex = (STColorSchemeIndex) get_store().find_attribute_user(BG2$6);
        }
        return sTColorSchemeIndex;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void setBg2(STColorSchemeIndex.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BG2$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BG2$6);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void xsetBg2(STColorSchemeIndex sTColorSchemeIndex) {
        synchronized (monitor()) {
            check_orphaned();
            STColorSchemeIndex sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().find_attribute_user(BG2$6);
            if (sTColorSchemeIndex2 == null) {
                sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().add_attribute_user(BG2$6);
            }
            sTColorSchemeIndex2.set(sTColorSchemeIndex);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex.Enum getTx2() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TX2$8);
            if (simpleValue == null) {
                return null;
            }
            return (STColorSchemeIndex.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex xgetTx2() {
        STColorSchemeIndex sTColorSchemeIndex;
        synchronized (monitor()) {
            check_orphaned();
            sTColorSchemeIndex = (STColorSchemeIndex) get_store().find_attribute_user(TX2$8);
        }
        return sTColorSchemeIndex;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void setTx2(STColorSchemeIndex.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TX2$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TX2$8);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void xsetTx2(STColorSchemeIndex sTColorSchemeIndex) {
        synchronized (monitor()) {
            check_orphaned();
            STColorSchemeIndex sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().find_attribute_user(TX2$8);
            if (sTColorSchemeIndex2 == null) {
                sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().add_attribute_user(TX2$8);
            }
            sTColorSchemeIndex2.set(sTColorSchemeIndex);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex.Enum getAccent1() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACCENT1$10);
            if (simpleValue == null) {
                return null;
            }
            return (STColorSchemeIndex.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex xgetAccent1() {
        STColorSchemeIndex sTColorSchemeIndex;
        synchronized (monitor()) {
            check_orphaned();
            sTColorSchemeIndex = (STColorSchemeIndex) get_store().find_attribute_user(ACCENT1$10);
        }
        return sTColorSchemeIndex;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void setAccent1(STColorSchemeIndex.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACCENT1$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ACCENT1$10);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void xsetAccent1(STColorSchemeIndex sTColorSchemeIndex) {
        synchronized (monitor()) {
            check_orphaned();
            STColorSchemeIndex sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().find_attribute_user(ACCENT1$10);
            if (sTColorSchemeIndex2 == null) {
                sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().add_attribute_user(ACCENT1$10);
            }
            sTColorSchemeIndex2.set(sTColorSchemeIndex);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex.Enum getAccent2() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACCENT2$12);
            if (simpleValue == null) {
                return null;
            }
            return (STColorSchemeIndex.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex xgetAccent2() {
        STColorSchemeIndex sTColorSchemeIndex;
        synchronized (monitor()) {
            check_orphaned();
            sTColorSchemeIndex = (STColorSchemeIndex) get_store().find_attribute_user(ACCENT2$12);
        }
        return sTColorSchemeIndex;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void setAccent2(STColorSchemeIndex.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACCENT2$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ACCENT2$12);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void xsetAccent2(STColorSchemeIndex sTColorSchemeIndex) {
        synchronized (monitor()) {
            check_orphaned();
            STColorSchemeIndex sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().find_attribute_user(ACCENT2$12);
            if (sTColorSchemeIndex2 == null) {
                sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().add_attribute_user(ACCENT2$12);
            }
            sTColorSchemeIndex2.set(sTColorSchemeIndex);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex.Enum getAccent3() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACCENT3$14);
            if (simpleValue == null) {
                return null;
            }
            return (STColorSchemeIndex.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex xgetAccent3() {
        STColorSchemeIndex sTColorSchemeIndex;
        synchronized (monitor()) {
            check_orphaned();
            sTColorSchemeIndex = (STColorSchemeIndex) get_store().find_attribute_user(ACCENT3$14);
        }
        return sTColorSchemeIndex;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void setAccent3(STColorSchemeIndex.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACCENT3$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ACCENT3$14);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void xsetAccent3(STColorSchemeIndex sTColorSchemeIndex) {
        synchronized (monitor()) {
            check_orphaned();
            STColorSchemeIndex sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().find_attribute_user(ACCENT3$14);
            if (sTColorSchemeIndex2 == null) {
                sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().add_attribute_user(ACCENT3$14);
            }
            sTColorSchemeIndex2.set(sTColorSchemeIndex);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex.Enum getAccent4() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACCENT4$16);
            if (simpleValue == null) {
                return null;
            }
            return (STColorSchemeIndex.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex xgetAccent4() {
        STColorSchemeIndex sTColorSchemeIndex;
        synchronized (monitor()) {
            check_orphaned();
            sTColorSchemeIndex = (STColorSchemeIndex) get_store().find_attribute_user(ACCENT4$16);
        }
        return sTColorSchemeIndex;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void setAccent4(STColorSchemeIndex.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACCENT4$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ACCENT4$16);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void xsetAccent4(STColorSchemeIndex sTColorSchemeIndex) {
        synchronized (monitor()) {
            check_orphaned();
            STColorSchemeIndex sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().find_attribute_user(ACCENT4$16);
            if (sTColorSchemeIndex2 == null) {
                sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().add_attribute_user(ACCENT4$16);
            }
            sTColorSchemeIndex2.set(sTColorSchemeIndex);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex.Enum getAccent5() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACCENT5$18);
            if (simpleValue == null) {
                return null;
            }
            return (STColorSchemeIndex.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex xgetAccent5() {
        STColorSchemeIndex sTColorSchemeIndex;
        synchronized (monitor()) {
            check_orphaned();
            sTColorSchemeIndex = (STColorSchemeIndex) get_store().find_attribute_user(ACCENT5$18);
        }
        return sTColorSchemeIndex;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void setAccent5(STColorSchemeIndex.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACCENT5$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ACCENT5$18);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void xsetAccent5(STColorSchemeIndex sTColorSchemeIndex) {
        synchronized (monitor()) {
            check_orphaned();
            STColorSchemeIndex sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().find_attribute_user(ACCENT5$18);
            if (sTColorSchemeIndex2 == null) {
                sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().add_attribute_user(ACCENT5$18);
            }
            sTColorSchemeIndex2.set(sTColorSchemeIndex);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex.Enum getAccent6() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACCENT6$20);
            if (simpleValue == null) {
                return null;
            }
            return (STColorSchemeIndex.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex xgetAccent6() {
        STColorSchemeIndex sTColorSchemeIndex;
        synchronized (monitor()) {
            check_orphaned();
            sTColorSchemeIndex = (STColorSchemeIndex) get_store().find_attribute_user(ACCENT6$20);
        }
        return sTColorSchemeIndex;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void setAccent6(STColorSchemeIndex.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ACCENT6$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ACCENT6$20);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void xsetAccent6(STColorSchemeIndex sTColorSchemeIndex) {
        synchronized (monitor()) {
            check_orphaned();
            STColorSchemeIndex sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().find_attribute_user(ACCENT6$20);
            if (sTColorSchemeIndex2 == null) {
                sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().add_attribute_user(ACCENT6$20);
            }
            sTColorSchemeIndex2.set(sTColorSchemeIndex);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex.Enum getHlink() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HLINK$22);
            if (simpleValue == null) {
                return null;
            }
            return (STColorSchemeIndex.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex xgetHlink() {
        STColorSchemeIndex sTColorSchemeIndex;
        synchronized (monitor()) {
            check_orphaned();
            sTColorSchemeIndex = (STColorSchemeIndex) get_store().find_attribute_user(HLINK$22);
        }
        return sTColorSchemeIndex;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void setHlink(STColorSchemeIndex.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HLINK$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HLINK$22);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void xsetHlink(STColorSchemeIndex sTColorSchemeIndex) {
        synchronized (monitor()) {
            check_orphaned();
            STColorSchemeIndex sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().find_attribute_user(HLINK$22);
            if (sTColorSchemeIndex2 == null) {
                sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().add_attribute_user(HLINK$22);
            }
            sTColorSchemeIndex2.set(sTColorSchemeIndex);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex.Enum getFolHlink() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FOLHLINK$24);
            if (simpleValue == null) {
                return null;
            }
            return (STColorSchemeIndex.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public STColorSchemeIndex xgetFolHlink() {
        STColorSchemeIndex sTColorSchemeIndex;
        synchronized (monitor()) {
            check_orphaned();
            sTColorSchemeIndex = (STColorSchemeIndex) get_store().find_attribute_user(FOLHLINK$24);
        }
        return sTColorSchemeIndex;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void setFolHlink(STColorSchemeIndex.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FOLHLINK$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FOLHLINK$24);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
    public void xsetFolHlink(STColorSchemeIndex sTColorSchemeIndex) {
        synchronized (monitor()) {
            check_orphaned();
            STColorSchemeIndex sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().find_attribute_user(FOLHLINK$24);
            if (sTColorSchemeIndex2 == null) {
                sTColorSchemeIndex2 = (STColorSchemeIndex) get_store().add_attribute_user(FOLHLINK$24);
            }
            sTColorSchemeIndex2.set(sTColorSchemeIndex);
        }
    }
}
