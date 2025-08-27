package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.PageContentsDocument;
import com.microsoft.schemas.office.visio.x2012.main.PageContentsType;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/PageContentsDocumentImpl.class */
public class PageContentsDocumentImpl extends XmlComplexContentImpl implements PageContentsDocument {
    private static final QName PAGECONTENTS$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "PageContents");

    public PageContentsDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageContentsDocument
    public PageContentsType getPageContents() {
        synchronized (monitor()) {
            check_orphaned();
            PageContentsType pageContentsType = (PageContentsType) get_store().find_element_user(PAGECONTENTS$0, 0);
            if (pageContentsType == null) {
                return null;
            }
            return pageContentsType;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageContentsDocument
    public void setPageContents(PageContentsType pageContentsType) {
        synchronized (monitor()) {
            check_orphaned();
            PageContentsType pageContentsType2 = (PageContentsType) get_store().find_element_user(PAGECONTENTS$0, 0);
            if (pageContentsType2 == null) {
                pageContentsType2 = (PageContentsType) get_store().add_element_user(PAGECONTENTS$0);
            }
            pageContentsType2.set(pageContentsType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.PageContentsDocument
    public PageContentsType addNewPageContents() {
        PageContentsType pageContentsType;
        synchronized (monitor()) {
            check_orphaned();
            pageContentsType = (PageContentsType) get_store().add_element_user(PAGECONTENTS$0);
        }
        return pageContentsType;
    }
}
