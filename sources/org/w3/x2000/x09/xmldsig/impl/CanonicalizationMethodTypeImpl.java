package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.w3.x2000.x09.xmldsig.CanonicalizationMethodType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/impl/CanonicalizationMethodTypeImpl.class */
public class CanonicalizationMethodTypeImpl extends XmlComplexContentImpl implements CanonicalizationMethodType {
    private static final QName ALGORITHM$0 = new QName("", "Algorithm");

    public CanonicalizationMethodTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.w3.x2000.x09.xmldsig.CanonicalizationMethodType
    public String getAlgorithm() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALGORITHM$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.CanonicalizationMethodType
    public XmlAnyURI xgetAlgorithm() {
        XmlAnyURI xmlAnyURI;
        synchronized (monitor()) {
            check_orphaned();
            xmlAnyURI = (XmlAnyURI) get_store().find_attribute_user(ALGORITHM$0);
        }
        return xmlAnyURI;
    }

    @Override // org.w3.x2000.x09.xmldsig.CanonicalizationMethodType
    public void setAlgorithm(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALGORITHM$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ALGORITHM$0);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.CanonicalizationMethodType
    public void xsetAlgorithm(XmlAnyURI xmlAnyURI) {
        synchronized (monitor()) {
            check_orphaned();
            XmlAnyURI xmlAnyURI2 = (XmlAnyURI) get_store().find_attribute_user(ALGORITHM$0);
            if (xmlAnyURI2 == null) {
                xmlAnyURI2 = (XmlAnyURI) get_store().add_attribute_user(ALGORITHM$0);
            }
            xmlAnyURI2.set(xmlAnyURI);
        }
    }
}
