package org.w3.x2000.x09.xmldsig.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.poifs.crypt.dsig.facets.SignatureFacet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.w3.x2000.x09.xmldsig.KeyInfoType;
import org.w3.x2000.x09.xmldsig.ObjectType;
import org.w3.x2000.x09.xmldsig.SignatureType;
import org.w3.x2000.x09.xmldsig.SignatureValueType;
import org.w3.x2000.x09.xmldsig.SignedInfoType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/impl/SignatureTypeImpl.class */
public class SignatureTypeImpl extends XmlComplexContentImpl implements SignatureType {
    private static final QName SIGNEDINFO$0 = new QName(SignatureFacet.XML_DIGSIG_NS, "SignedInfo");
    private static final QName SIGNATUREVALUE$2 = new QName(SignatureFacet.XML_DIGSIG_NS, "SignatureValue");
    private static final QName KEYINFO$4 = new QName(SignatureFacet.XML_DIGSIG_NS, "KeyInfo");
    private static final QName OBJECT$6 = new QName(SignatureFacet.XML_DIGSIG_NS, "Object");
    private static final QName ID$8 = new QName("", PackageRelationship.ID_ATTRIBUTE_NAME);

    public SignatureTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public SignedInfoType getSignedInfo() {
        synchronized (monitor()) {
            check_orphaned();
            SignedInfoType signedInfoType = (SignedInfoType) get_store().find_element_user(SIGNEDINFO$0, 0);
            if (signedInfoType == null) {
                return null;
            }
            return signedInfoType;
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public void setSignedInfo(SignedInfoType signedInfoType) {
        synchronized (monitor()) {
            check_orphaned();
            SignedInfoType signedInfoType2 = (SignedInfoType) get_store().find_element_user(SIGNEDINFO$0, 0);
            if (signedInfoType2 == null) {
                signedInfoType2 = (SignedInfoType) get_store().add_element_user(SIGNEDINFO$0);
            }
            signedInfoType2.set(signedInfoType);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public SignedInfoType addNewSignedInfo() {
        SignedInfoType signedInfoType;
        synchronized (monitor()) {
            check_orphaned();
            signedInfoType = (SignedInfoType) get_store().add_element_user(SIGNEDINFO$0);
        }
        return signedInfoType;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public SignatureValueType getSignatureValue() {
        synchronized (monitor()) {
            check_orphaned();
            SignatureValueType signatureValueType = (SignatureValueType) get_store().find_element_user(SIGNATUREVALUE$2, 0);
            if (signatureValueType == null) {
                return null;
            }
            return signatureValueType;
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public void setSignatureValue(SignatureValueType signatureValueType) {
        synchronized (monitor()) {
            check_orphaned();
            SignatureValueType signatureValueType2 = (SignatureValueType) get_store().find_element_user(SIGNATUREVALUE$2, 0);
            if (signatureValueType2 == null) {
                signatureValueType2 = (SignatureValueType) get_store().add_element_user(SIGNATUREVALUE$2);
            }
            signatureValueType2.set(signatureValueType);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public SignatureValueType addNewSignatureValue() {
        SignatureValueType signatureValueType;
        synchronized (monitor()) {
            check_orphaned();
            signatureValueType = (SignatureValueType) get_store().add_element_user(SIGNATUREVALUE$2);
        }
        return signatureValueType;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public KeyInfoType getKeyInfo() {
        synchronized (monitor()) {
            check_orphaned();
            KeyInfoType keyInfoTypeFind_element_user = get_store().find_element_user(KEYINFO$4, 0);
            if (keyInfoTypeFind_element_user == null) {
                return null;
            }
            return keyInfoTypeFind_element_user;
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public boolean isSetKeyInfo() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(KEYINFO$4) != 0;
        }
        return z;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public void setKeyInfo(KeyInfoType keyInfoType) {
        synchronized (monitor()) {
            check_orphaned();
            KeyInfoType keyInfoTypeFind_element_user = get_store().find_element_user(KEYINFO$4, 0);
            if (keyInfoTypeFind_element_user == null) {
                keyInfoTypeFind_element_user = (KeyInfoType) get_store().add_element_user(KEYINFO$4);
            }
            keyInfoTypeFind_element_user.set(keyInfoType);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public KeyInfoType addNewKeyInfo() {
        KeyInfoType keyInfoTypeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            keyInfoTypeAdd_element_user = get_store().add_element_user(KEYINFO$4);
        }
        return keyInfoTypeAdd_element_user;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public void unsetKeyInfo() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(KEYINFO$4, 0);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public List<ObjectType> getObjectList() {
        1ObjectList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1ObjectList(this);
        }
        return r0;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public ObjectType[] getObjectArray() {
        ObjectType[] objectTypeArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(OBJECT$6, arrayList);
            objectTypeArr = new ObjectType[arrayList.size()];
            arrayList.toArray(objectTypeArr);
        }
        return objectTypeArr;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public ObjectType getObjectArray(int i) {
        ObjectType objectType;
        synchronized (monitor()) {
            check_orphaned();
            objectType = (ObjectType) get_store().find_element_user(OBJECT$6, i);
            if (objectType == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return objectType;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public int sizeOfObjectArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(OBJECT$6);
        }
        return iCount_elements;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public void setObjectArray(ObjectType[] objectTypeArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(objectTypeArr, OBJECT$6);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public void setObjectArray(int i, ObjectType objectType) {
        synchronized (monitor()) {
            check_orphaned();
            ObjectType objectType2 = (ObjectType) get_store().find_element_user(OBJECT$6, i);
            if (objectType2 == null) {
                throw new IndexOutOfBoundsException();
            }
            objectType2.set(objectType);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public ObjectType insertNewObject(int i) {
        ObjectType objectType;
        synchronized (monitor()) {
            check_orphaned();
            objectType = (ObjectType) get_store().insert_element_user(OBJECT$6, i);
        }
        return objectType;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public ObjectType addNewObject() {
        ObjectType objectType;
        synchronized (monitor()) {
            check_orphaned();
            objectType = (ObjectType) get_store().add_element_user(OBJECT$6);
        }
        return objectType;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public void removeObject(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(OBJECT$6, i);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public String getId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$8);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public XmlID xgetId() {
        XmlID xmlID;
        synchronized (monitor()) {
            check_orphaned();
            xmlID = (XmlID) get_store().find_attribute_user(ID$8);
        }
        return xmlID;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public boolean isSetId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$8) != null;
        }
        return z;
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public void setId(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$8);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public void xsetId(XmlID xmlID) {
        synchronized (monitor()) {
            check_orphaned();
            XmlID xmlID2 = (XmlID) get_store().find_attribute_user(ID$8);
            if (xmlID2 == null) {
                xmlID2 = (XmlID) get_store().add_attribute_user(ID$8);
            }
            xmlID2.set(xmlID);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.SignatureType
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$8);
        }
    }
}
