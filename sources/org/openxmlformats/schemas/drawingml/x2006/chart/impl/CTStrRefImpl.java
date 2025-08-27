package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import com.alibaba.excel.constant.ExcelXmlConstants;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTStrData;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTStrRefImpl.class */
public class CTStrRefImpl extends XmlComplexContentImpl implements CTStrRef {
    private static final QName F$0 = new QName(XSSFRelation.NS_CHART, ExcelXmlConstants.CELL_FORMULA_TAG);
    private static final QName STRCACHE$2 = new QName(XSSFRelation.NS_CHART, "strCache");
    private static final QName EXTLST$4 = new QName(XSSFRelation.NS_CHART, "extLst");

    public CTStrRefImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public String getF() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(F$0, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public XmlString xgetF() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_element_user(F$0, 0);
        }
        return xmlString;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public void setF(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(F$0, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(F$0);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public void xsetF(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_element_user(F$0, 0);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_element_user(F$0);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public CTStrData getStrCache() {
        synchronized (monitor()) {
            check_orphaned();
            CTStrData cTStrData = (CTStrData) get_store().find_element_user(STRCACHE$2, 0);
            if (cTStrData == null) {
                return null;
            }
            return cTStrData;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public boolean isSetStrCache() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(STRCACHE$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public void setStrCache(CTStrData cTStrData) {
        synchronized (monitor()) {
            check_orphaned();
            CTStrData cTStrData2 = (CTStrData) get_store().find_element_user(STRCACHE$2, 0);
            if (cTStrData2 == null) {
                cTStrData2 = (CTStrData) get_store().add_element_user(STRCACHE$2);
            }
            cTStrData2.set(cTStrData);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public CTStrData addNewStrCache() {
        CTStrData cTStrData;
        synchronized (monitor()) {
            check_orphaned();
            cTStrData = (CTStrData) get_store().add_element_user(STRCACHE$2);
        }
        return cTStrData;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public void unsetStrCache() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(STRCACHE$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$4, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$4, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$4);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$4);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$4, 0);
        }
    }
}
