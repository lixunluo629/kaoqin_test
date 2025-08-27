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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NarrowMaxMin.class */
public interface NarrowMaxMin extends LocalElement {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(NarrowMaxMin.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("narrowmaxmin926atype");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NarrowMaxMin$MinOccurs.class */
    public interface MinOccurs extends XmlNonNegativeInteger {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(MinOccurs.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("minoccurs1acbattrtype");

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NarrowMaxMin$MinOccurs$Factory.class */
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

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NarrowMaxMin$MaxOccurs.class */
    public interface MaxOccurs extends AllNNI {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(MaxOccurs.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("maxoccursd85dattrtype");

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

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NarrowMaxMin$MaxOccurs$Factory.class */
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

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NarrowMaxMin$Factory.class */
    public static final class Factory {
        public static NarrowMaxMin newInstance() {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().newInstance(NarrowMaxMin.type, null);
        }

        public static NarrowMaxMin newInstance(XmlOptions options) {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().newInstance(NarrowMaxMin.type, options);
        }

        public static NarrowMaxMin parse(String xmlAsString) throws XmlException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(xmlAsString, NarrowMaxMin.type, (XmlOptions) null);
        }

        public static NarrowMaxMin parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(xmlAsString, NarrowMaxMin.type, options);
        }

        public static NarrowMaxMin parse(File file) throws XmlException, IOException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(file, NarrowMaxMin.type, (XmlOptions) null);
        }

        public static NarrowMaxMin parse(File file, XmlOptions options) throws XmlException, IOException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(file, NarrowMaxMin.type, options);
        }

        public static NarrowMaxMin parse(URL u) throws XmlException, IOException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(u, NarrowMaxMin.type, (XmlOptions) null);
        }

        public static NarrowMaxMin parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(u, NarrowMaxMin.type, options);
        }

        public static NarrowMaxMin parse(InputStream is) throws XmlException, IOException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(is, NarrowMaxMin.type, (XmlOptions) null);
        }

        public static NarrowMaxMin parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(is, NarrowMaxMin.type, options);
        }

        public static NarrowMaxMin parse(Reader r) throws XmlException, IOException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(r, NarrowMaxMin.type, (XmlOptions) null);
        }

        public static NarrowMaxMin parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(r, NarrowMaxMin.type, options);
        }

        public static NarrowMaxMin parse(XMLStreamReader sr) throws XmlException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(sr, NarrowMaxMin.type, (XmlOptions) null);
        }

        public static NarrowMaxMin parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(sr, NarrowMaxMin.type, options);
        }

        public static NarrowMaxMin parse(Node node) throws XmlException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(node, NarrowMaxMin.type, (XmlOptions) null);
        }

        public static NarrowMaxMin parse(Node node, XmlOptions options) throws XmlException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(node, NarrowMaxMin.type, options);
        }

        public static NarrowMaxMin parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(xis, NarrowMaxMin.type, (XmlOptions) null);
        }

        public static NarrowMaxMin parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (NarrowMaxMin) XmlBeans.getContextTypeLoader().parse(xis, NarrowMaxMin.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NarrowMaxMin.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NarrowMaxMin.type, options);
        }

        private Factory() {
        }
    }
}
