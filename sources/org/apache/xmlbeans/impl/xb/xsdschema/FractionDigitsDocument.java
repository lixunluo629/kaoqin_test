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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FractionDigitsDocument.class */
public interface FractionDigitsDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(FractionDigitsDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("fractiondigitsed7bdoctype");

    NumFacet getFractionDigits();

    void setFractionDigits(NumFacet numFacet);

    NumFacet addNewFractionDigits();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FractionDigitsDocument$Factory.class */
    public static final class Factory {
        public static FractionDigitsDocument newInstance() {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().newInstance(FractionDigitsDocument.type, null);
        }

        public static FractionDigitsDocument newInstance(XmlOptions options) {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().newInstance(FractionDigitsDocument.type, options);
        }

        public static FractionDigitsDocument parse(String xmlAsString) throws XmlException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, FractionDigitsDocument.type, (XmlOptions) null);
        }

        public static FractionDigitsDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, FractionDigitsDocument.type, options);
        }

        public static FractionDigitsDocument parse(File file) throws XmlException, IOException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(file, FractionDigitsDocument.type, (XmlOptions) null);
        }

        public static FractionDigitsDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(file, FractionDigitsDocument.type, options);
        }

        public static FractionDigitsDocument parse(URL u) throws XmlException, IOException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(u, FractionDigitsDocument.type, (XmlOptions) null);
        }

        public static FractionDigitsDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(u, FractionDigitsDocument.type, options);
        }

        public static FractionDigitsDocument parse(InputStream is) throws XmlException, IOException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(is, FractionDigitsDocument.type, (XmlOptions) null);
        }

        public static FractionDigitsDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(is, FractionDigitsDocument.type, options);
        }

        public static FractionDigitsDocument parse(Reader r) throws XmlException, IOException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(r, FractionDigitsDocument.type, (XmlOptions) null);
        }

        public static FractionDigitsDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(r, FractionDigitsDocument.type, options);
        }

        public static FractionDigitsDocument parse(XMLStreamReader sr) throws XmlException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(sr, FractionDigitsDocument.type, (XmlOptions) null);
        }

        public static FractionDigitsDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(sr, FractionDigitsDocument.type, options);
        }

        public static FractionDigitsDocument parse(Node node) throws XmlException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(node, FractionDigitsDocument.type, (XmlOptions) null);
        }

        public static FractionDigitsDocument parse(Node node, XmlOptions options) throws XmlException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(node, FractionDigitsDocument.type, options);
        }

        public static FractionDigitsDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(xis, FractionDigitsDocument.type, (XmlOptions) null);
        }

        public static FractionDigitsDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (FractionDigitsDocument) XmlBeans.getContextTypeLoader().parse(xis, FractionDigitsDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, FractionDigitsDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, FractionDigitsDocument.type, options);
        }

        private Factory() {
        }
    }
}
