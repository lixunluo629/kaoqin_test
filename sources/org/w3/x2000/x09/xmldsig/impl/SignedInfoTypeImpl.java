package org.w3.x2000.x09.xmldsig.impl;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.poifs.crypt.dsig.facets.SignatureFacet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.w3.x2000.x09.xmldsig.CanonicalizationMethodType;
import org.w3.x2000.x09.xmldsig.ReferenceType;
import org.w3.x2000.x09.xmldsig.SignatureMethodType;
import org.w3.x2000.x09.xmldsig.SignedInfoType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/impl/SignedInfoTypeImpl.class */
public class SignedInfoTypeImpl extends XmlComplexContentImpl implements SignedInfoType {
    private static final QName CANONICALIZATIONMETHOD$0 = new QName(SignatureFacet.XML_DIGSIG_NS, "CanonicalizationMethod");
    private static final QName SIGNATUREMETHOD$2 = new QName(SignatureFacet.XML_DIGSIG_NS, "SignatureMethod");
    private static final QName REFERENCE$4 = new QName(SignatureFacet.XML_DIGSIG_NS, StandardRoles.REFERENCE);
    private static final QName ID$6 = new QName("", PackageRelationship.ID_ATTRIBUTE_NAME);

    public SignedInfoTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public CanonicalizationMethodType getCanonicalizationMethod() {
        synchronized (monitor()) {
            check_orphaned();
            CanonicalizationMethodType canonicalizationMethodType = (CanonicalizationMethodType) get_store().find_element_user(CANONICALIZATIONMETHOD$0, 0);
            if (canonicalizationMethodType == null) {
                return null;
            }
            return canonicalizationMethodType;
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public void setCanonicalizationMethod(CanonicalizationMethodType canonicalizationMethodType) {
        synchronized (monitor()) {
            check_orphaned();
            CanonicalizationMethodType canonicalizationMethodType2 = (CanonicalizationMethodType) get_store().find_element_user(CANONICALIZATIONMETHOD$0, 0);
            if (canonicalizationMethodType2 == null) {
                canonicalizationMethodType2 = (CanonicalizationMethodType) get_store().add_element_user(CANONICALIZATIONMETHOD$0);
            }
            canonicalizationMethodType2.set(canonicalizationMethodType);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public CanonicalizationMethodType addNewCanonicalizationMethod() {
        CanonicalizationMethodType canonicalizationMethodType;
        synchronized (monitor()) {
            check_orphaned();
            canonicalizationMethodType = (CanonicalizationMethodType) get_store().add_element_user(CANONICALIZATIONMETHOD$0);
        }
        return canonicalizationMethodType;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public SignatureMethodType getSignatureMethod() {
        synchronized (monitor()) {
            check_orphaned();
            SignatureMethodType signatureMethodTypeFind_element_user = get_store().find_element_user(SIGNATUREMETHOD$2, 0);
            if (signatureMethodTypeFind_element_user == null) {
                return null;
            }
            return signatureMethodTypeFind_element_user;
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public void setSignatureMethod(SignatureMethodType signatureMethodType) {
        synchronized (monitor()) {
            check_orphaned();
            SignatureMethodType signatureMethodTypeFind_element_user = get_store().find_element_user(SIGNATUREMETHOD$2, 0);
            if (signatureMethodTypeFind_element_user == null) {
                signatureMethodTypeFind_element_user = (SignatureMethodType) get_store().add_element_user(SIGNATUREMETHOD$2);
            }
            signatureMethodTypeFind_element_user.set(signatureMethodType);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public SignatureMethodType addNewSignatureMethod() {
        SignatureMethodType signatureMethodTypeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            signatureMethodTypeAdd_element_user = get_store().add_element_user(SIGNATUREMETHOD$2);
        }
        return signatureMethodTypeAdd_element_user;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public List<ReferenceType> getReferenceList() {
        1ReferenceList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1ReferenceList(this);
        }
        return r0;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public ReferenceType[] getReferenceArray() {
        ReferenceType[] referenceTypeArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(REFERENCE$4, arrayList);
            referenceTypeArr = new ReferenceType[arrayList.size()];
            arrayList.toArray(referenceTypeArr);
        }
        return referenceTypeArr;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public ReferenceType getReferenceArray(int i) {
        ReferenceType referenceType;
        synchronized (monitor()) {
            check_orphaned();
            referenceType = (ReferenceType) get_store().find_element_user(REFERENCE$4, i);
            if (referenceType == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return referenceType;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public int sizeOfReferenceArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(REFERENCE$4);
        }
        return iCount_elements;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public void setReferenceArray(ReferenceType[] referenceTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(referenceTypeArr, REFERENCE$4);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public void setReferenceArray(int i, ReferenceType referenceType) {
        synchronized (monitor()) {
            check_orphaned();
            ReferenceType referenceType2 = (ReferenceType) get_store().find_element_user(REFERENCE$4, i);
            if (referenceType2 == null) {
                throw new IndexOutOfBoundsException();
            }
            referenceType2.set(referenceType);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public ReferenceType insertNewReference(int i) {
        ReferenceType referenceType;
        synchronized (monitor()) {
            check_orphaned();
            referenceType = (ReferenceType) get_store().insert_element_user(REFERENCE$4, i);
        }
        return referenceType;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public ReferenceType addNewReference() {
        ReferenceType referenceType;
        synchronized (monitor()) {
            check_orphaned();
            referenceType = (ReferenceType) get_store().add_element_user(REFERENCE$4);
        }
        return referenceType;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public void removeReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(REFERENCE$4, i);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
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

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public XmlID xgetId() {
        XmlID xmlID;
        synchronized (monitor()) {
            check_orphaned();
            xmlID = (XmlID) get_store().find_attribute_user(ID$6);
        }
        return xmlID;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public boolean isSetId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$6) != null;
        }
        return z;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
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

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
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

    @Override // org.w3.x2000.x09.xmldsig.SignedInfoType
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$6);
        }
    }
}
