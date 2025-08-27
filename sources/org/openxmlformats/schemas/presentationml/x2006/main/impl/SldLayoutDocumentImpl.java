package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout;
import org.openxmlformats.schemas.presentationml.x2006.main.SldLayoutDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/impl/SldLayoutDocumentImpl.class */
public class SldLayoutDocumentImpl extends XmlComplexContentImpl implements SldLayoutDocument {
    private static final QName SLDLAYOUT$0 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sldLayout");

    public SldLayoutDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.SldLayoutDocument
    public CTSlideLayout getSldLayout() {
        synchronized (monitor()) {
            check_orphaned();
            CTSlideLayout cTSlideLayout = (CTSlideLayout) get_store().find_element_user(SLDLAYOUT$0, 0);
            if (cTSlideLayout == null) {
                return null;
            }
            return cTSlideLayout;
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.SldLayoutDocument
    public void setSldLayout(CTSlideLayout cTSlideLayout) {
        synchronized (monitor()) {
            check_orphaned();
            CTSlideLayout cTSlideLayout2 = (CTSlideLayout) get_store().find_element_user(SLDLAYOUT$0, 0);
            if (cTSlideLayout2 == null) {
                cTSlideLayout2 = (CTSlideLayout) get_store().add_element_user(SLDLAYOUT$0);
            }
            cTSlideLayout2.set(cTSlideLayout);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.SldLayoutDocument
    public CTSlideLayout addNewSldLayout() {
        CTSlideLayout cTSlideLayout;
        synchronized (monitor()) {
            check_orphaned();
            cTSlideLayout = (CTSlideLayout) get_store().add_element_user(SLDLAYOUT$0);
        }
        return cTSlideLayout;
    }
}
