package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTColor.class */
public interface CTColor extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTColor.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcolord2c2type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTColor$Factory.class */
    public static final class Factory {
        public static CTColor newInstance() {
            return (CTColor) POIXMLTypeLoader.newInstance(CTColor.type, null);
        }

        public static CTColor newInstance(XmlOptions xmlOptions) {
            return (CTColor) POIXMLTypeLoader.newInstance(CTColor.type, xmlOptions);
        }

        public static CTColor parse(String str) throws XmlException {
            return (CTColor) POIXMLTypeLoader.parse(str, CTColor.type, (XmlOptions) null);
        }

        public static CTColor parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTColor) POIXMLTypeLoader.parse(str, CTColor.type, xmlOptions);
        }

        public static CTColor parse(File file) throws XmlException, IOException {
            return (CTColor) POIXMLTypeLoader.parse(file, CTColor.type, (XmlOptions) null);
        }

        public static CTColor parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColor) POIXMLTypeLoader.parse(file, CTColor.type, xmlOptions);
        }

        public static CTColor parse(URL url) throws XmlException, IOException {
            return (CTColor) POIXMLTypeLoader.parse(url, CTColor.type, (XmlOptions) null);
        }

        public static CTColor parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColor) POIXMLTypeLoader.parse(url, CTColor.type, xmlOptions);
        }

        public static CTColor parse(InputStream inputStream) throws XmlException, IOException {
            return (CTColor) POIXMLTypeLoader.parse(inputStream, CTColor.type, (XmlOptions) null);
        }

        public static CTColor parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColor) POIXMLTypeLoader.parse(inputStream, CTColor.type, xmlOptions);
        }

        public static CTColor parse(Reader reader) throws XmlException, IOException {
            return (CTColor) POIXMLTypeLoader.parse(reader, CTColor.type, (XmlOptions) null);
        }

        public static CTColor parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTColor) POIXMLTypeLoader.parse(reader, CTColor.type, xmlOptions);
        }

        public static CTColor parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTColor) POIXMLTypeLoader.parse(xMLStreamReader, CTColor.type, (XmlOptions) null);
        }

        public static CTColor parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTColor) POIXMLTypeLoader.parse(xMLStreamReader, CTColor.type, xmlOptions);
        }

        public static CTColor parse(Node node) throws XmlException {
            return (CTColor) POIXMLTypeLoader.parse(node, CTColor.type, (XmlOptions) null);
        }

        public static CTColor parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTColor) POIXMLTypeLoader.parse(node, CTColor.type, xmlOptions);
        }

        public static CTColor parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTColor) POIXMLTypeLoader.parse(xMLInputStream, CTColor.type, (XmlOptions) null);
        }

        public static CTColor parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTColor) POIXMLTypeLoader.parse(xMLInputStream, CTColor.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTColor.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTColor.type, xmlOptions);
        }

        private Factory() {
        }
    }

    boolean getAuto();

    XmlBoolean xgetAuto();

    boolean isSetAuto();

    void setAuto(boolean z);

    void xsetAuto(XmlBoolean xmlBoolean);

    void unsetAuto();

    long getIndexed();

    XmlUnsignedInt xgetIndexed();

    boolean isSetIndexed();

    void setIndexed(long j);

    void xsetIndexed(XmlUnsignedInt xmlUnsignedInt);

    void unsetIndexed();

    byte[] getRgb();

    STUnsignedIntHex xgetRgb();

    boolean isSetRgb();

    void setRgb(byte[] bArr);

    void xsetRgb(STUnsignedIntHex sTUnsignedIntHex);

    void unsetRgb();

    long getTheme();

    XmlUnsignedInt xgetTheme();

    boolean isSetTheme();

    void setTheme(long j);

    void xsetTheme(XmlUnsignedInt xmlUnsignedInt);

    void unsetTheme();

    double getTint();

    XmlDouble xgetTint();

    boolean isSetTint();

    void setTint(double d);

    void xsetTint(XmlDouble xmlDouble);

    void unsetTint();
}
