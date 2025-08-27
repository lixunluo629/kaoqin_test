package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CommentsDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CommentsDocumentImpl.class */
public class CommentsDocumentImpl extends XmlComplexContentImpl implements CommentsDocument {
    private static final QName COMMENTS$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "comments");

    public CommentsDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CommentsDocument
    public CTComments getComments() {
        synchronized (monitor()) {
            check_orphaned();
            CTComments cTComments = (CTComments) get_store().find_element_user(COMMENTS$0, 0);
            if (cTComments == null) {
                return null;
            }
            return cTComments;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CommentsDocument
    public void setComments(CTComments cTComments) {
        synchronized (monitor()) {
            check_orphaned();
            CTComments cTComments2 = (CTComments) get_store().find_element_user(COMMENTS$0, 0);
            if (cTComments2 == null) {
                cTComments2 = (CTComments) get_store().add_element_user(COMMENTS$0);
            }
            cTComments2.set(cTComments);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CommentsDocument
    public CTComments addNewComments() {
        CTComments cTComments;
        synchronized (monitor()) {
            check_orphaned();
            cTComments = (CTComments) get_store().add_element_user(COMMENTS$0);
        }
        return cTComments;
    }
}
