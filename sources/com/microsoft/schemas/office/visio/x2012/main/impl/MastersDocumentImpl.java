package com.microsoft.schemas.office.visio.x2012.main.impl;

import com.microsoft.schemas.office.visio.x2012.main.MastersDocument;
import com.microsoft.schemas.office.visio.x2012.main.MastersType;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/impl/MastersDocumentImpl.class */
public class MastersDocumentImpl extends XmlComplexContentImpl implements MastersDocument {
    private static final QName MASTERS$0 = new QName("http://schemas.microsoft.com/office/visio/2012/main", "Masters");

    public MastersDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MastersDocument
    public MastersType getMasters() {
        synchronized (monitor()) {
            check_orphaned();
            MastersType mastersType = (MastersType) get_store().find_element_user(MASTERS$0, 0);
            if (mastersType == null) {
                return null;
            }
            return mastersType;
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MastersDocument
    public void setMasters(MastersType mastersType) {
        synchronized (monitor()) {
            check_orphaned();
            MastersType mastersType2 = (MastersType) get_store().find_element_user(MASTERS$0, 0);
            if (mastersType2 == null) {
                mastersType2 = (MastersType) get_store().add_element_user(MASTERS$0);
            }
            mastersType2.set(mastersType);
        }
    }

    @Override // com.microsoft.schemas.office.visio.x2012.main.MastersDocument
    public MastersType addNewMasters() {
        MastersType mastersType;
        synchronized (monitor()) {
            check_orphaned();
            mastersType = (MastersType) get_store().add_element_user(MASTERS$0);
        }
        return mastersType;
    }
}
