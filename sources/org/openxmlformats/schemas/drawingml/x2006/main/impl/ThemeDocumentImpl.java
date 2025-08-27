package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet;
import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/ThemeDocumentImpl.class */
public class ThemeDocumentImpl extends XmlComplexContentImpl implements ThemeDocument {
    private static final QName THEME$0 = new QName(XSSFRelation.NS_DRAWINGML, "theme");

    public ThemeDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument
    public CTOfficeStyleSheet getTheme() {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeStyleSheet cTOfficeStyleSheet = (CTOfficeStyleSheet) get_store().find_element_user(THEME$0, 0);
            if (cTOfficeStyleSheet == null) {
                return null;
            }
            return cTOfficeStyleSheet;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument
    public void setTheme(CTOfficeStyleSheet cTOfficeStyleSheet) {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeStyleSheet cTOfficeStyleSheet2 = (CTOfficeStyleSheet) get_store().find_element_user(THEME$0, 0);
            if (cTOfficeStyleSheet2 == null) {
                cTOfficeStyleSheet2 = (CTOfficeStyleSheet) get_store().add_element_user(THEME$0);
            }
            cTOfficeStyleSheet2.set(cTOfficeStyleSheet);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument
    public CTOfficeStyleSheet addNewTheme() {
        CTOfficeStyleSheet cTOfficeStyleSheet;
        synchronized (monitor()) {
            check_orphaned();
            cTOfficeStyleSheet = (CTOfficeStyleSheet) get_store().add_element_user(THEME$0);
        }
        return cTOfficeStyleSheet;
    }
}
