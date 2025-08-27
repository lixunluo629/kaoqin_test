package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.w3.x2000.x09.xmldsig.ObjectType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/impl/ObjectTypeImpl.class */
public class ObjectTypeImpl extends XmlComplexContentImpl implements ObjectType {
    private static final QName ID$0 = new QName("", PackageRelationship.ID_ATTRIBUTE_NAME);
    private static final QName MIMETYPE$2 = new QName("", "MimeType");
    private static final QName ENCODING$4 = new QName("", "Encoding");

    public ObjectTypeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public String getId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public XmlID xgetId() {
        XmlID xmlID;
        synchronized (monitor()) {
            check_orphaned();
            xmlID = (XmlID) get_store().find_attribute_user(ID$0);
        }
        return xmlID;
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public boolean isSetId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$0) != null;
        }
        return z;
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public void setId(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$0);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public void xsetId(XmlID xmlID) {
        synchronized (monitor()) {
            check_orphaned();
            XmlID xmlID2 = (XmlID) get_store().find_attribute_user(ID$0);
            if (xmlID2 == null) {
                xmlID2 = (XmlID) get_store().add_attribute_user(ID$0);
            }
            xmlID2.set(xmlID);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$0);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public String getMimeType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MIMETYPE$2);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public XmlString xgetMimeType() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(MIMETYPE$2);
        }
        return xmlString;
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public boolean isSetMimeType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MIMETYPE$2) != null;
        }
        return z;
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public void setMimeType(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MIMETYPE$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MIMETYPE$2);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public void xsetMimeType(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(MIMETYPE$2);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(MIMETYPE$2);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public void unsetMimeType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MIMETYPE$2);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public String getEncoding() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENCODING$4);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public XmlAnyURI xgetEncoding() {
        XmlAnyURI xmlAnyURI;
        synchronized (monitor()) {
            check_orphaned();
            xmlAnyURI = (XmlAnyURI) get_store().find_attribute_user(ENCODING$4);
        }
        return xmlAnyURI;
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public boolean isSetEncoding() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ENCODING$4) != null;
        }
        return z;
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public void setEncoding(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENCODING$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ENCODING$4);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public void xsetEncoding(XmlAnyURI xmlAnyURI) {
        synchronized (monitor()) {
            check_orphaned();
            XmlAnyURI xmlAnyURI2 = (XmlAnyURI) get_store().find_attribute_user(ENCODING$4);
            if (xmlAnyURI2 == null) {
                xmlAnyURI2 = (XmlAnyURI) get_store().add_attribute_user(ENCODING$4);
            }
            xmlAnyURI2.set(xmlAnyURI);
        }
    }

    @Override // org.w3.x2000.x09.xmldsig.ObjectType
    public void unsetEncoding() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ENCODING$4);
        }
    }
}
