package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.ExternalLinkDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/ExternalLinkDocumentImpl.class */
public class ExternalLinkDocumentImpl extends XmlComplexContentImpl implements ExternalLinkDocument {
    private static final QName EXTERNALLINK$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "externalLink");

    public ExternalLinkDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.ExternalLinkDocument
    public CTExternalLink getExternalLink() {
        synchronized (monitor()) {
            check_orphaned();
            CTExternalLink cTExternalLink = (CTExternalLink) get_store().find_element_user(EXTERNALLINK$0, 0);
            if (cTExternalLink == null) {
                return null;
            }
            return cTExternalLink;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.ExternalLinkDocument
    public void setExternalLink(CTExternalLink cTExternalLink) {
        synchronized (monitor()) {
            check_orphaned();
            CTExternalLink cTExternalLink2 = (CTExternalLink) get_store().find_element_user(EXTERNALLINK$0, 0);
            if (cTExternalLink2 == null) {
                cTExternalLink2 = (CTExternalLink) get_store().add_element_user(EXTERNALLINK$0);
            }
            cTExternalLink2.set(cTExternalLink);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.ExternalLinkDocument
    public CTExternalLink addNewExternalLink() {
        CTExternalLink cTExternalLink;
        synchronized (monitor()) {
            check_orphaned();
            cTExternalLink = (CTExternalLink) get_store().add_element_user(EXTERNALLINK$0);
        }
        return cTExternalLink;
    }
}
