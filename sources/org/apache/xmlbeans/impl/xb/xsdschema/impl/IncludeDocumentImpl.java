package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/IncludeDocumentImpl.class */
public class IncludeDocumentImpl extends XmlComplexContentImpl implements IncludeDocument {
    private static final long serialVersionUID = 1;
    private static final QName INCLUDE$0 = new QName("http://www.w3.org/2001/XMLSchema", "include");

    public IncludeDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument
    public IncludeDocument.Include getInclude() {
        synchronized (monitor()) {
            check_orphaned();
            IncludeDocument.Include target = (IncludeDocument.Include) get_store().find_element_user(INCLUDE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument
    public void setInclude(IncludeDocument.Include include) {
        generatedSetterHelperImpl(include, INCLUDE$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument
    public IncludeDocument.Include addNewInclude() {
        IncludeDocument.Include target;
        synchronized (monitor()) {
            check_orphaned();
            target = (IncludeDocument.Include) get_store().add_element_user(INCLUDE$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/IncludeDocumentImpl$IncludeImpl.class */
    public static class IncludeImpl extends AnnotatedImpl implements IncludeDocument.Include {
        private static final long serialVersionUID = 1;
        private static final QName SCHEMALOCATION$0 = new QName("", "schemaLocation");

        public IncludeImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument.Include
        public String getSchemaLocation() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(SCHEMALOCATION$0);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument.Include
        public XmlAnyURI xgetSchemaLocation() {
            XmlAnyURI target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlAnyURI) get_store().find_attribute_user(SCHEMALOCATION$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument.Include
        public void setSchemaLocation(String schemaLocation) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(SCHEMALOCATION$0);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(SCHEMALOCATION$0);
                }
                target.setStringValue(schemaLocation);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument.Include
        public void xsetSchemaLocation(XmlAnyURI schemaLocation) {
            synchronized (monitor()) {
                check_orphaned();
                XmlAnyURI target = (XmlAnyURI) get_store().find_attribute_user(SCHEMALOCATION$0);
                if (target == null) {
                    target = (XmlAnyURI) get_store().add_attribute_user(SCHEMALOCATION$0);
                }
                target.set(schemaLocation);
            }
        }
    }
}
