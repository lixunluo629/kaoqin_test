package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import com.alibaba.excel.constant.ExcelXmlConstants;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef;
import org.openxmlformats.schemas.drawingml.x2006.chart.STXstring;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTSerTxImpl.class */
public class CTSerTxImpl extends XmlComplexContentImpl implements CTSerTx {
    private static final QName STRREF$0 = new QName(XSSFRelation.NS_CHART, "strRef");
    private static final QName V$2 = new QName(XSSFRelation.NS_CHART, ExcelXmlConstants.CELL_VALUE_TAG);

    public CTSerTxImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx
    public CTStrRef getStrRef() {
        synchronized (monitor()) {
            check_orphaned();
            CTStrRef cTStrRef = (CTStrRef) get_store().find_element_user(STRREF$0, 0);
            if (cTStrRef == null) {
                return null;
            }
            return cTStrRef;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx
    public boolean isSetStrRef() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(STRREF$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx
    public void setStrRef(CTStrRef cTStrRef) {
        synchronized (monitor()) {
            check_orphaned();
            CTStrRef cTStrRef2 = (CTStrRef) get_store().find_element_user(STRREF$0, 0);
            if (cTStrRef2 == null) {
                cTStrRef2 = (CTStrRef) get_store().add_element_user(STRREF$0);
            }
            cTStrRef2.set(cTStrRef);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx
    public CTStrRef addNewStrRef() {
        CTStrRef cTStrRef;
        synchronized (monitor()) {
            check_orphaned();
            cTStrRef = (CTStrRef) get_store().add_element_user(STRREF$0);
        }
        return cTStrRef;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx
    public void unsetStrRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(STRREF$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx
    public String getV() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(V$2, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx
    public STXstring xgetV() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_element_user(V$2, 0);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx
    public boolean isSetV() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(V$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx
    public void setV(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(V$2, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(V$2);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx
    public void xsetV(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_element_user(V$2, 0);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_element_user(V$2);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx
    public void unsetV() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(V$2, 0);
        }
    }
}
