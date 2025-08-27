package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import com.alibaba.excel.constant.ExcelXmlConstants;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlUnsignedByte;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellSpans;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTRowImpl.class */
public class CTRowImpl extends XmlComplexContentImpl implements CTRow {
    private static final QName C$0 = new QName(XSSFRelation.NS_SPREADSHEETML, ExcelXmlConstants.CELL_TAG);
    private static final QName EXTLST$2 = new QName(XSSFRelation.NS_SPREADSHEETML, "extLst");
    private static final QName R$4 = new QName("", ExcelXmlConstants.POSITION);
    private static final QName SPANS$6 = new QName("", "spans");
    private static final QName S$8 = new QName("", ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
    private static final QName CUSTOMFORMAT$10 = new QName("", "customFormat");
    private static final QName HT$12 = new QName("", "ht");
    private static final QName HIDDEN$14 = new QName("", CellUtil.HIDDEN);
    private static final QName CUSTOMHEIGHT$16 = new QName("", "customHeight");
    private static final QName OUTLINELEVEL$18 = new QName("", "outlineLevel");
    private static final QName COLLAPSED$20 = new QName("", "collapsed");
    private static final QName THICKTOP$22 = new QName("", "thickTop");
    private static final QName THICKBOT$24 = new QName("", "thickBot");
    private static final QName PH$26 = new QName("", "ph");

    public CTRowImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public List<CTCell> getCList() {
        1CList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1CList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public CTCell[] getCArray() {
        CTCell[] cTCellArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(C$0, arrayList);
            cTCellArr = new CTCell[arrayList.size()];
            arrayList.toArray(cTCellArr);
        }
        return cTCellArr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public CTCell getCArray(int i) {
        CTCell cTCell;
        synchronized (monitor()) {
            check_orphaned();
            cTCell = (CTCell) get_store().find_element_user(C$0, i);
            if (cTCell == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTCell;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public int sizeOfCArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(C$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setCArray(CTCell[] cTCellArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTCellArr, C$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setCArray(int i, CTCell cTCell) {
        synchronized (monitor()) {
            check_orphaned();
            CTCell cTCell2 = (CTCell) get_store().find_element_user(C$0, i);
            if (cTCell2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTCell2.set(cTCell);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public CTCell insertNewC(int i) {
        CTCell cTCell;
        synchronized (monitor()) {
            check_orphaned();
            cTCell = (CTCell) get_store().insert_element_user(C$0, i);
        }
        return cTCell;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public CTCell addNewC() {
        CTCell cTCell;
        synchronized (monitor()) {
            check_orphaned();
            cTCell = (CTCell) get_store().add_element_user(C$0);
        }
        return cTCell;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void removeC(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(C$0, i);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$2, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$2, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$2);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$2);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public long getR() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(R$4);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public XmlUnsignedInt xgetR() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(R$4);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean isSetR() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(R$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setR(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(R$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(R$4);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void xsetR(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(R$4);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(R$4);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void unsetR() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(R$4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public List getSpans() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SPANS$6);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getListValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public STCellSpans xgetSpans() {
        STCellSpans sTCellSpansFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTCellSpansFind_attribute_user = get_store().find_attribute_user(SPANS$6);
        }
        return sTCellSpansFind_attribute_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean isSetSpans() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SPANS$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setSpans(List list) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SPANS$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SPANS$6);
            }
            simpleValue.setListValue(list);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void xsetSpans(STCellSpans sTCellSpans) {
        synchronized (monitor()) {
            check_orphaned();
            STCellSpans sTCellSpansFind_attribute_user = get_store().find_attribute_user(SPANS$6);
            if (sTCellSpansFind_attribute_user == null) {
                sTCellSpansFind_attribute_user = (STCellSpans) get_store().add_attribute_user(SPANS$6);
            }
            sTCellSpansFind_attribute_user.set(sTCellSpans);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void unsetSpans() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SPANS$6);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public long getS() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(S$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(S$8);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public XmlUnsignedInt xgetS() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(S$8);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(S$8);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean isSetS() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(S$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setS(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(S$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(S$8);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void xsetS(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(S$8);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(S$8);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void unsetS() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(S$8);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean getCustomFormat() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CUSTOMFORMAT$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(CUSTOMFORMAT$10);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public XmlBoolean xgetCustomFormat() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(CUSTOMFORMAT$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(CUSTOMFORMAT$10);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean isSetCustomFormat() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CUSTOMFORMAT$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setCustomFormat(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CUSTOMFORMAT$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CUSTOMFORMAT$10);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void xsetCustomFormat(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(CUSTOMFORMAT$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(CUSTOMFORMAT$10);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void unsetCustomFormat() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CUSTOMFORMAT$10);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public double getHt() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HT$12);
            if (simpleValue == null) {
                return 0.0d;
            }
            return simpleValue.getDoubleValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public XmlDouble xgetHt() {
        XmlDouble xmlDouble;
        synchronized (monitor()) {
            check_orphaned();
            xmlDouble = (XmlDouble) get_store().find_attribute_user(HT$12);
        }
        return xmlDouble;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean isSetHt() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HT$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setHt(double d) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HT$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HT$12);
            }
            simpleValue.setDoubleValue(d);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void xsetHt(XmlDouble xmlDouble) {
        synchronized (monitor()) {
            check_orphaned();
            XmlDouble xmlDouble2 = (XmlDouble) get_store().find_attribute_user(HT$12);
            if (xmlDouble2 == null) {
                xmlDouble2 = (XmlDouble) get_store().add_attribute_user(HT$12);
            }
            xmlDouble2.set(xmlDouble);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void unsetHt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HT$12);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean getHidden() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HIDDEN$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(HIDDEN$14);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public XmlBoolean xgetHidden() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HIDDEN$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(HIDDEN$14);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean isSetHidden() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HIDDEN$14) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setHidden(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HIDDEN$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HIDDEN$14);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void xsetHidden(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HIDDEN$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(HIDDEN$14);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void unsetHidden() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HIDDEN$14);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean getCustomHeight() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CUSTOMHEIGHT$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(CUSTOMHEIGHT$16);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public XmlBoolean xgetCustomHeight() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(CUSTOMHEIGHT$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(CUSTOMHEIGHT$16);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean isSetCustomHeight() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CUSTOMHEIGHT$16) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setCustomHeight(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CUSTOMHEIGHT$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CUSTOMHEIGHT$16);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void xsetCustomHeight(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(CUSTOMHEIGHT$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(CUSTOMHEIGHT$16);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void unsetCustomHeight() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CUSTOMHEIGHT$16);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public short getOutlineLevel() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OUTLINELEVEL$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(OUTLINELEVEL$18);
            }
            if (simpleValue == null) {
                return (short) 0;
            }
            return simpleValue.getShortValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public XmlUnsignedByte xgetOutlineLevel() {
        XmlUnsignedByte xmlUnsignedByte;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedByte xmlUnsignedByte2 = (XmlUnsignedByte) get_store().find_attribute_user(OUTLINELEVEL$18);
            if (xmlUnsignedByte2 == null) {
                xmlUnsignedByte2 = (XmlUnsignedByte) get_default_attribute_value(OUTLINELEVEL$18);
            }
            xmlUnsignedByte = xmlUnsignedByte2;
        }
        return xmlUnsignedByte;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean isSetOutlineLevel() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(OUTLINELEVEL$18) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setOutlineLevel(short s) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OUTLINELEVEL$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(OUTLINELEVEL$18);
            }
            simpleValue.setShortValue(s);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void xsetOutlineLevel(XmlUnsignedByte xmlUnsignedByte) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedByte xmlUnsignedByte2 = (XmlUnsignedByte) get_store().find_attribute_user(OUTLINELEVEL$18);
            if (xmlUnsignedByte2 == null) {
                xmlUnsignedByte2 = (XmlUnsignedByte) get_store().add_attribute_user(OUTLINELEVEL$18);
            }
            xmlUnsignedByte2.set(xmlUnsignedByte);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void unsetOutlineLevel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(OUTLINELEVEL$18);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean getCollapsed() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COLLAPSED$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(COLLAPSED$20);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public XmlBoolean xgetCollapsed() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COLLAPSED$20);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(COLLAPSED$20);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean isSetCollapsed() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COLLAPSED$20) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setCollapsed(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COLLAPSED$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COLLAPSED$20);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void xsetCollapsed(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COLLAPSED$20);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(COLLAPSED$20);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void unsetCollapsed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COLLAPSED$20);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean getThickTop() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(THICKTOP$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(THICKTOP$22);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public XmlBoolean xgetThickTop() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(THICKTOP$22);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(THICKTOP$22);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean isSetThickTop() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(THICKTOP$22) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setThickTop(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(THICKTOP$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(THICKTOP$22);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void xsetThickTop(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(THICKTOP$22);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(THICKTOP$22);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void unsetThickTop() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(THICKTOP$22);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean getThickBot() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(THICKBOT$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(THICKBOT$24);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public XmlBoolean xgetThickBot() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(THICKBOT$24);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(THICKBOT$24);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean isSetThickBot() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(THICKBOT$24) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setThickBot(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(THICKBOT$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(THICKBOT$24);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void xsetThickBot(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(THICKBOT$24);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(THICKBOT$24);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void unsetThickBot() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(THICKBOT$24);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean getPh() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PH$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PH$26);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public XmlBoolean xgetPh() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PH$26);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PH$26);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public boolean isSetPh() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PH$26) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void setPh(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PH$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PH$26);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void xsetPh(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PH$26);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PH$26);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow
    public void unsetPh() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PH$26);
        }
    }
}
