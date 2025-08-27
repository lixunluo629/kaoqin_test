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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/MaxInclusiveDocument.class */
public interface MaxInclusiveDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(MaxInclusiveDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("maxinclusive93dbdoctype");

    Facet getMaxInclusive();

    void setMaxInclusive(Facet facet);

    Facet addNewMaxInclusive();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/MaxInclusiveDocument$Factory.class */
    public static final class Factory {
        public static MaxInclusiveDocument newInstance() {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().newInstance(MaxInclusiveDocument.type, null);
        }

        public static MaxInclusiveDocument newInstance(XmlOptions options) {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().newInstance(MaxInclusiveDocument.type, options);
        }

        public static MaxInclusiveDocument parse(String xmlAsString) throws XmlException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, MaxInclusiveDocument.type, (XmlOptions) null);
        }

        public static MaxInclusiveDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, MaxInclusiveDocument.type, options);
        }

        public static MaxInclusiveDocument parse(File file) throws XmlException, IOException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(file, MaxInclusiveDocument.type, (XmlOptions) null);
        }

        public static MaxInclusiveDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(file, MaxInclusiveDocument.type, options);
        }

        public static MaxInclusiveDocument parse(URL u) throws XmlException, IOException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(u, MaxInclusiveDocument.type, (XmlOptions) null);
        }

        public static MaxInclusiveDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(u, MaxInclusiveDocument.type, options);
        }

        public static MaxInclusiveDocument parse(InputStream is) throws XmlException, IOException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(is, MaxInclusiveDocument.type, (XmlOptions) null);
        }

        public static MaxInclusiveDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(is, MaxInclusiveDocument.type, options);
        }

        public static MaxInclusiveDocument parse(Reader r) throws XmlException, IOException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(r, MaxInclusiveDocument.type, (XmlOptions) null);
        }

        public static MaxInclusiveDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(r, MaxInclusiveDocument.type, options);
        }

        public static MaxInclusiveDocument parse(XMLStreamReader sr) throws XmlException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(sr, MaxInclusiveDocument.type, (XmlOptions) null);
        }

        public static MaxInclusiveDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(sr, MaxInclusiveDocument.type, options);
        }

        public static MaxInclusiveDocument parse(Node node) throws XmlException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(node, MaxInclusiveDocument.type, (XmlOptions) null);
        }

        public static MaxInclusiveDocument parse(Node node, XmlOptions options) throws XmlException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(node, MaxInclusiveDocument.type, options);
        }

        public static MaxInclusiveDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(xis, MaxInclusiveDocument.type, (XmlOptions) null);
        }

        public static MaxInclusiveDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (MaxInclusiveDocument) XmlBeans.getContextTypeLoader().parse(xis, MaxInclusiveDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, MaxInclusiveDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, MaxInclusiveDocument.type, options);
        }

        private Factory() {
        }
    }
}
