package org.w3.x2000.x09.xmldsig;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/ReferenceType.class */
public interface ReferenceType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ReferenceType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("referencetypef44ctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/ReferenceType$Factory.class */
    public static final class Factory {
        public static ReferenceType newInstance() {
            return (ReferenceType) POIXMLTypeLoader.newInstance(ReferenceType.type, null);
        }

        public static ReferenceType newInstance(XmlOptions xmlOptions) {
            return (ReferenceType) POIXMLTypeLoader.newInstance(ReferenceType.type, xmlOptions);
        }

        public static ReferenceType parse(String str) throws XmlException {
            return (ReferenceType) POIXMLTypeLoader.parse(str, ReferenceType.type, (XmlOptions) null);
        }

        public static ReferenceType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (ReferenceType) POIXMLTypeLoader.parse(str, ReferenceType.type, xmlOptions);
        }

        public static ReferenceType parse(File file) throws XmlException, IOException {
            return (ReferenceType) POIXMLTypeLoader.parse(file, ReferenceType.type, (XmlOptions) null);
        }

        public static ReferenceType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ReferenceType) POIXMLTypeLoader.parse(file, ReferenceType.type, xmlOptions);
        }

        public static ReferenceType parse(URL url) throws XmlException, IOException {
            return (ReferenceType) POIXMLTypeLoader.parse(url, ReferenceType.type, (XmlOptions) null);
        }

        public static ReferenceType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ReferenceType) POIXMLTypeLoader.parse(url, ReferenceType.type, xmlOptions);
        }

        public static ReferenceType parse(InputStream inputStream) throws XmlException, IOException {
            return (ReferenceType) POIXMLTypeLoader.parse(inputStream, ReferenceType.type, (XmlOptions) null);
        }

        public static ReferenceType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ReferenceType) POIXMLTypeLoader.parse(inputStream, ReferenceType.type, xmlOptions);
        }

        public static ReferenceType parse(Reader reader) throws XmlException, IOException {
            return (ReferenceType) POIXMLTypeLoader.parse(reader, ReferenceType.type, (XmlOptions) null);
        }

        public static ReferenceType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ReferenceType) POIXMLTypeLoader.parse(reader, ReferenceType.type, xmlOptions);
        }

        public static ReferenceType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (ReferenceType) POIXMLTypeLoader.parse(xMLStreamReader, ReferenceType.type, (XmlOptions) null);
        }

        public static ReferenceType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (ReferenceType) POIXMLTypeLoader.parse(xMLStreamReader, ReferenceType.type, xmlOptions);
        }

        public static ReferenceType parse(Node node) throws XmlException {
            return (ReferenceType) POIXMLTypeLoader.parse(node, ReferenceType.type, (XmlOptions) null);
        }

        public static ReferenceType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (ReferenceType) POIXMLTypeLoader.parse(node, ReferenceType.type, xmlOptions);
        }

        public static ReferenceType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (ReferenceType) POIXMLTypeLoader.parse(xMLInputStream, ReferenceType.type, (XmlOptions) null);
        }

        public static ReferenceType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (ReferenceType) POIXMLTypeLoader.parse(xMLInputStream, ReferenceType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, ReferenceType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, ReferenceType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    TransformsType getTransforms();

    boolean isSetTransforms();

    void setTransforms(TransformsType transformsType);

    TransformsType addNewTransforms();

    void unsetTransforms();

    DigestMethodType getDigestMethod();

    void setDigestMethod(DigestMethodType digestMethodType);

    DigestMethodType addNewDigestMethod();

    byte[] getDigestValue();

    DigestValueType xgetDigestValue();

    void setDigestValue(byte[] bArr);

    void xsetDigestValue(DigestValueType digestValueType);

    String getId();

    XmlID xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlID xmlID);

    void unsetId();

    String getURI();

    XmlAnyURI xgetURI();

    boolean isSetURI();

    void setURI(String str);

    void xsetURI(XmlAnyURI xmlAnyURI);

    void unsetURI();

    String getType();

    XmlAnyURI xgetType();

    boolean isSetType();

    void setType(String str);

    void xsetType(XmlAnyURI xmlAnyURI);

    void unsetType();
}
