package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeLink;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalBook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleLink;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTExternalLinkImpl.class */
public class CTExternalLinkImpl extends XmlComplexContentImpl implements CTExternalLink {
    private static final QName EXTERNALBOOK$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "externalBook");
    private static final QName DDELINK$2 = new QName(XSSFRelation.NS_SPREADSHEETML, "ddeLink");
    private static final QName OLELINK$4 = new QName(XSSFRelation.NS_SPREADSHEETML, "oleLink");
    private static final QName EXTLST$6 = new QName(XSSFRelation.NS_SPREADSHEETML, "extLst");

    public CTExternalLinkImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public CTExternalBook getExternalBook() {
        synchronized (monitor()) {
            check_orphaned();
            CTExternalBook cTExternalBook = (CTExternalBook) get_store().find_element_user(EXTERNALBOOK$0, 0);
            if (cTExternalBook == null) {
                return null;
            }
            return cTExternalBook;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public boolean isSetExternalBook() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTERNALBOOK$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public void setExternalBook(CTExternalBook cTExternalBook) {
        synchronized (monitor()) {
            check_orphaned();
            CTExternalBook cTExternalBook2 = (CTExternalBook) get_store().find_element_user(EXTERNALBOOK$0, 0);
            if (cTExternalBook2 == null) {
                cTExternalBook2 = (CTExternalBook) get_store().add_element_user(EXTERNALBOOK$0);
            }
            cTExternalBook2.set(cTExternalBook);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public CTExternalBook addNewExternalBook() {
        CTExternalBook cTExternalBook;
        synchronized (monitor()) {
            check_orphaned();
            cTExternalBook = (CTExternalBook) get_store().add_element_user(EXTERNALBOOK$0);
        }
        return cTExternalBook;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public void unsetExternalBook() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTERNALBOOK$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public CTDdeLink getDdeLink() {
        synchronized (monitor()) {
            check_orphaned();
            CTDdeLink cTDdeLinkFind_element_user = get_store().find_element_user(DDELINK$2, 0);
            if (cTDdeLinkFind_element_user == null) {
                return null;
            }
            return cTDdeLinkFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public boolean isSetDdeLink() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(DDELINK$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public void setDdeLink(CTDdeLink cTDdeLink) {
        synchronized (monitor()) {
            check_orphaned();
            CTDdeLink cTDdeLinkFind_element_user = get_store().find_element_user(DDELINK$2, 0);
            if (cTDdeLinkFind_element_user == null) {
                cTDdeLinkFind_element_user = (CTDdeLink) get_store().add_element_user(DDELINK$2);
            }
            cTDdeLinkFind_element_user.set(cTDdeLink);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public CTDdeLink addNewDdeLink() {
        CTDdeLink cTDdeLinkAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTDdeLinkAdd_element_user = get_store().add_element_user(DDELINK$2);
        }
        return cTDdeLinkAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public void unsetDdeLink() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DDELINK$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public CTOleLink getOleLink() {
        synchronized (monitor()) {
            check_orphaned();
            CTOleLink cTOleLinkFind_element_user = get_store().find_element_user(OLELINK$4, 0);
            if (cTOleLinkFind_element_user == null) {
                return null;
            }
            return cTOleLinkFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public boolean isSetOleLink() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(OLELINK$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public void setOleLink(CTOleLink cTOleLink) {
        synchronized (monitor()) {
            check_orphaned();
            CTOleLink cTOleLinkFind_element_user = get_store().find_element_user(OLELINK$4, 0);
            if (cTOleLinkFind_element_user == null) {
                cTOleLinkFind_element_user = (CTOleLink) get_store().add_element_user(OLELINK$4);
            }
            cTOleLinkFind_element_user.set(cTOleLink);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public CTOleLink addNewOleLink() {
        CTOleLink cTOleLinkAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTOleLinkAdd_element_user = get_store().add_element_user(OLELINK$4);
        }
        return cTOleLinkAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public void unsetOleLink() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(OLELINK$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$6, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$6) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$6, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$6);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$6);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$6, 0);
        }
    }
}
