package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.poi.poifs.crypt.dsig.facets.SignatureFacet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.w3.x2000.x09.xmldsig.TransformDocument;
import org.w3.x2000.x09.xmldsig.TransformType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/impl/TransformDocumentImpl.class */
public class TransformDocumentImpl extends XmlComplexContentImpl implements TransformDocument {
    private static final QName TRANSFORM$0 = new QName(SignatureFacet.XML_DIGSIG_NS, "Transform");

    public TransformDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.w3.x2000.x09.xmldsig.TransformDocument
    public TransformType getTransform() {
        synchronized (monitor()) {
            check_orphaned();
            TransformType transformType = (TransformType) get_store().find_element_user(TRANSFORM$0, 0);
            if (transformType == null) {
                return null;
            }
            return transformType;
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.TransformDocument
    public void setTransform(TransformType transformType) {
        synchronized (monitor()) {
            check_orphaned();
            TransformType transformType2 = (TransformType) get_store().find_element_user(TRANSFORM$0, 0);
            if (transformType2 == null) {
                transformType2 = (TransformType) get_store().add_element_user(TRANSFORM$0);
            }
            transformType2.set(transformType);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.TransformDocument
    public TransformType addNewTransform() {
        TransformType transformType;
        synchronized (monitor()) {
            check_orphaned();
            transformType = (TransformType) get_store().add_element_user(TRANSFORM$0);
        }
        return transformType;
    }
}
