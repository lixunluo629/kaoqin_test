package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTNumbering.class */
public interface CTNumbering extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTNumbering.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctnumberingfdf9type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTNumbering$Factory.class */
    public static final class Factory {
        public static CTNumbering newInstance() {
            return (CTNumbering) POIXMLTypeLoader.newInstance(CTNumbering.type, null);
        }

        public static CTNumbering newInstance(XmlOptions xmlOptions) {
            return (CTNumbering) POIXMLTypeLoader.newInstance(CTNumbering.type, xmlOptions);
        }

        public static CTNumbering parse(String str) throws XmlException {
            return (CTNumbering) POIXMLTypeLoader.parse(str, CTNumbering.type, (XmlOptions) null);
        }

        public static CTNumbering parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTNumbering) POIXMLTypeLoader.parse(str, CTNumbering.type, xmlOptions);
        }

        public static CTNumbering parse(File file) throws XmlException, IOException {
            return (CTNumbering) POIXMLTypeLoader.parse(file, CTNumbering.type, (XmlOptions) null);
        }

        public static CTNumbering parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumbering) POIXMLTypeLoader.parse(file, CTNumbering.type, xmlOptions);
        }

        public static CTNumbering parse(URL url) throws XmlException, IOException {
            return (CTNumbering) POIXMLTypeLoader.parse(url, CTNumbering.type, (XmlOptions) null);
        }

        public static CTNumbering parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumbering) POIXMLTypeLoader.parse(url, CTNumbering.type, xmlOptions);
        }

        public static CTNumbering parse(InputStream inputStream) throws XmlException, IOException {
            return (CTNumbering) POIXMLTypeLoader.parse(inputStream, CTNumbering.type, (XmlOptions) null);
        }

        public static CTNumbering parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumbering) POIXMLTypeLoader.parse(inputStream, CTNumbering.type, xmlOptions);
        }

        public static CTNumbering parse(Reader reader) throws XmlException, IOException {
            return (CTNumbering) POIXMLTypeLoader.parse(reader, CTNumbering.type, (XmlOptions) null);
        }

        public static CTNumbering parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumbering) POIXMLTypeLoader.parse(reader, CTNumbering.type, xmlOptions);
        }

        public static CTNumbering parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTNumbering) POIXMLTypeLoader.parse(xMLStreamReader, CTNumbering.type, (XmlOptions) null);
        }

        public static CTNumbering parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTNumbering) POIXMLTypeLoader.parse(xMLStreamReader, CTNumbering.type, xmlOptions);
        }

        public static CTNumbering parse(Node node) throws XmlException {
            return (CTNumbering) POIXMLTypeLoader.parse(node, CTNumbering.type, (XmlOptions) null);
        }

        public static CTNumbering parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTNumbering) POIXMLTypeLoader.parse(node, CTNumbering.type, xmlOptions);
        }

        public static CTNumbering parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTNumbering) POIXMLTypeLoader.parse(xMLInputStream, CTNumbering.type, (XmlOptions) null);
        }

        public static CTNumbering parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTNumbering) POIXMLTypeLoader.parse(xMLInputStream, CTNumbering.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNumbering.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNumbering.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTNumPicBullet> getNumPicBulletList();

    CTNumPicBullet[] getNumPicBulletArray();

    CTNumPicBullet getNumPicBulletArray(int i);

    int sizeOfNumPicBulletArray();

    void setNumPicBulletArray(CTNumPicBullet[] cTNumPicBulletArr);

    void setNumPicBulletArray(int i, CTNumPicBullet cTNumPicBullet);

    CTNumPicBullet insertNewNumPicBullet(int i);

    CTNumPicBullet addNewNumPicBullet();

    void removeNumPicBullet(int i);

    List<CTAbstractNum> getAbstractNumList();

    CTAbstractNum[] getAbstractNumArray();

    CTAbstractNum getAbstractNumArray(int i);

    int sizeOfAbstractNumArray();

    void setAbstractNumArray(CTAbstractNum[] cTAbstractNumArr);

    void setAbstractNumArray(int i, CTAbstractNum cTAbstractNum);

    CTAbstractNum insertNewAbstractNum(int i);

    CTAbstractNum addNewAbstractNum();

    void removeAbstractNum(int i);

    List<CTNum> getNumList();

    CTNum[] getNumArray();

    CTNum getNumArray(int i);

    int sizeOfNumArray();

    void setNumArray(CTNum[] cTNumArr);

    void setNumArray(int i, CTNum cTNum);

    CTNum insertNewNum(int i);

    CTNum addNewNum();

    void removeNum(int i);

    CTDecimalNumber getNumIdMacAtCleanup();

    boolean isSetNumIdMacAtCleanup();

    void setNumIdMacAtCleanup(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewNumIdMacAtCleanup();

    void unsetNumIdMacAtCleanup();
}
