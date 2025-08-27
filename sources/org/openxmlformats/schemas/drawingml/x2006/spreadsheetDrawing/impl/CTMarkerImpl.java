package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.impl;

import com.alibaba.excel.constant.ExcelXmlConstants;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STColID;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STRowID;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/impl/CTMarkerImpl.class */
public class CTMarkerImpl extends XmlComplexContentImpl implements CTMarker {
    private static final QName COL$0 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "col");
    private static final QName COLOFF$2 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "colOff");
    private static final QName ROW$4 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", ExcelXmlConstants.ROW_TAG);
    private static final QName ROWOFF$6 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "rowOff");

    public CTMarkerImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public int getCol() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(COL$0, 0);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public STColID xgetCol() {
        STColID sTColID;
        synchronized (monitor()) {
            check_orphaned();
            sTColID = (STColID) get_store().find_element_user(COL$0, 0);
        }
        return sTColID;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public void setCol(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(COL$0, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(COL$0);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public void xsetCol(STColID sTColID) {
        synchronized (monitor()) {
            check_orphaned();
            STColID sTColID2 = (STColID) get_store().find_element_user(COL$0, 0);
            if (sTColID2 == null) {
                sTColID2 = (STColID) get_store().add_element_user(COL$0);
            }
            sTColID2.set(sTColID);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public long getColOff() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(COLOFF$2, 0);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public STCoordinate xgetColOff() {
        STCoordinate sTCoordinate;
        synchronized (monitor()) {
            check_orphaned();
            sTCoordinate = (STCoordinate) get_store().find_element_user(COLOFF$2, 0);
        }
        return sTCoordinate;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public void setColOff(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(COLOFF$2, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(COLOFF$2);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public void xsetColOff(STCoordinate sTCoordinate) {
        synchronized (monitor()) {
            check_orphaned();
            STCoordinate sTCoordinate2 = (STCoordinate) get_store().find_element_user(COLOFF$2, 0);
            if (sTCoordinate2 == null) {
                sTCoordinate2 = (STCoordinate) get_store().add_element_user(COLOFF$2);
            }
            sTCoordinate2.set(sTCoordinate);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public int getRow() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(ROW$4, 0);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public STRowID xgetRow() {
        STRowID sTRowID;
        synchronized (monitor()) {
            check_orphaned();
            sTRowID = (STRowID) get_store().find_element_user(ROW$4, 0);
        }
        return sTRowID;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public void setRow(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(ROW$4, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(ROW$4);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public void xsetRow(STRowID sTRowID) {
        synchronized (monitor()) {
            check_orphaned();
            STRowID sTRowID2 = (STRowID) get_store().find_element_user(ROW$4, 0);
            if (sTRowID2 == null) {
                sTRowID2 = (STRowID) get_store().add_element_user(ROW$4);
            }
            sTRowID2.set(sTRowID);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public long getRowOff() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(ROWOFF$6, 0);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public STCoordinate xgetRowOff() {
        STCoordinate sTCoordinate;
        synchronized (monitor()) {
            check_orphaned();
            sTCoordinate = (STCoordinate) get_store().find_element_user(ROWOFF$6, 0);
        }
        return sTCoordinate;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public void setRowOff(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(ROWOFF$6, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(ROWOFF$6);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
    public void xsetRowOff(STCoordinate sTCoordinate) {
        synchronized (monitor()) {
            check_orphaned();
            STCoordinate sTCoordinate2 = (STCoordinate) get_store().find_element_user(ROWOFF$6, 0);
            if (sTCoordinate2 == null) {
                sTCoordinate2 = (STCoordinate) get_store().add_element_user(ROWOFF$6);
            }
            sTCoordinate2.set(sTCoordinate);
        }
    }
}
