package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.VisioDocumentDocument1;
import com.microsoft.schemas.office.visio.x2012.main.VisioDocumentType;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/VisioDocumentDocument1Impl.class */
public class VisioDocumentDocument1Impl extends XmlComplexContentImpl implements VisioDocumentDocument1 {
    private static final QName VISIODOCUMENT$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "VisioDocument");

    public VisioDocumentDocument1Impl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.VisioDocumentDocument1
    public VisioDocumentType getVisioDocument() {
        synchronized (monitor()) {
            check_orphaned();
            VisioDocumentType visioDocumentType = (VisioDocumentType) get_store().find_element_user(VISIODOCUMENT$0, 0);
            if (visioDocumentType == null) {
                return null;
            }
            return visioDocumentType;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.VisioDocumentDocument1
    public void setVisioDocument(VisioDocumentType visioDocumentType) {
        synchronized (monitor()) {
            check_orphaned();
            VisioDocumentType visioDocumentType2 = (VisioDocumentType) get_store().find_element_user(VISIODOCUMENT$0, 0);
            if (visioDocumentType2 == null) {
                visioDocumentType2 = (VisioDocumentType) get_store().add_element_user(VISIODOCUMENT$0);
            }
            visioDocumentType2.set(visioDocumentType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.VisioDocumentDocument1
    public VisioDocumentType addNewVisioDocument() {
        VisioDocumentType visioDocumentType;
        synchronized (monitor()) {
            check_orphaned();
            visioDocumentType = (VisioDocumentType) get_store().add_element_user(VISIODOCUMENT$0);
        }
        return visioDocumentType;
    }
}
