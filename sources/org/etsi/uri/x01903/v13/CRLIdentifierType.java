package org.etsi.uri.x01903.v13;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.net.URL;
import java.util.Calendar;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CRLIdentifierType.class */
public interface CRLIdentifierType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CRLIdentifierType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("crlidentifiertypeb702type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CRLIdentifierType$Factory.class */
    public static final class Factory {
        public static CRLIdentifierType newInstance() {
            return (CRLIdentifierType) POIXMLTypeLoader.newInstance(CRLIdentifierType.type, null);
        }

        public static CRLIdentifierType newInstance(XmlOptions xmlOptions) {
            return (CRLIdentifierType) POIXMLTypeLoader.newInstance(CRLIdentifierType.type, xmlOptions);
        }

        public static CRLIdentifierType parse(String str) throws XmlException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(str, CRLIdentifierType.type, (XmlOptions) null);
        }

        public static CRLIdentifierType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(str, CRLIdentifierType.type, xmlOptions);
        }

        public static CRLIdentifierType parse(File file) throws XmlException, IOException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(file, CRLIdentifierType.type, (XmlOptions) null);
        }

        public static CRLIdentifierType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(file, CRLIdentifierType.type, xmlOptions);
        }

        public static CRLIdentifierType parse(URL url) throws XmlException, IOException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(url, CRLIdentifierType.type, (XmlOptions) null);
        }

        public static CRLIdentifierType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(url, CRLIdentifierType.type, xmlOptions);
        }

        public static CRLIdentifierType parse(InputStream inputStream) throws XmlException, IOException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(inputStream, CRLIdentifierType.type, (XmlOptions) null);
        }

        public static CRLIdentifierType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(inputStream, CRLIdentifierType.type, xmlOptions);
        }

        public static CRLIdentifierType parse(Reader reader) throws XmlException, IOException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(reader, CRLIdentifierType.type, (XmlOptions) null);
        }

        public static CRLIdentifierType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(reader, CRLIdentifierType.type, xmlOptions);
        }

        public static CRLIdentifierType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(xMLStreamReader, CRLIdentifierType.type, (XmlOptions) null);
        }

        public static CRLIdentifierType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(xMLStreamReader, CRLIdentifierType.type, xmlOptions);
        }

        public static CRLIdentifierType parse(Node node) throws XmlException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(node, CRLIdentifierType.type, (XmlOptions) null);
        }

        public static CRLIdentifierType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(node, CRLIdentifierType.type, xmlOptions);
        }

        public static CRLIdentifierType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(xMLInputStream, CRLIdentifierType.type, (XmlOptions) null);
        }

        public static CRLIdentifierType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CRLIdentifierType) POIXMLTypeLoader.parse(xMLInputStream, CRLIdentifierType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CRLIdentifierType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CRLIdentifierType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getIssuer();

    XmlString xgetIssuer();

    void setIssuer(String str);

    void xsetIssuer(XmlString xmlString);

    Calendar getIssueTime();

    XmlDateTime xgetIssueTime();

    void setIssueTime(Calendar calendar);

    void xsetIssueTime(XmlDateTime xmlDateTime);

    BigInteger getNumber();

    XmlInteger xgetNumber();

    boolean isSetNumber();

    void setNumber(BigInteger bigInteger);

    void xsetNumber(XmlInteger xmlInteger);

    void unsetNumber();

    String getURI();

    XmlAnyURI xgetURI();

    boolean isSetURI();

    void setURI(String str);

    void xsetURI(XmlAnyURI xmlAnyURI);

    void unsetURI();
}
