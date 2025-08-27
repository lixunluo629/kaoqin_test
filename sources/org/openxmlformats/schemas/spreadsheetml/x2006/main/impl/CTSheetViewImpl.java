package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType$Enum;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTSheetViewImpl.class */
public class CTSheetViewImpl extends XmlComplexContentImpl implements CTSheetView {
    private static final QName PANE$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "pane");
    private static final QName SELECTION$2 = new QName(XSSFRelation.NS_SPREADSHEETML, "selection");
    private static final QName PIVOTSELECTION$4 = new QName(XSSFRelation.NS_SPREADSHEETML, "pivotSelection");
    private static final QName EXTLST$6 = new QName(XSSFRelation.NS_SPREADSHEETML, "extLst");
    private static final QName WINDOWPROTECTION$8 = new QName("", "windowProtection");
    private static final QName SHOWFORMULAS$10 = new QName("", "showFormulas");
    private static final QName SHOWGRIDLINES$12 = new QName("", "showGridLines");
    private static final QName SHOWROWCOLHEADERS$14 = new QName("", "showRowColHeaders");
    private static final QName SHOWZEROS$16 = new QName("", "showZeros");
    private static final QName RIGHTTOLEFT$18 = new QName("", "rightToLeft");
    private static final QName TABSELECTED$20 = new QName("", "tabSelected");
    private static final QName SHOWRULER$22 = new QName("", "showRuler");
    private static final QName SHOWOUTLINESYMBOLS$24 = new QName("", "showOutlineSymbols");
    private static final QName DEFAULTGRIDCOLOR$26 = new QName("", "defaultGridColor");
    private static final QName SHOWWHITESPACE$28 = new QName("", "showWhiteSpace");
    private static final QName VIEW$30 = new QName("", "view");
    private static final QName TOPLEFTCELL$32 = new QName("", "topLeftCell");
    private static final QName COLORID$34 = new QName("", "colorId");
    private static final QName ZOOMSCALE$36 = new QName("", "zoomScale");
    private static final QName ZOOMSCALENORMAL$38 = new QName("", "zoomScaleNormal");
    private static final QName ZOOMSCALESHEETLAYOUTVIEW$40 = new QName("", "zoomScaleSheetLayoutView");
    private static final QName ZOOMSCALEPAGELAYOUTVIEW$42 = new QName("", "zoomScalePageLayoutView");
    private static final QName WORKBOOKVIEWID$44 = new QName("", "workbookViewId");

    public CTSheetViewImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public CTPane getPane() {
        synchronized (monitor()) {
            check_orphaned();
            CTPane cTPane = (CTPane) get_store().find_element_user(PANE$0, 0);
            if (cTPane == null) {
                return null;
            }
            return cTPane;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetPane() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PANE$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setPane(CTPane cTPane) {
        synchronized (monitor()) {
            check_orphaned();
            CTPane cTPane2 = (CTPane) get_store().find_element_user(PANE$0, 0);
            if (cTPane2 == null) {
                cTPane2 = (CTPane) get_store().add_element_user(PANE$0);
            }
            cTPane2.set(cTPane);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public CTPane addNewPane() {
        CTPane cTPane;
        synchronized (monitor()) {
            check_orphaned();
            cTPane = (CTPane) get_store().add_element_user(PANE$0);
        }
        return cTPane;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetPane() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PANE$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public List<CTSelection> getSelectionList() {
        1SelectionList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1SelectionList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public CTSelection[] getSelectionArray() {
        CTSelection[] cTSelectionArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(SELECTION$2, arrayList);
            cTSelectionArr = new CTSelection[arrayList.size()];
            arrayList.toArray(cTSelectionArr);
        }
        return cTSelectionArr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public CTSelection getSelectionArray(int i) {
        CTSelection cTSelection;
        synchronized (monitor()) {
            check_orphaned();
            cTSelection = (CTSelection) get_store().find_element_user(SELECTION$2, i);
            if (cTSelection == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTSelection;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public int sizeOfSelectionArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(SELECTION$2);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setSelectionArray(CTSelection[] cTSelectionArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTSelectionArr, SELECTION$2);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setSelectionArray(int i, CTSelection cTSelection) {
        synchronized (monitor()) {
            check_orphaned();
            CTSelection cTSelection2 = (CTSelection) get_store().find_element_user(SELECTION$2, i);
            if (cTSelection2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTSelection2.set(cTSelection);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public CTSelection insertNewSelection(int i) {
        CTSelection cTSelection;
        synchronized (monitor()) {
            check_orphaned();
            cTSelection = (CTSelection) get_store().insert_element_user(SELECTION$2, i);
        }
        return cTSelection;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public CTSelection addNewSelection() {
        CTSelection cTSelection;
        synchronized (monitor()) {
            check_orphaned();
            cTSelection = (CTSelection) get_store().add_element_user(SELECTION$2);
        }
        return cTSelection;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void removeSelection(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SELECTION$2, i);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public List<CTPivotSelection> getPivotSelectionList() {
        1PivotSelectionList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1PivotSelectionList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public CTPivotSelection[] getPivotSelectionArray() {
        CTPivotSelection[] cTPivotSelectionArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(PIVOTSELECTION$4, arrayList);
            cTPivotSelectionArr = new CTPivotSelection[arrayList.size()];
            arrayList.toArray(cTPivotSelectionArr);
        }
        return cTPivotSelectionArr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public CTPivotSelection getPivotSelectionArray(int i) {
        CTPivotSelection cTPivotSelectionFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTPivotSelectionFind_element_user = get_store().find_element_user(PIVOTSELECTION$4, i);
            if (cTPivotSelectionFind_element_user == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTPivotSelectionFind_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public int sizeOfPivotSelectionArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(PIVOTSELECTION$4);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setPivotSelectionArray(CTPivotSelection[] cTPivotSelectionArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper((XmlObject[]) cTPivotSelectionArr, PIVOTSELECTION$4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setPivotSelectionArray(int i, CTPivotSelection cTPivotSelection) {
        synchronized (monitor()) {
            check_orphaned();
            CTPivotSelection cTPivotSelectionFind_element_user = get_store().find_element_user(PIVOTSELECTION$4, i);
            if (cTPivotSelectionFind_element_user == null) {
                throw new IndexOutOfBoundsException();
            }
            cTPivotSelectionFind_element_user.set(cTPivotSelection);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public CTPivotSelection insertNewPivotSelection(int i) {
        CTPivotSelection cTPivotSelectionInsert_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTPivotSelectionInsert_element_user = get_store().insert_element_user(PIVOTSELECTION$4, i);
        }
        return cTPivotSelectionInsert_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public CTPivotSelection addNewPivotSelection() {
        CTPivotSelection cTPivotSelectionAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTPivotSelectionAdd_element_user = get_store().add_element_user(PIVOTSELECTION$4);
        }
        return cTPivotSelectionAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void removePivotSelection(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PIVOTSELECTION$4, i);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$6, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$6) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$6, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$6);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$6);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$6, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean getWindowProtection() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(WINDOWPROTECTION$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(WINDOWPROTECTION$8);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlBoolean xgetWindowProtection() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(WINDOWPROTECTION$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(WINDOWPROTECTION$8);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetWindowProtection() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(WINDOWPROTECTION$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setWindowProtection(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(WINDOWPROTECTION$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(WINDOWPROTECTION$8);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetWindowProtection(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(WINDOWPROTECTION$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(WINDOWPROTECTION$8);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetWindowProtection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(WINDOWPROTECTION$8);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean getShowFormulas() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWFORMULAS$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWFORMULAS$10);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlBoolean xgetShowFormulas() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWFORMULAS$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWFORMULAS$10);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetShowFormulas() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWFORMULAS$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setShowFormulas(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWFORMULAS$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWFORMULAS$10);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetShowFormulas(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWFORMULAS$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWFORMULAS$10);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetShowFormulas() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWFORMULAS$10);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean getShowGridLines() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWGRIDLINES$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWGRIDLINES$12);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlBoolean xgetShowGridLines() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWGRIDLINES$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWGRIDLINES$12);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetShowGridLines() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWGRIDLINES$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setShowGridLines(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWGRIDLINES$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWGRIDLINES$12);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetShowGridLines(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWGRIDLINES$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWGRIDLINES$12);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetShowGridLines() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWGRIDLINES$12);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean getShowRowColHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWROWCOLHEADERS$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWROWCOLHEADERS$14);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlBoolean xgetShowRowColHeaders() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWROWCOLHEADERS$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWROWCOLHEADERS$14);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetShowRowColHeaders() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWROWCOLHEADERS$14) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setShowRowColHeaders(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWROWCOLHEADERS$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWROWCOLHEADERS$14);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetShowRowColHeaders(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWROWCOLHEADERS$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWROWCOLHEADERS$14);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetShowRowColHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWROWCOLHEADERS$14);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean getShowZeros() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWZEROS$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWZEROS$16);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlBoolean xgetShowZeros() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWZEROS$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWZEROS$16);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetShowZeros() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWZEROS$16) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setShowZeros(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWZEROS$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWZEROS$16);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetShowZeros(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWZEROS$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWZEROS$16);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetShowZeros() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWZEROS$16);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean getRightToLeft() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RIGHTTOLEFT$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(RIGHTTOLEFT$18);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlBoolean xgetRightToLeft() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(RIGHTTOLEFT$18);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(RIGHTTOLEFT$18);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetRightToLeft() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(RIGHTTOLEFT$18) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setRightToLeft(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RIGHTTOLEFT$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(RIGHTTOLEFT$18);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetRightToLeft(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(RIGHTTOLEFT$18);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(RIGHTTOLEFT$18);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetRightToLeft() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(RIGHTTOLEFT$18);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean getTabSelected() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TABSELECTED$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(TABSELECTED$20);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlBoolean xgetTabSelected() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TABSELECTED$20);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(TABSELECTED$20);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetTabSelected() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TABSELECTED$20) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setTabSelected(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TABSELECTED$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TABSELECTED$20);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetTabSelected(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TABSELECTED$20);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(TABSELECTED$20);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetTabSelected() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TABSELECTED$20);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean getShowRuler() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWRULER$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWRULER$22);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlBoolean xgetShowRuler() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWRULER$22);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWRULER$22);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetShowRuler() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWRULER$22) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setShowRuler(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWRULER$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWRULER$22);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetShowRuler(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWRULER$22);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWRULER$22);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetShowRuler() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWRULER$22);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean getShowOutlineSymbols() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWOUTLINESYMBOLS$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWOUTLINESYMBOLS$24);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlBoolean xgetShowOutlineSymbols() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWOUTLINESYMBOLS$24);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWOUTLINESYMBOLS$24);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetShowOutlineSymbols() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWOUTLINESYMBOLS$24) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setShowOutlineSymbols(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWOUTLINESYMBOLS$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWOUTLINESYMBOLS$24);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetShowOutlineSymbols(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWOUTLINESYMBOLS$24);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWOUTLINESYMBOLS$24);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetShowOutlineSymbols() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWOUTLINESYMBOLS$24);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean getDefaultGridColor() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEFAULTGRIDCOLOR$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DEFAULTGRIDCOLOR$26);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlBoolean xgetDefaultGridColor() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DEFAULTGRIDCOLOR$26);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DEFAULTGRIDCOLOR$26);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetDefaultGridColor() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DEFAULTGRIDCOLOR$26) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setDefaultGridColor(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEFAULTGRIDCOLOR$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DEFAULTGRIDCOLOR$26);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetDefaultGridColor(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DEFAULTGRIDCOLOR$26);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DEFAULTGRIDCOLOR$26);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetDefaultGridColor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DEFAULTGRIDCOLOR$26);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean getShowWhiteSpace() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWWHITESPACE$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWWHITESPACE$28);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlBoolean xgetShowWhiteSpace() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWWHITESPACE$28);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWWHITESPACE$28);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetShowWhiteSpace() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWWHITESPACE$28) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setShowWhiteSpace(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWWHITESPACE$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWWHITESPACE$28);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetShowWhiteSpace(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWWHITESPACE$28);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWWHITESPACE$28);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetShowWhiteSpace() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWWHITESPACE$28);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public STSheetViewType$Enum getView() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VIEW$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VIEW$30);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STSheetViewType$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public STSheetViewType xgetView() {
        STSheetViewType sTSheetViewType;
        synchronized (monitor()) {
            check_orphaned();
            STSheetViewType sTSheetViewTypeFind_attribute_user = get_store().find_attribute_user(VIEW$30);
            if (sTSheetViewTypeFind_attribute_user == null) {
                sTSheetViewTypeFind_attribute_user = (STSheetViewType) get_default_attribute_value(VIEW$30);
            }
            sTSheetViewType = sTSheetViewTypeFind_attribute_user;
        }
        return sTSheetViewType;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetView() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VIEW$30) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setView(STSheetViewType$Enum sTSheetViewType$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VIEW$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VIEW$30);
            }
            simpleValue.setEnumValue(sTSheetViewType$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetView(STSheetViewType sTSheetViewType) {
        synchronized (monitor()) {
            check_orphaned();
            STSheetViewType sTSheetViewTypeFind_attribute_user = get_store().find_attribute_user(VIEW$30);
            if (sTSheetViewTypeFind_attribute_user == null) {
                sTSheetViewTypeFind_attribute_user = (STSheetViewType) get_store().add_attribute_user(VIEW$30);
            }
            sTSheetViewTypeFind_attribute_user.set(sTSheetViewType);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetView() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VIEW$30);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public String getTopLeftCell() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOPLEFTCELL$32);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public STCellRef xgetTopLeftCell() {
        STCellRef sTCellRef;
        synchronized (monitor()) {
            check_orphaned();
            sTCellRef = (STCellRef) get_store().find_attribute_user(TOPLEFTCELL$32);
        }
        return sTCellRef;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetTopLeftCell() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TOPLEFTCELL$32) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setTopLeftCell(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOPLEFTCELL$32);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TOPLEFTCELL$32);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetTopLeftCell(STCellRef sTCellRef) {
        synchronized (monitor()) {
            check_orphaned();
            STCellRef sTCellRef2 = (STCellRef) get_store().find_attribute_user(TOPLEFTCELL$32);
            if (sTCellRef2 == null) {
                sTCellRef2 = (STCellRef) get_store().add_attribute_user(TOPLEFTCELL$32);
            }
            sTCellRef2.set(sTCellRef);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetTopLeftCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TOPLEFTCELL$32);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public long getColorId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COLORID$34);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(COLORID$34);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlUnsignedInt xgetColorId() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(COLORID$34);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(COLORID$34);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetColorId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COLORID$34) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setColorId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COLORID$34);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COLORID$34);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetColorId(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(COLORID$34);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(COLORID$34);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetColorId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COLORID$34);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public long getZoomScale() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ZOOMSCALE$36);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ZOOMSCALE$36);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlUnsignedInt xgetZoomScale() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ZOOMSCALE$36);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(ZOOMSCALE$36);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetZoomScale() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ZOOMSCALE$36) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setZoomScale(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ZOOMSCALE$36);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ZOOMSCALE$36);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetZoomScale(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ZOOMSCALE$36);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(ZOOMSCALE$36);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetZoomScale() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ZOOMSCALE$36);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public long getZoomScaleNormal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ZOOMSCALENORMAL$38);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ZOOMSCALENORMAL$38);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlUnsignedInt xgetZoomScaleNormal() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ZOOMSCALENORMAL$38);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(ZOOMSCALENORMAL$38);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetZoomScaleNormal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ZOOMSCALENORMAL$38) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setZoomScaleNormal(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ZOOMSCALENORMAL$38);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ZOOMSCALENORMAL$38);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetZoomScaleNormal(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ZOOMSCALENORMAL$38);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(ZOOMSCALENORMAL$38);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetZoomScaleNormal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ZOOMSCALENORMAL$38);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public long getZoomScaleSheetLayoutView() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ZOOMSCALESHEETLAYOUTVIEW$40);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ZOOMSCALESHEETLAYOUTVIEW$40);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlUnsignedInt xgetZoomScaleSheetLayoutView() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ZOOMSCALESHEETLAYOUTVIEW$40);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(ZOOMSCALESHEETLAYOUTVIEW$40);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetZoomScaleSheetLayoutView() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ZOOMSCALESHEETLAYOUTVIEW$40) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setZoomScaleSheetLayoutView(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ZOOMSCALESHEETLAYOUTVIEW$40);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ZOOMSCALESHEETLAYOUTVIEW$40);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetZoomScaleSheetLayoutView(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ZOOMSCALESHEETLAYOUTVIEW$40);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(ZOOMSCALESHEETLAYOUTVIEW$40);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetZoomScaleSheetLayoutView() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ZOOMSCALESHEETLAYOUTVIEW$40);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public long getZoomScalePageLayoutView() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ZOOMSCALEPAGELAYOUTVIEW$42);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ZOOMSCALEPAGELAYOUTVIEW$42);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlUnsignedInt xgetZoomScalePageLayoutView() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ZOOMSCALEPAGELAYOUTVIEW$42);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(ZOOMSCALEPAGELAYOUTVIEW$42);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public boolean isSetZoomScalePageLayoutView() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ZOOMSCALEPAGELAYOUTVIEW$42) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setZoomScalePageLayoutView(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ZOOMSCALEPAGELAYOUTVIEW$42);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ZOOMSCALEPAGELAYOUTVIEW$42);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetZoomScalePageLayoutView(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ZOOMSCALEPAGELAYOUTVIEW$42);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(ZOOMSCALEPAGELAYOUTVIEW$42);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void unsetZoomScalePageLayoutView() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ZOOMSCALEPAGELAYOUTVIEW$42);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public long getWorkbookViewId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(WORKBOOKVIEWID$44);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public XmlUnsignedInt xgetWorkbookViewId() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(WORKBOOKVIEWID$44);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void setWorkbookViewId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(WORKBOOKVIEWID$44);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(WORKBOOKVIEWID$44);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
    public void xsetWorkbookViewId(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(WORKBOOKVIEWID$44);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(WORKBOOKVIEWID$44);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }
}
