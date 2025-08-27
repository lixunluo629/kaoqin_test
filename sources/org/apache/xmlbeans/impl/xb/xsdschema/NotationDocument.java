package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NotationDocument.class */
public interface NotationDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(NotationDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("notation3381doctype");

    Notation getNotation();

    void setNotation(Notation notation);

    Notation addNewNotation();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NotationDocument$Notation.class */
    public interface Notation extends Annotated {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Notation.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("notation8b1felemtype");

        String getName();

        XmlNCName xgetName();

        void setName(String str);

        void xsetName(XmlNCName xmlNCName);

        String getPublic();

        Public xgetPublic();

        boolean isSetPublic();

        void setPublic(String str);

        void xsetPublic(Public r1);

        void unsetPublic();

        String getSystem();

        XmlAnyURI xgetSystem();

        boolean isSetSystem();

        void setSystem(String str);

        void xsetSystem(XmlAnyURI xmlAnyURI);

        void unsetSystem();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NotationDocument$Notation$Factory.class */
        public static final class Factory {
            public static Notation newInstance() {
                return (Notation) XmlBeans.getContextTypeLoader().newInstance(Notation.type, null);
            }

            public static Notation newInstance(XmlOptions options) {
                return (Notation) XmlBeans.getContextTypeLoader().newInstance(Notation.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NotationDocument$Factory.class */
    public static final class Factory {
        public static NotationDocument newInstance() {
            return (NotationDocument) XmlBeans.getContextTypeLoader().newInstance(NotationDocument.type, null);
        }

        public static NotationDocument newInstance(XmlOptions options) {
            return (NotationDocument) XmlBeans.getContextTypeLoader().newInstance(NotationDocument.type, options);
        }

        public static NotationDocument parse(String xmlAsString) throws XmlException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, NotationDocument.type, (XmlOptions) null);
        }

        public static NotationDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, NotationDocument.type, options);
        }

        public static NotationDocument parse(File file) throws XmlException, IOException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(file, NotationDocument.type, (XmlOptions) null);
        }

        public static NotationDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(file, NotationDocument.type, options);
        }

        public static NotationDocument parse(URL u) throws XmlException, IOException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(u, NotationDocument.type, (XmlOptions) null);
        }

        public static NotationDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(u, NotationDocument.type, options);
        }

        public static NotationDocument parse(InputStream is) throws XmlException, IOException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(is, NotationDocument.type, (XmlOptions) null);
        }

        public static NotationDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(is, NotationDocument.type, options);
        }

        public static NotationDocument parse(Reader r) throws XmlException, IOException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(r, NotationDocument.type, (XmlOptions) null);
        }

        public static NotationDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(r, NotationDocument.type, options);
        }

        public static NotationDocument parse(XMLStreamReader sr) throws XmlException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(sr, NotationDocument.type, (XmlOptions) null);
        }

        public static NotationDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(sr, NotationDocument.type, options);
        }

        public static NotationDocument parse(Node node) throws XmlException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(node, NotationDocument.type, (XmlOptions) null);
        }

        public static NotationDocument parse(Node node, XmlOptions options) throws XmlException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(node, NotationDocument.type, options);
        }

        public static NotationDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(xis, NotationDocument.type, (XmlOptions) null);
        }

        public static NotationDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (NotationDocument) XmlBeans.getContextTypeLoader().parse(xis, NotationDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NotationDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NotationDocument.type, options);
        }

        private Factory() {
        }
    }
}
