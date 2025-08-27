package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlNonNegativeInteger;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/All.class */
public interface All extends ExplicitGroup {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(All.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("all3c04type");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/All$MinOccurs.class */
    public interface MinOccurs extends XmlNonNegativeInteger {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(MinOccurs.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("minoccurs9283attrtype");

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/All$MinOccurs$Factory.class */
        public static final class Factory {
            public static MinOccurs newValue(Object obj) {
                return (MinOccurs) MinOccurs.type.newValue(obj);
            }

            public static MinOccurs newInstance() {
                return (MinOccurs) XmlBeans.getContextTypeLoader().newInstance(MinOccurs.type, null);
            }

            public static MinOccurs newInstance(XmlOptions options) {
                return (MinOccurs) XmlBeans.getContextTypeLoader().newInstance(MinOccurs.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/All$MaxOccurs.class */
    public interface MaxOccurs extends AllNNI {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(MaxOccurs.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("maxoccurse8b1attrtype");

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AllNNI
        Object getObjectValue();

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AllNNI
        void setObjectValue(Object obj);

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AllNNI
        Object objectValue();

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AllNNI
        void objectSet(Object obj);

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AllNNI
        SchemaType instanceType();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/All$MaxOccurs$Factory.class */
        public static final class Factory {
            public static MaxOccurs newValue(Object obj) {
                return (MaxOccurs) MaxOccurs.type.newValue(obj);
            }

            public static MaxOccurs newInstance() {
                return (MaxOccurs) XmlBeans.getContextTypeLoader().newInstance(MaxOccurs.type, null);
            }

            public static MaxOccurs newInstance(XmlOptions options) {
                return (MaxOccurs) XmlBeans.getContextTypeLoader().newInstance(MaxOccurs.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/All$Factory.class */
    public static final class Factory {
        public static All newInstance() {
            return (All) XmlBeans.getContextTypeLoader().newInstance(All.type, null);
        }

        public static All newInstance(XmlOptions options) {
            return (All) XmlBeans.getContextTypeLoader().newInstance(All.type, options);
        }

        public static All parse(String xmlAsString) throws XmlException {
            return (All) XmlBeans.getContextTypeLoader().parse(xmlAsString, All.type, (XmlOptions) null);
        }

        public static All parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (All) XmlBeans.getContextTypeLoader().parse(xmlAsString, All.type, options);
        }

        public static All parse(File file) throws XmlException, IOException {
            return (All) XmlBeans.getContextTypeLoader().parse(file, All.type, (XmlOptions) null);
        }

        public static All parse(File file, XmlOptions options) throws XmlException, IOException {
            return (All) XmlBeans.getContextTypeLoader().parse(file, All.type, options);
        }

        public static All parse(URL u) throws XmlException, IOException {
            return (All) XmlBeans.getContextTypeLoader().parse(u, All.type, (XmlOptions) null);
        }

        public static All parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (All) XmlBeans.getContextTypeLoader().parse(u, All.type, options);
        }

        public static All parse(InputStream is) throws XmlException, IOException {
            return (All) XmlBeans.getContextTypeLoader().parse(is, All.type, (XmlOptions) null);
        }

        public static All parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (All) XmlBeans.getContextTypeLoader().parse(is, All.type, options);
        }

        public static All parse(Reader r) throws XmlException, IOException {
            return (All) XmlBeans.getContextTypeLoader().parse(r, All.type, (XmlOptions) null);
        }

        public static All parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (All) XmlBeans.getContextTypeLoader().parse(r, All.type, options);
        }

        public static All parse(XMLStreamReader sr) throws XmlException {
            return (All) XmlBeans.getContextTypeLoader().parse(sr, All.type, (XmlOptions) null);
        }

        public static All parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (All) XmlBeans.getContextTypeLoader().parse(sr, All.type, options);
        }

        public static All parse(Node node) throws XmlException {
            return (All) XmlBeans.getContextTypeLoader().parse(node, All.type, (XmlOptions) null);
        }

        public static All parse(Node node, XmlOptions options) throws XmlException {
            return (All) XmlBeans.getContextTypeLoader().parse(node, All.type, options);
        }

        public static All parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (All) XmlBeans.getContextTypeLoader().parse(xis, All.type, (XmlOptions) null);
        }

        public static All parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (All) XmlBeans.getContextTypeLoader().parse(xis, All.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, All.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, All.type, options);
        }

        private Factory() {
        }
    }
}
