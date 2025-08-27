package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.MasterContentsDocument;
import com.microsoft.schemas.office.visio.x2012.main.PageContentsType;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/MasterContentsDocumentImpl.class */
public class MasterContentsDocumentImpl extends XmlComplexContentImpl implements MasterContentsDocument {
    private static final QName MASTERCONTENTS$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "MasterContents");

    public MasterContentsDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterContentsDocument
    public PageContentsType getMasterContents() {
        synchronized (monitor()) {
            check_orphaned();
            PageContentsType pageContentsType = (PageContentsType) get_store().find_element_user(MASTERCONTENTS$0, 0);
            if (pageContentsType == null) {
                return null;
            }
            return pageContentsType;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterContentsDocument
    public void setMasterContents(PageContentsType pageContentsType) {
        synchronized (monitor()) {
            check_orphaned();
            PageContentsType pageContentsType2 = (PageContentsType) get_store().find_element_user(MASTERCONTENTS$0, 0);
            if (pageContentsType2 == null) {
                pageContentsType2 = (PageContentsType) get_store().add_element_user(MASTERCONTENTS$0);
            }
            pageContentsType2.set(pageContentsType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MasterContentsDocument
    public PageContentsType addNewMasterContents() {
        PageContentsType pageContentsType;
        synchronized (monitor()) {
            check_orphaned();
            pageContentsType = (PageContentsType) get_store().add_element_user(MASTERCONTENTS$0);
        }
        return pageContentsType;
    }
}
