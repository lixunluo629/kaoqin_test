package org.etsi.uri.x01903.v13;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/QualifyingPropertiesType.class */
public interface QualifyingPropertiesType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(QualifyingPropertiesType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("qualifyingpropertiestype9e16type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/QualifyingPropertiesType$Factory.class */
    public static final class Factory {
        public static QualifyingPropertiesType newInstance() {
            return (QualifyingPropertiesType) POIXMLTypeLoader.newInstance(QualifyingPropertiesType.type, null);
        }

        public static QualifyingPropertiesType newInstance(XmlOptions xmlOptions) {
            return (QualifyingPropertiesType) POIXMLTypeLoader.newInstance(QualifyingPropertiesType.type, xmlOptions);
        }

        public static QualifyingPropertiesType parse(String str) throws XmlException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(str, QualifyingPropertiesType.type, (XmlOptions) null);
        }

        public static QualifyingPropertiesType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(str, QualifyingPropertiesType.type, xmlOptions);
        }

        public static QualifyingPropertiesType parse(File file) throws XmlException, IOException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(file, QualifyingPropertiesType.type, (XmlOptions) null);
        }

        public static QualifyingPropertiesType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(file, QualifyingPropertiesType.type, xmlOptions);
        }

        public static QualifyingPropertiesType parse(URL url) throws XmlException, IOException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(url, QualifyingPropertiesType.type, (XmlOptions) null);
        }

        public static QualifyingPropertiesType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(url, QualifyingPropertiesType.type, xmlOptions);
        }

        public static QualifyingPropertiesType parse(InputStream inputStream) throws XmlException, IOException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(inputStream, QualifyingPropertiesType.type, (XmlOptions) null);
        }

        public static QualifyingPropertiesType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(inputStream, QualifyingPropertiesType.type, xmlOptions);
        }

        public static QualifyingPropertiesType parse(Reader reader) throws XmlException, IOException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(reader, QualifyingPropertiesType.type, (XmlOptions) null);
        }

        public static QualifyingPropertiesType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(reader, QualifyingPropertiesType.type, xmlOptions);
        }

        public static QualifyingPropertiesType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(xMLStreamReader, QualifyingPropertiesType.type, (XmlOptions) null);
        }

        public static QualifyingPropertiesType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(xMLStreamReader, QualifyingPropertiesType.type, xmlOptions);
        }

        public static QualifyingPropertiesType parse(Node node) throws XmlException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(node, QualifyingPropertiesType.type, (XmlOptions) null);
        }

        public static QualifyingPropertiesType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(node, QualifyingPropertiesType.type, xmlOptions);
        }

        public static QualifyingPropertiesType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(xMLInputStream, QualifyingPropertiesType.type, (XmlOptions) null);
        }

        public static QualifyingPropertiesType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (QualifyingPropertiesType) POIXMLTypeLoader.parse(xMLInputStream, QualifyingPropertiesType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, QualifyingPropertiesType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, QualifyingPropertiesType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    SignedPropertiesType getSignedProperties();

    boolean isSetSignedProperties();

    void setSignedProperties(SignedPropertiesType signedPropertiesType);

    SignedPropertiesType addNewSignedProperties();

    void unsetSignedProperties();

    UnsignedPropertiesType getUnsignedProperties();

    boolean isSetUnsignedProperties();

    void setUnsignedProperties(UnsignedPropertiesType unsignedPropertiesType);

    UnsignedPropertiesType addNewUnsignedProperties();

    void unsetUnsignedProperties();

    String getTarget();

    XmlAnyURI xgetTarget();

    void setTarget(String str);

    void xsetTarget(XmlAnyURI xmlAnyURI);

    String getId();

    XmlID xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlID xmlID);

    void unsetId();
}
