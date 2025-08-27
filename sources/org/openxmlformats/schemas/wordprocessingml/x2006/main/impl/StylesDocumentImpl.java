package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/StylesDocumentImpl.class */
public class StylesDocumentImpl extends XmlComplexContentImpl implements StylesDocument {
    private static final QName STYLES$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "styles");

    public StylesDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument
    public CTStyles getStyles() {
        synchronized (monitor()) {
            check_orphaned();
            CTStyles cTStyles = (CTStyles) get_store().find_element_user(STYLES$0, 0);
            if (cTStyles == null) {
                return null;
            }
            return cTStyles;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument
    public void setStyles(CTStyles cTStyles) {
        synchronized (monitor()) {
            check_orphaned();
            CTStyles cTStyles2 = (CTStyles) get_store().find_element_user(STYLES$0, 0);
            if (cTStyles2 == null) {
                cTStyles2 = (CTStyles) get_store().add_element_user(STYLES$0);
            }
            cTStyles2.set(cTStyles);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument
    public CTStyles addNewStyles() {
        CTStyles cTStyles;
        synchronized (monitor()) {
            check_orphaned();
            cTStyles = (CTStyles) get_store().add_element_user(STYLES$0);
        }
        return cTStyles;
    }
}
