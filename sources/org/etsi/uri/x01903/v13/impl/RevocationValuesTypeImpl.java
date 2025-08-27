package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.poifs.crypt.dsig.facets.SignatureFacet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.etsi.uri.x01903.v13.CRLValuesType;
import org.etsi.uri.x01903.v13.OCSPValuesType;
import org.etsi.uri.x01903.v13.OtherCertStatusValuesType;
import org.etsi.uri.x01903.v13.RevocationValuesType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/impl/RevocationValuesTypeImpl.class */
public class RevocationValuesTypeImpl extends XmlComplexContentImpl implements RevocationValuesType {
    private static final QName CRLVALUES$0 = new QName(SignatureFacet.XADES_132_NS, "CRLValues");
    private static final QName OCSPVALUES$2 = new QName(SignatureFacet.XADES_132_NS, "OCSPValues");
    private static final QName OTHERVALUES$4 = new QName(SignatureFacet.XADES_132_NS, "OtherValues");
    private static final QName ID$6 = new QName("", PackageRelationship.ID_ATTRIBUTE_NAME);

    public RevocationValuesTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public CRLValuesType getCRLValues() {
        synchronized (monitor()) {
            check_orphaned();
            CRLValuesType cRLValuesType = (CRLValuesType) get_store().find_element_user(CRLVALUES$0, 0);
            if (cRLValuesType == null) {
                return null;
            }
            return cRLValuesType;
        }
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public boolean isSetCRLValues() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(CRLVALUES$0) != 0;
        }
        return z;
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public void setCRLValues(CRLValuesType cRLValuesType) {
        synchronized (monitor()) {
            check_orphaned();
            CRLValuesType cRLValuesType2 = (CRLValuesType) get_store().find_element_user(CRLVALUES$0, 0);
            if (cRLValuesType2 == null) {
                cRLValuesType2 = (CRLValuesType) get_store().add_element_user(CRLVALUES$0);
            }
            cRLValuesType2.set(cRLValuesType);
        }
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public CRLValuesType addNewCRLValues() {
        CRLValuesType cRLValuesType;
        synchronized (monitor()) {
            check_orphaned();
            cRLValuesType = (CRLValuesType) get_store().add_element_user(CRLVALUES$0);
        }
        return cRLValuesType;
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public void unsetCRLValues() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CRLVALUES$0, 0);
        }
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public OCSPValuesType getOCSPValues() {
        synchronized (monitor()) {
            check_orphaned();
            OCSPValuesType oCSPValuesType = (OCSPValuesType) get_store().find_element_user(OCSPVALUES$2, 0);
            if (oCSPValuesType == null) {
                return null;
            }
            return oCSPValuesType;
        }
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public boolean isSetOCSPValues() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(OCSPVALUES$2) != 0;
        }
        return z;
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public void setOCSPValues(OCSPValuesType oCSPValuesType) {
        synchronized (monitor()) {
            check_orphaned();
            OCSPValuesType oCSPValuesType2 = (OCSPValuesType) get_store().find_element_user(OCSPVALUES$2, 0);
            if (oCSPValuesType2 == null) {
                oCSPValuesType2 = (OCSPValuesType) get_store().add_element_user(OCSPVALUES$2);
            }
            oCSPValuesType2.set(oCSPValuesType);
        }
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public OCSPValuesType addNewOCSPValues() {
        OCSPValuesType oCSPValuesType;
        synchronized (monitor()) {
            check_orphaned();
            oCSPValuesType = (OCSPValuesType) get_store().add_element_user(OCSPVALUES$2);
        }
        return oCSPValuesType;
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public void unsetOCSPValues() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(OCSPVALUES$2, 0);
        }
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public OtherCertStatusValuesType getOtherValues() {
        synchronized (monitor()) {
            check_orphaned();
            OtherCertStatusValuesType otherCertStatusValuesTypeFind_element_user = get_store().find_element_user(OTHERVALUES$4, 0);
            if (otherCertStatusValuesTypeFind_element_user == null) {
                return null;
            }
            return otherCertStatusValuesTypeFind_element_user;
        }
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public boolean isSetOtherValues() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(OTHERVALUES$4) != 0;
        }
        return z;
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public void setOtherValues(OtherCertStatusValuesType otherCertStatusValuesType) {
        synchronized (monitor()) {
            check_orphaned();
            OtherCertStatusValuesType otherCertStatusValuesTypeFind_element_user = get_store().find_element_user(OTHERVALUES$4, 0);
            if (otherCertStatusValuesTypeFind_element_user == null) {
                otherCertStatusValuesTypeFind_element_user = (OtherCertStatusValuesType) get_store().add_element_user(OTHERVALUES$4);
            }
            otherCertStatusValuesTypeFind_element_user.set(otherCertStatusValuesType);
        }
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public OtherCertStatusValuesType addNewOtherValues() {
        OtherCertStatusValuesType otherCertStatusValuesTypeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            otherCertStatusValuesTypeAdd_element_user = get_store().add_element_user(OTHERVALUES$4);
        }
        return otherCertStatusValuesTypeAdd_element_user;
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public void unsetOtherValues() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(OTHERVALUES$4, 0);
        }
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public String getId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$6);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public XmlID xgetId() {
        XmlID xmlID;
        synchronized (monitor()) {
            check_orphaned();
            xmlID = (XmlID) get_store().find_attribute_user(ID$6);
        }
        return xmlID;
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public boolean isSetId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$6) != null;
        }
        return z;
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public void setId(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$6);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public void xsetId(XmlID xmlID) {
        synchronized (monitor()) {
            check_orphaned();
            XmlID xmlID2 = (XmlID) get_store().find_attribute_user(ID$6);
            if (xmlID2 == null) {
                xmlID2 = (XmlID) get_store().add_attribute_user(ID$6);
            }
            xmlID2.set(xmlID);
        }
    }

    @Override // org.etsi.uri.x01903.v13.RevocationValuesType
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$6);
        }
    }
}
