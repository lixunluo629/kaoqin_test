package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.PagesDocument;
import com.microsoft.schemas.office.visio.x2012.main.PagesType;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/PagesDocumentImpl.class */
public class PagesDocumentImpl extends XmlComplexContentImpl implements PagesDocument {
    private static final QName PAGES$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Pages");

    public PagesDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PagesDocument
    public PagesType getPages() {
        synchronized (monitor()) {
            check_orphaned();
            PagesType pagesType = (PagesType) get_store().find_element_user(PAGES$0, 0);
            if (pagesType == null) {
                return null;
            }
            return pagesType;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PagesDocument
    public void setPages(PagesType pagesType) {
        synchronized (monitor()) {
            check_orphaned();
            PagesType pagesType2 = (PagesType) get_store().find_element_user(PAGES$0, 0);
            if (pagesType2 == null) {
                pagesType2 = (PagesType) get_store().add_element_user(PAGES$0);
            }
            pagesType2.set(pagesType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PagesDocument
    public PagesType addNewPages() {
        PagesType pagesType;
        synchronized (monitor()) {
            check_orphaned();
            pagesType = (PagesType) get_store().add_element_user(PAGES$0);
        }
        return pagesType;
    }
}
