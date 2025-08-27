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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/LengthDocument.class */
public interface LengthDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(LengthDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("length7edddoctype");

    NumFacet getLength();

    void setLength(NumFacet numFacet);

    NumFacet addNewLength();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/LengthDocument$Factory.class */
    public static final class Factory {
        public static LengthDocument newInstance() {
            return (LengthDocument) XmlBeans.getContextTypeLoader().newInstance(LengthDocument.type, null);
        }

        public static LengthDocument newInstance(XmlOptions options) {
            return (LengthDocument) XmlBeans.getContextTypeLoader().newInstance(LengthDocument.type, options);
        }

        public static LengthDocument parse(String xmlAsString) throws XmlException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, LengthDocument.type, (XmlOptions) null);
        }

        public static LengthDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, LengthDocument.type, options);
        }

        public static LengthDocument parse(File file) throws XmlException, IOException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(file, LengthDocument.type, (XmlOptions) null);
        }

        public static LengthDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(file, LengthDocument.type, options);
        }

        public static LengthDocument parse(URL u) throws XmlException, IOException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(u, LengthDocument.type, (XmlOptions) null);
        }

        public static LengthDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(u, LengthDocument.type, options);
        }

        public static LengthDocument parse(InputStream is) throws XmlException, IOException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(is, LengthDocument.type, (XmlOptions) null);
        }

        public static LengthDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(is, LengthDocument.type, options);
        }

        public static LengthDocument parse(Reader r) throws XmlException, IOException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(r, LengthDocument.type, (XmlOptions) null);
        }

        public static LengthDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(r, LengthDocument.type, options);
        }

        public static LengthDocument parse(XMLStreamReader sr) throws XmlException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(sr, LengthDocument.type, (XmlOptions) null);
        }

        public static LengthDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(sr, LengthDocument.type, options);
        }

        public static LengthDocument parse(Node node) throws XmlException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(node, LengthDocument.type, (XmlOptions) null);
        }

        public static LengthDocument parse(Node node, XmlOptions options) throws XmlException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(node, LengthDocument.type, options);
        }

        public static LengthDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(xis, LengthDocument.type, (XmlOptions) null);
        }

        public static LengthDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (LengthDocument) XmlBeans.getContextTypeLoader().parse(xis, LengthDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, LengthDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, LengthDocument.type, options);
        }

        private Factory() {
        }
    }
}
