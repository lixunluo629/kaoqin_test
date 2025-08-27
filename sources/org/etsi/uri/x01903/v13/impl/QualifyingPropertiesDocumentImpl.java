package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.poi.poifs.crypt.dsig.facets.SignatureFacet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.etsi.uri.x01903.v13.QualifyingPropertiesDocument;
import org.etsi.uri.x01903.v13.QualifyingPropertiesType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/impl/QualifyingPropertiesDocumentImpl.class */
public class QualifyingPropertiesDocumentImpl extends XmlComplexContentImpl implements QualifyingPropertiesDocument {
    private static final QName QUALIFYINGPROPERTIES$0 = new QName(SignatureFacet.XADES_132_NS, "QualifyingProperties");

    public QualifyingPropertiesDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.etsi.uri.x01903.v13.QualifyingPropertiesDocument
    public QualifyingPropertiesType getQualifyingProperties() {
        synchronized (monitor()) {
            check_orphaned();
            QualifyingPropertiesType qualifyingPropertiesType = (QualifyingPropertiesType) get_store().find_element_user(QUALIFYINGPROPERTIES$0, 0);
            if (qualifyingPropertiesType == null) {
                return null;
            }
            return qualifyingPropertiesType;
        }
    }

    @Override // org.etsi.uri.x01903.v13.QualifyingPropertiesDocument
    public void setQualifyingProperties(QualifyingPropertiesType qualifyingPropertiesType) {
        synchronized (monitor()) {
            check_orphaned();
            QualifyingPropertiesType qualifyingPropertiesType2 = (QualifyingPropertiesType) get_store().find_element_user(QUALIFYINGPROPERTIES$0, 0);
            if (qualifyingPropertiesType2 == null) {
                qualifyingPropertiesType2 = (QualifyingPropertiesType) get_store().add_element_user(QUALIFYINGPROPERTIES$0);
            }
            qualifyingPropertiesType2.set(qualifyingPropertiesType);
        }
    }

    @Override // org.etsi.uri.x01903.v13.QualifyingPropertiesDocument
    public QualifyingPropertiesType addNewQualifyingProperties() {
        QualifyingPropertiesType qualifyingPropertiesType;
        synchronized (monitor()) {
            check_orphaned();
            qualifyingPropertiesType = (QualifyingPropertiesType) get_store().add_element_user(QUALIFYINGPROPERTIES$0);
        }
        return qualifyingPropertiesType;
    }
}
