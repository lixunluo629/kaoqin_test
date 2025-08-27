package org.etsi.uri.x01903.v13.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.poifs.crypt.dsig.facets.SignatureFacet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.etsi.uri.x01903.v13.AnyType;
import org.etsi.uri.x01903.v13.CertificateValuesType;
import org.etsi.uri.x01903.v13.EncapsulatedPKIDataType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/impl/CertificateValuesTypeImpl.class */
public class CertificateValuesTypeImpl extends XmlComplexContentImpl implements CertificateValuesType {
    private static final QName ENCAPSULATEDX509CERTIFICATE$0 = new QName(SignatureFacet.XADES_132_NS, "EncapsulatedX509Certificate");
    private static final QName OTHERCERTIFICATE$2 = new QName(SignatureFacet.XADES_132_NS, "OtherCertificate");
    private static final QName ID$4 = new QName("", PackageRelationship.ID_ATTRIBUTE_NAME);

    public CertificateValuesTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public List<EncapsulatedPKIDataType> getEncapsulatedX509CertificateList() {
        1EncapsulatedX509CertificateList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1EncapsulatedX509CertificateList(this);
        }
        return r0;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public EncapsulatedPKIDataType[] getEncapsulatedX509CertificateArray() {
        EncapsulatedPKIDataType[] encapsulatedPKIDataTypeArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(ENCAPSULATEDX509CERTIFICATE$0, arrayList);
            encapsulatedPKIDataTypeArr = new EncapsulatedPKIDataType[arrayList.size()];
            arrayList.toArray(encapsulatedPKIDataTypeArr);
        }
        return encapsulatedPKIDataTypeArr;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public EncapsulatedPKIDataType getEncapsulatedX509CertificateArray(int i) {
        EncapsulatedPKIDataType encapsulatedPKIDataType;
        synchronized (monitor()) {
            check_orphaned();
            encapsulatedPKIDataType = (EncapsulatedPKIDataType) get_store().find_element_user(ENCAPSULATEDX509CERTIFICATE$0, i);
            if (encapsulatedPKIDataType == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return encapsulatedPKIDataType;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public int sizeOfEncapsulatedX509CertificateArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(ENCAPSULATEDX509CERTIFICATE$0);
        }
        return iCount_elements;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public void setEncapsulatedX509CertificateArray(EncapsulatedPKIDataType[] encapsulatedPKIDataTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(encapsulatedPKIDataTypeArr, ENCAPSULATEDX509CERTIFICATE$0);
        }
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public void setEncapsulatedX509CertificateArray(int i, EncapsulatedPKIDataType encapsulatedPKIDataType) {
        synchronized (monitor()) {
            check_orphaned();
            EncapsulatedPKIDataType encapsulatedPKIDataType2 = (EncapsulatedPKIDataType) get_store().find_element_user(ENCAPSULATEDX509CERTIFICATE$0, i);
            if (encapsulatedPKIDataType2 == null) {
                throw new IndexOutOfBoundsException();
            }
            encapsulatedPKIDataType2.set(encapsulatedPKIDataType);
        }
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public EncapsulatedPKIDataType insertNewEncapsulatedX509Certificate(int i) {
        EncapsulatedPKIDataType encapsulatedPKIDataType;
        synchronized (monitor()) {
            check_orphaned();
            encapsulatedPKIDataType = (EncapsulatedPKIDataType) get_store().insert_element_user(ENCAPSULATEDX509CERTIFICATE$0, i);
        }
        return encapsulatedPKIDataType;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public EncapsulatedPKIDataType addNewEncapsulatedX509Certificate() {
        EncapsulatedPKIDataType encapsulatedPKIDataType;
        synchronized (monitor()) {
            check_orphaned();
            encapsulatedPKIDataType = (EncapsulatedPKIDataType) get_store().add_element_user(ENCAPSULATEDX509CERTIFICATE$0);
        }
        return encapsulatedPKIDataType;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public void removeEncapsulatedX509Certificate(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ENCAPSULATEDX509CERTIFICATE$0, i);
        }
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public List<AnyType> getOtherCertificateList() {
        1OtherCertificateList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1OtherCertificateList(this);
        }
        return r0;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public AnyType[] getOtherCertificateArray() {
        AnyType[] anyTypeArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(OTHERCERTIFICATE$2, arrayList);
            anyTypeArr = new AnyType[arrayList.size()];
            arrayList.toArray(anyTypeArr);
        }
        return anyTypeArr;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public AnyType getOtherCertificateArray(int i) {
        AnyType anyTypeFind_element_user;
        synchronized (monitor()) {
            check_orphaned();
            anyTypeFind_element_user = get_store().find_element_user(OTHERCERTIFICATE$2, i);
            if (anyTypeFind_element_user == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return anyTypeFind_element_user;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public int sizeOfOtherCertificateArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(OTHERCERTIFICATE$2);
        }
        return iCount_elements;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public void setOtherCertificateArray(AnyType[] anyTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper((XmlObject[]) anyTypeArr, OTHERCERTIFICATE$2);
        }
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public void setOtherCertificateArray(int i, AnyType anyType) {
        synchronized (monitor()) {
            check_orphaned();
            AnyType anyTypeFind_element_user = get_store().find_element_user(OTHERCERTIFICATE$2, i);
            if (anyTypeFind_element_user == null) {
                throw new IndexOutOfBoundsException();
            }
            anyTypeFind_element_user.set(anyType);
        }
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public AnyType insertNewOtherCertificate(int i) {
        AnyType anyTypeInsert_element_user;
        synchronized (monitor()) {
            check_orphaned();
            anyTypeInsert_element_user = get_store().insert_element_user(OTHERCERTIFICATE$2, i);
        }
        return anyTypeInsert_element_user;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public AnyType addNewOtherCertificate() {
        AnyType anyTypeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            anyTypeAdd_element_user = get_store().add_element_user(OTHERCERTIFICATE$2);
        }
        return anyTypeAdd_element_user;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public void removeOtherCertificate(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(OTHERCERTIFICATE$2, i);
        }
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public String getId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$4);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public XmlID xgetId() {
        XmlID xmlID;
        synchronized (monitor()) {
            check_orphaned();
            xmlID = (XmlID) get_store().find_attribute_user(ID$4);
        }
        return xmlID;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public boolean isSetId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$4) != null;
        }
        return z;
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public void setId(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$4);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public void xsetId(XmlID xmlID) {
        synchronized (monitor()) {
            check_orphaned();
            XmlID xmlID2 = (XmlID) get_store().find_attribute_user(ID$4);
            if (xmlID2 == null) {
                xmlID2 = (XmlID) get_store().add_attribute_user(ID$4);
            }
            xmlID2.set(xmlID);
        }
    }

    @Override // org.etsi.uri.x01903.v13.CertificateValuesType
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$4);
        }
    }
}
