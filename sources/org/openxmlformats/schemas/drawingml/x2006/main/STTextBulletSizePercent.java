package org.openxmlformats.schemas.drawingml.x2006.main;

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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextBulletSizePercent.class */
public interface STTextBulletSizePercent extends STPercentage {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STTextBulletSizePercent.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sttextbulletsizepercentb516type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextBulletSizePercent$Factory.class */
    public static final class Factory {
        public static STTextBulletSizePercent newValue(Object obj) {
            return (STTextBulletSizePercent) STTextBulletSizePercent.type.newValue(obj);
        }

        public static STTextBulletSizePercent newInstance() {
            return (STTextBulletSizePercent) POIXMLTypeLoader.newInstance(STTextBulletSizePercent.type, null);
        }

        public static STTextBulletSizePercent newInstance(XmlOptions xmlOptions) {
            return (STTextBulletSizePercent) POIXMLTypeLoader.newInstance(STTextBulletSizePercent.type, xmlOptions);
        }

        public static STTextBulletSizePercent parse(String str) throws XmlException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(str, STTextBulletSizePercent.type, (XmlOptions) null);
        }

        public static STTextBulletSizePercent parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(str, STTextBulletSizePercent.type, xmlOptions);
        }

        public static STTextBulletSizePercent parse(File file) throws XmlException, IOException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(file, STTextBulletSizePercent.type, (XmlOptions) null);
        }

        public static STTextBulletSizePercent parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(file, STTextBulletSizePercent.type, xmlOptions);
        }

        public static STTextBulletSizePercent parse(URL url) throws XmlException, IOException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(url, STTextBulletSizePercent.type, (XmlOptions) null);
        }

        public static STTextBulletSizePercent parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(url, STTextBulletSizePercent.type, xmlOptions);
        }

        public static STTextBulletSizePercent parse(InputStream inputStream) throws XmlException, IOException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(inputStream, STTextBulletSizePercent.type, (XmlOptions) null);
        }

        public static STTextBulletSizePercent parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(inputStream, STTextBulletSizePercent.type, xmlOptions);
        }

        public static STTextBulletSizePercent parse(Reader reader) throws XmlException, IOException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(reader, STTextBulletSizePercent.type, (XmlOptions) null);
        }

        public static STTextBulletSizePercent parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(reader, STTextBulletSizePercent.type, xmlOptions);
        }

        public static STTextBulletSizePercent parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(xMLStreamReader, STTextBulletSizePercent.type, (XmlOptions) null);
        }

        public static STTextBulletSizePercent parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(xMLStreamReader, STTextBulletSizePercent.type, xmlOptions);
        }

        public static STTextBulletSizePercent parse(Node node) throws XmlException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(node, STTextBulletSizePercent.type, (XmlOptions) null);
        }

        public static STTextBulletSizePercent parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(node, STTextBulletSizePercent.type, xmlOptions);
        }

        public static STTextBulletSizePercent parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(xMLInputStream, STTextBulletSizePercent.type, (XmlOptions) null);
        }

        public static STTextBulletSizePercent parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STTextBulletSizePercent) POIXMLTypeLoader.parse(xMLInputStream, STTextBulletSizePercent.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextBulletSizePercent.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextBulletSizePercent.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
