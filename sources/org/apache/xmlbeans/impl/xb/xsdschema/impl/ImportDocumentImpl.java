package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/ImportDocumentImpl.class */
public class ImportDocumentImpl extends XmlComplexContentImpl implements ImportDocument {
    private static final long serialVersionUID = 1;
    private static final QName IMPORT$0 = new QName("http://www.w3.org/2001/XMLSchema", DefaultBeanDefinitionDocumentReader.IMPORT_ELEMENT);

    public ImportDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument
    public ImportDocument.Import getImport() {
        synchronized (monitor()) {
            check_orphaned();
            ImportDocument.Import target = (ImportDocument.Import) get_store().find_element_user(IMPORT$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument
    public void setImport(ImportDocument.Import ximport) {
        generatedSetterHelperImpl(ximport, IMPORT$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument
    public ImportDocument.Import addNewImport() {
        ImportDocument.Import target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ImportDocument.Import) get_store().add_element_user(IMPORT$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/ImportDocumentImpl$ImportImpl.class */
    public static class ImportImpl extends AnnotatedImpl implements ImportDocument.Import {
        private static final long serialVersionUID = 1;
        private static final QName NAMESPACE$0 = new QName("", "namespace");
        private static final QName SCHEMALOCATION$2 = new QName("", "schemaLocation");

        public ImportImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import
        public String getNamespace() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAMESPACE$0);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import
        public XmlAnyURI xgetNamespace() {
            XmlAnyURI target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlAnyURI) get_store().find_attribute_user(NAMESPACE$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import
        public boolean isSetNamespace() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(NAMESPACE$0) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import
        public void setNamespace(String namespace) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAMESPACE$0);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(NAMESPACE$0);
                }
                target.setStringValue(namespace);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import
        public void xsetNamespace(XmlAnyURI namespace) {
            synchronized (monitor()) {
                check_orphaned();
                XmlAnyURI target = (XmlAnyURI) get_store().find_attribute_user(NAMESPACE$0);
                if (target == null) {
                    target = (XmlAnyURI) get_store().add_attribute_user(NAMESPACE$0);
                }
                target.set(namespace);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import
        public void unsetNamespace() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(NAMESPACE$0);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import
        public String getSchemaLocation() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(SCHEMALOCATION$2);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import
        public XmlAnyURI xgetSchemaLocation() {
            XmlAnyURI target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlAnyURI) get_store().find_attribute_user(SCHEMALOCATION$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import
        public boolean isSetSchemaLocation() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(SCHEMALOCATION$2) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import
        public void setSchemaLocation(String schemaLocation) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(SCHEMALOCATION$2);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(SCHEMALOCATION$2);
                }
                target.setStringValue(schemaLocation);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import
        public void xsetSchemaLocation(XmlAnyURI schemaLocation) {
            synchronized (monitor()) {
                check_orphaned();
                XmlAnyURI target = (XmlAnyURI) get_store().find_attribute_user(SCHEMALOCATION$2);
                if (target == null) {
                    target = (XmlAnyURI) get_store().add_attribute_user(SCHEMALOCATION$2);
                }
                target.set(schemaLocation);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import
        public void unsetSchemaLocation() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(SCHEMALOCATION$2);
            }
        }
    }
}
