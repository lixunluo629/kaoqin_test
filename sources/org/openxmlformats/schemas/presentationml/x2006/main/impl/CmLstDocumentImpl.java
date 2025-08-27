package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList;
import org.openxmlformats.schemas.presentationml.x2006.main.CmLstDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/impl/CmLstDocumentImpl.class */
public class CmLstDocumentImpl extends XmlComplexContentImpl implements CmLstDocument {
    private static final QName CMLST$0 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cmLst");

    public CmLstDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CmLstDocument
    public CTCommentList getCmLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTCommentList cTCommentList = (CTCommentList) get_store().find_element_user(CMLST$0, 0);
            if (cTCommentList == null) {
                return null;
            }
            return cTCommentList;
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CmLstDocument
    public void setCmLst(CTCommentList cTCommentList) {
        synchronized (monitor()) {
            check_orphaned();
            CTCommentList cTCommentList2 = (CTCommentList) get_store().find_element_user(CMLST$0, 0);
            if (cTCommentList2 == null) {
                cTCommentList2 = (CTCommentList) get_store().add_element_user(CMLST$0);
            }
            cTCommentList2.set(cTCommentList);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.CmLstDocument
    public CTCommentList addNewCmLst() {
        CTCommentList cTCommentList;
        synchronized (monitor()) {
            check_orphaned();
            cTCommentList = (CTCommentList) get_store().add_element_user(CMLST$0);
        }
        return cTCommentList;
    }
}
