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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/MaxLengthDocument.class */
public interface MaxLengthDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(MaxLengthDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("maxlengthf8abdoctype");

    NumFacet getMaxLength();

    void setMaxLength(NumFacet numFacet);

    NumFacet addNewMaxLength();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/MaxLengthDocument$Factory.class */
    public static final class Factory {
        public static MaxLengthDocument newInstance() {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().newInstance(MaxLengthDocument.type, null);
        }

        public static MaxLengthDocument newInstance(XmlOptions options) {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().newInstance(MaxLengthDocument.type, options);
        }

        public static MaxLengthDocument parse(String xmlAsString) throws XmlException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, MaxLengthDocument.type, (XmlOptions) null);
        }

        public static MaxLengthDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, MaxLengthDocument.type, options);
        }

        public static MaxLengthDocument parse(File file) throws XmlException, IOException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(file, MaxLengthDocument.type, (XmlOptions) null);
        }

        public static MaxLengthDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(file, MaxLengthDocument.type, options);
        }

        public static MaxLengthDocument parse(URL u) throws XmlException, IOException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(u, MaxLengthDocument.type, (XmlOptions) null);
        }

        public static MaxLengthDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(u, MaxLengthDocument.type, options);
        }

        public static MaxLengthDocument parse(InputStream is) throws XmlException, IOException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(is, MaxLengthDocument.type, (XmlOptions) null);
        }

        public static MaxLengthDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(is, MaxLengthDocument.type, options);
        }

        public static MaxLengthDocument parse(Reader r) throws XmlException, IOException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(r, MaxLengthDocument.type, (XmlOptions) null);
        }

        public static MaxLengthDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(r, MaxLengthDocument.type, options);
        }

        public static MaxLengthDocument parse(XMLStreamReader sr) throws XmlException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(sr, MaxLengthDocument.type, (XmlOptions) null);
        }

        public static MaxLengthDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(sr, MaxLengthDocument.type, options);
        }

        public static MaxLengthDocument parse(Node node) throws XmlException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(node, MaxLengthDocument.type, (XmlOptions) null);
        }

        public static MaxLengthDocument parse(Node node, XmlOptions options) throws XmlException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(node, MaxLengthDocument.type, options);
        }

        public static MaxLengthDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(xis, MaxLengthDocument.type, (XmlOptions) null);
        }

        public static MaxLengthDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (MaxLengthDocument) XmlBeans.getContextTypeLoader().parse(xis, MaxLengthDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, MaxLengthDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, MaxLengthDocument.type, options);
        }

        private Factory() {
        }
    }
}
