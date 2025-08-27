package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTPBdr.class */
public interface CTPBdr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPBdr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpbdre388type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTPBdr$Factory.class */
    public static final class Factory {
        public static CTPBdr newInstance() {
            return (CTPBdr) POIXMLTypeLoader.newInstance(CTPBdr.type, null);
        }

        public static CTPBdr newInstance(XmlOptions xmlOptions) {
            return (CTPBdr) POIXMLTypeLoader.newInstance(CTPBdr.type, xmlOptions);
        }

        public static CTPBdr parse(String str) throws XmlException {
            return (CTPBdr) POIXMLTypeLoader.parse(str, CTPBdr.type, (XmlOptions) null);
        }

        public static CTPBdr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPBdr) POIXMLTypeLoader.parse(str, CTPBdr.type, xmlOptions);
        }

        public static CTPBdr parse(File file) throws XmlException, IOException {
            return (CTPBdr) POIXMLTypeLoader.parse(file, CTPBdr.type, (XmlOptions) null);
        }

        public static CTPBdr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPBdr) POIXMLTypeLoader.parse(file, CTPBdr.type, xmlOptions);
        }

        public static CTPBdr parse(URL url) throws XmlException, IOException {
            return (CTPBdr) POIXMLTypeLoader.parse(url, CTPBdr.type, (XmlOptions) null);
        }

        public static CTPBdr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPBdr) POIXMLTypeLoader.parse(url, CTPBdr.type, xmlOptions);
        }

        public static CTPBdr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPBdr) POIXMLTypeLoader.parse(inputStream, CTPBdr.type, (XmlOptions) null);
        }

        public static CTPBdr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPBdr) POIXMLTypeLoader.parse(inputStream, CTPBdr.type, xmlOptions);
        }

        public static CTPBdr parse(Reader reader) throws XmlException, IOException {
            return (CTPBdr) POIXMLTypeLoader.parse(reader, CTPBdr.type, (XmlOptions) null);
        }

        public static CTPBdr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPBdr) POIXMLTypeLoader.parse(reader, CTPBdr.type, xmlOptions);
        }

        public static CTPBdr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPBdr) POIXMLTypeLoader.parse(xMLStreamReader, CTPBdr.type, (XmlOptions) null);
        }

        public static CTPBdr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPBdr) POIXMLTypeLoader.parse(xMLStreamReader, CTPBdr.type, xmlOptions);
        }

        public static CTPBdr parse(Node node) throws XmlException {
            return (CTPBdr) POIXMLTypeLoader.parse(node, CTPBdr.type, (XmlOptions) null);
        }

        public static CTPBdr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPBdr) POIXMLTypeLoader.parse(node, CTPBdr.type, xmlOptions);
        }

        public static CTPBdr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPBdr) POIXMLTypeLoader.parse(xMLInputStream, CTPBdr.type, (XmlOptions) null);
        }

        public static CTPBdr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPBdr) POIXMLTypeLoader.parse(xMLInputStream, CTPBdr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPBdr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPBdr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTBorder getTop();

    boolean isSetTop();

    void setTop(CTBorder cTBorder);

    CTBorder addNewTop();

    void unsetTop();

    CTBorder getLeft();

    boolean isSetLeft();

    void setLeft(CTBorder cTBorder);

    CTBorder addNewLeft();

    void unsetLeft();

    CTBorder getBottom();

    boolean isSetBottom();

    void setBottom(CTBorder cTBorder);

    CTBorder addNewBottom();

    void unsetBottom();

    CTBorder getRight();

    boolean isSetRight();

    void setRight(CTBorder cTBorder);

    CTBorder addNewRight();

    void unsetRight();

    CTBorder getBetween();

    boolean isSetBetween();

    void setBetween(CTBorder cTBorder);

    CTBorder addNewBetween();

    void unsetBetween();

    CTBorder getBar();

    boolean isSetBar();

    void setBar(CTBorder cTBorder);

    CTBorder addNewBar();

    void unsetBar();
}
