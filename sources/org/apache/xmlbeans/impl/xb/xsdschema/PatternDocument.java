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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/PatternDocument.class */
public interface PatternDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(PatternDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("pattern9585doctype");

    Pattern getPattern();

    void setPattern(Pattern pattern);

    Pattern addNewPattern();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/PatternDocument$Pattern.class */
    public interface Pattern extends NoFixedFacet {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Pattern.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("pattern6809elemtype");

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/PatternDocument$Pattern$Factory.class */
        public static final class Factory {
            public static Pattern newInstance() {
                return (Pattern) XmlBeans.getContextTypeLoader().newInstance(Pattern.type, null);
            }

            public static Pattern newInstance(XmlOptions options) {
                return (Pattern) XmlBeans.getContextTypeLoader().newInstance(Pattern.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/PatternDocument$Factory.class */
    public static final class Factory {
        public static PatternDocument newInstance() {
            return (PatternDocument) XmlBeans.getContextTypeLoader().newInstance(PatternDocument.type, null);
        }

        public static PatternDocument newInstance(XmlOptions options) {
            return (PatternDocument) XmlBeans.getContextTypeLoader().newInstance(PatternDocument.type, options);
        }

        public static PatternDocument parse(String xmlAsString) throws XmlException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, PatternDocument.type, (XmlOptions) null);
        }

        public static PatternDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, PatternDocument.type, options);
        }

        public static PatternDocument parse(File file) throws XmlException, IOException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(file, PatternDocument.type, (XmlOptions) null);
        }

        public static PatternDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(file, PatternDocument.type, options);
        }

        public static PatternDocument parse(URL u) throws XmlException, IOException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(u, PatternDocument.type, (XmlOptions) null);
        }

        public static PatternDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(u, PatternDocument.type, options);
        }

        public static PatternDocument parse(InputStream is) throws XmlException, IOException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(is, PatternDocument.type, (XmlOptions) null);
        }

        public static PatternDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(is, PatternDocument.type, options);
        }

        public static PatternDocument parse(Reader r) throws XmlException, IOException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(r, PatternDocument.type, (XmlOptions) null);
        }

        public static PatternDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(r, PatternDocument.type, options);
        }

        public static PatternDocument parse(XMLStreamReader sr) throws XmlException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(sr, PatternDocument.type, (XmlOptions) null);
        }

        public static PatternDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(sr, PatternDocument.type, options);
        }

        public static PatternDocument parse(Node node) throws XmlException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(node, PatternDocument.type, (XmlOptions) null);
        }

        public static PatternDocument parse(Node node, XmlOptions options) throws XmlException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(node, PatternDocument.type, options);
        }

        public static PatternDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(xis, PatternDocument.type, (XmlOptions) null);
        }

        public static PatternDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (PatternDocument) XmlBeans.getContextTypeLoader().parse(xis, PatternDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, PatternDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, PatternDocument.type, options);
        }

        private Factory() {
        }
    }
}
