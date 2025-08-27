package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentList;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTCommentsImpl.class */
public class CTCommentsImpl extends XmlComplexContentImpl implements CTComments {
    private static final QName AUTHORS$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "authors");
    private static final QName COMMENTLIST$2 = new QName(XSSFRelation.NS_SPREADSHEETML, "commentList");
    private static final QName EXTLST$4 = new QName(XSSFRelation.NS_SPREADSHEETML, "extLst");

    public CTCommentsImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments
    public CTAuthors getAuthors() {
        synchronized (monitor()) {
            check_orphaned();
            CTAuthors cTAuthors = (CTAuthors) get_store().find_element_user(AUTHORS$0, 0);
            if (cTAuthors == null) {
                return null;
            }
            return cTAuthors;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments
    public void setAuthors(CTAuthors cTAuthors) {
        synchronized (monitor()) {
            check_orphaned();
            CTAuthors cTAuthors2 = (CTAuthors) get_store().find_element_user(AUTHORS$0, 0);
            if (cTAuthors2 == null) {
                cTAuthors2 = (CTAuthors) get_store().add_element_user(AUTHORS$0);
            }
            cTAuthors2.set(cTAuthors);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments
    public CTAuthors addNewAuthors() {
        CTAuthors cTAuthors;
        synchronized (monitor()) {
            check_orphaned();
            cTAuthors = (CTAuthors) get_store().add_element_user(AUTHORS$0);
        }
        return cTAuthors;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments
    public CTCommentList getCommentList() {
        synchronized (monitor()) {
            check_orphaned();
            CTCommentList cTCommentList = (CTCommentList) get_store().find_element_user(COMMENTLIST$2, 0);
            if (cTCommentList == null) {
                return null;
            }
            return cTCommentList;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments
    public void setCommentList(CTCommentList cTCommentList) {
        synchronized (monitor()) {
            check_orphaned();
            CTCommentList cTCommentList2 = (CTCommentList) get_store().find_element_user(COMMENTLIST$2, 0);
            if (cTCommentList2 == null) {
                cTCommentList2 = (CTCommentList) get_store().add_element_user(COMMENTLIST$2);
            }
            cTCommentList2.set(cTCommentList);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments
    public CTCommentList addNewCommentList() {
        CTCommentList cTCommentList;
        synchronized (monitor()) {
            check_orphaned();
            cTCommentList = (CTCommentList) get_store().add_element_user(COMMENTLIST$2);
        }
        return cTCommentList;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$4, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$4, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$4);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$4);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$4, 0);
        }
    }
}
