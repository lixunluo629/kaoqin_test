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
import org.apache.xmlbeans.XmlByte;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextFont.class */
public interface CTTextFont extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTextFont.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttextfont92b7type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTextFont$Factory.class */
    public static final class Factory {
        public static CTTextFont newInstance() {
            return (CTTextFont) POIXMLTypeLoader.newInstance(CTTextFont.type, null);
        }

        public static CTTextFont newInstance(XmlOptions xmlOptions) {
            return (CTTextFont) POIXMLTypeLoader.newInstance(CTTextFont.type, xmlOptions);
        }

        public static CTTextFont parse(String str) throws XmlException {
            return (CTTextFont) POIXMLTypeLoader.parse(str, CTTextFont.type, (XmlOptions) null);
        }

        public static CTTextFont parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTextFont) POIXMLTypeLoader.parse(str, CTTextFont.type, xmlOptions);
        }

        public static CTTextFont parse(File file) throws XmlException, IOException {
            return (CTTextFont) POIXMLTypeLoader.parse(file, CTTextFont.type, (XmlOptions) null);
        }

        public static CTTextFont parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextFont) POIXMLTypeLoader.parse(file, CTTextFont.type, xmlOptions);
        }

        public static CTTextFont parse(URL url) throws XmlException, IOException {
            return (CTTextFont) POIXMLTypeLoader.parse(url, CTTextFont.type, (XmlOptions) null);
        }

        public static CTTextFont parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextFont) POIXMLTypeLoader.parse(url, CTTextFont.type, xmlOptions);
        }

        public static CTTextFont parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTextFont) POIXMLTypeLoader.parse(inputStream, CTTextFont.type, (XmlOptions) null);
        }

        public static CTTextFont parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextFont) POIXMLTypeLoader.parse(inputStream, CTTextFont.type, xmlOptions);
        }

        public static CTTextFont parse(Reader reader) throws XmlException, IOException {
            return (CTTextFont) POIXMLTypeLoader.parse(reader, CTTextFont.type, (XmlOptions) null);
        }

        public static CTTextFont parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTextFont) POIXMLTypeLoader.parse(reader, CTTextFont.type, xmlOptions);
        }

        public static CTTextFont parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTextFont) POIXMLTypeLoader.parse(xMLStreamReader, CTTextFont.type, (XmlOptions) null);
        }

        public static CTTextFont parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTextFont) POIXMLTypeLoader.parse(xMLStreamReader, CTTextFont.type, xmlOptions);
        }

        public static CTTextFont parse(Node node) throws XmlException {
            return (CTTextFont) POIXMLTypeLoader.parse(node, CTTextFont.type, (XmlOptions) null);
        }

        public static CTTextFont parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTextFont) POIXMLTypeLoader.parse(node, CTTextFont.type, xmlOptions);
        }

        public static CTTextFont parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTextFont) POIXMLTypeLoader.parse(xMLInputStream, CTTextFont.type, (XmlOptions) null);
        }

        public static CTTextFont parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTextFont) POIXMLTypeLoader.parse(xMLInputStream, CTTextFont.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextFont.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTextFont.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getTypeface();

    STTextTypeface xgetTypeface();

    boolean isSetTypeface();

    void setTypeface(String str);

    void xsetTypeface(STTextTypeface sTTextTypeface);

    void unsetTypeface();

    byte[] getPanose();

    STPanose xgetPanose();

    boolean isSetPanose();

    void setPanose(byte[] bArr);

    void xsetPanose(STPanose sTPanose);

    void unsetPanose();

    byte getPitchFamily();

    XmlByte xgetPitchFamily();

    boolean isSetPitchFamily();

    void setPitchFamily(byte b);

    void xsetPitchFamily(XmlByte xmlByte);

    void unsetPitchFamily();

    byte getCharset();

    XmlByte xgetCharset();

    boolean isSetCharset();

    void setCharset(byte b);

    void xsetCharset(XmlByte xmlByte);

    void unsetCharset();
}
