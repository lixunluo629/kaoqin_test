package org.etsi.uri.x01903.v13;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CRLRefsType.class */
public interface CRLRefsType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CRLRefsType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("crlrefstype2a59type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CRLRefsType$Factory.class */
    public static final class Factory {
        public static CRLRefsType newInstance() {
            return (CRLRefsType) POIXMLTypeLoader.newInstance(CRLRefsType.type, null);
        }

        public static CRLRefsType newInstance(XmlOptions xmlOptions) {
            return (CRLRefsType) POIXMLTypeLoader.newInstance(CRLRefsType.type, xmlOptions);
        }

        public static CRLRefsType parse(String str) throws XmlException {
            return (CRLRefsType) POIXMLTypeLoader.parse(str, CRLRefsType.type, (XmlOptions) null);
        }

        public static CRLRefsType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CRLRefsType) POIXMLTypeLoader.parse(str, CRLRefsType.type, xmlOptions);
        }

        public static CRLRefsType parse(File file) throws XmlException, IOException {
            return (CRLRefsType) POIXMLTypeLoader.parse(file, CRLRefsType.type, (XmlOptions) null);
        }

        public static CRLRefsType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CRLRefsType) POIXMLTypeLoader.parse(file, CRLRefsType.type, xmlOptions);
        }

        public static CRLRefsType parse(URL url) throws XmlException, IOException {
            return (CRLRefsType) POIXMLTypeLoader.parse(url, CRLRefsType.type, (XmlOptions) null);
        }

        public static CRLRefsType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CRLRefsType) POIXMLTypeLoader.parse(url, CRLRefsType.type, xmlOptions);
        }

        public static CRLRefsType parse(InputStream inputStream) throws XmlException, IOException {
            return (CRLRefsType) POIXMLTypeLoader.parse(inputStream, CRLRefsType.type, (XmlOptions) null);
        }

        public static CRLRefsType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CRLRefsType) POIXMLTypeLoader.parse(inputStream, CRLRefsType.type, xmlOptions);
        }

        public static CRLRefsType parse(Reader reader) throws XmlException, IOException {
            return (CRLRefsType) POIXMLTypeLoader.parse(reader, CRLRefsType.type, (XmlOptions) null);
        }

        public static CRLRefsType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CRLRefsType) POIXMLTypeLoader.parse(reader, CRLRefsType.type, xmlOptions);
        }

        public static CRLRefsType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CRLRefsType) POIXMLTypeLoader.parse(xMLStreamReader, CRLRefsType.type, (XmlOptions) null);
        }

        public static CRLRefsType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CRLRefsType) POIXMLTypeLoader.parse(xMLStreamReader, CRLRefsType.type, xmlOptions);
        }

        public static CRLRefsType parse(Node node) throws XmlException {
            return (CRLRefsType) POIXMLTypeLoader.parse(node, CRLRefsType.type, (XmlOptions) null);
        }

        public static CRLRefsType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CRLRefsType) POIXMLTypeLoader.parse(node, CRLRefsType.type, xmlOptions);
        }

        public static CRLRefsType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CRLRefsType) POIXMLTypeLoader.parse(xMLInputStream, CRLRefsType.type, (XmlOptions) null);
        }

        public static CRLRefsType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CRLRefsType) POIXMLTypeLoader.parse(xMLInputStream, CRLRefsType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CRLRefsType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CRLRefsType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CRLRefType> getCRLRefList();

    CRLRefType[] getCRLRefArray();

    CRLRefType getCRLRefArray(int i);

    int sizeOfCRLRefArray();

    void setCRLRefArray(CRLRefType[] cRLRefTypeArr);

    void setCRLRefArray(int i, CRLRefType cRLRefType);

    CRLRefType insertNewCRLRef(int i);

    CRLRefType addNewCRLRef();

    void removeCRLRef(int i);
}
