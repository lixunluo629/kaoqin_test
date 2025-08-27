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
import org.apache.xmlbeans.XmlUnsignedByte;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCol.class */
public interface CTCol extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCol.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcola95ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCol$Factory.class */
    public static final class Factory {
        public static CTCol newInstance() {
            return (CTCol) POIXMLTypeLoader.newInstance(CTCol.type, null);
        }

        public static CTCol newInstance(XmlOptions xmlOptions) {
            return (CTCol) POIXMLTypeLoader.newInstance(CTCol.type, xmlOptions);
        }

        public static CTCol parse(String str) throws XmlException {
            return (CTCol) POIXMLTypeLoader.parse(str, CTCol.type, (XmlOptions) null);
        }

        public static CTCol parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCol) POIXMLTypeLoader.parse(str, CTCol.type, xmlOptions);
        }

        public static CTCol parse(File file) throws XmlException, IOException {
            return (CTCol) POIXMLTypeLoader.parse(file, CTCol.type, (XmlOptions) null);
        }

        public static CTCol parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCol) POIXMLTypeLoader.parse(file, CTCol.type, xmlOptions);
        }

        public static CTCol parse(URL url) throws XmlException, IOException {
            return (CTCol) POIXMLTypeLoader.parse(url, CTCol.type, (XmlOptions) null);
        }

        public static CTCol parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCol) POIXMLTypeLoader.parse(url, CTCol.type, xmlOptions);
        }

        public static CTCol parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCol) POIXMLTypeLoader.parse(inputStream, CTCol.type, (XmlOptions) null);
        }

        public static CTCol parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCol) POIXMLTypeLoader.parse(inputStream, CTCol.type, xmlOptions);
        }

        public static CTCol parse(Reader reader) throws XmlException, IOException {
            return (CTCol) POIXMLTypeLoader.parse(reader, CTCol.type, (XmlOptions) null);
        }

        public static CTCol parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCol) POIXMLTypeLoader.parse(reader, CTCol.type, xmlOptions);
        }

        public static CTCol parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCol) POIXMLTypeLoader.parse(xMLStreamReader, CTCol.type, (XmlOptions) null);
        }

        public static CTCol parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCol) POIXMLTypeLoader.parse(xMLStreamReader, CTCol.type, xmlOptions);
        }

        public static CTCol parse(Node node) throws XmlException {
            return (CTCol) POIXMLTypeLoader.parse(node, CTCol.type, (XmlOptions) null);
        }

        public static CTCol parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCol) POIXMLTypeLoader.parse(node, CTCol.type, xmlOptions);
        }

        public static CTCol parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCol) POIXMLTypeLoader.parse(xMLInputStream, CTCol.type, (XmlOptions) null);
        }

        public static CTCol parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCol) POIXMLTypeLoader.parse(xMLInputStream, CTCol.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCol.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCol.type, xmlOptions);
        }

        private Factory() {
        }
    }

    long getMin();

    XmlUnsignedInt xgetMin();

    void setMin(long j);

    void xsetMin(XmlUnsignedInt xmlUnsignedInt);

    long getMax();

    XmlUnsignedInt xgetMax();

    void setMax(long j);

    void xsetMax(XmlUnsignedInt xmlUnsignedInt);

    double getWidth();

    XmlDouble xgetWidth();

    boolean isSetWidth();

    void setWidth(double d);

    void xsetWidth(XmlDouble xmlDouble);

    void unsetWidth();

    long getStyle();

    XmlUnsignedInt xgetStyle();

    boolean isSetStyle();

    void setStyle(long j);

    void xsetStyle(XmlUnsignedInt xmlUnsignedInt);

    void unsetStyle();

    boolean getHidden();

    XmlBoolean xgetHidden();

    boolean isSetHidden();

    void setHidden(boolean z);

    void xsetHidden(XmlBoolean xmlBoolean);

    void unsetHidden();

    boolean getBestFit();

    XmlBoolean xgetBestFit();

    boolean isSetBestFit();

    void setBestFit(boolean z);

    void xsetBestFit(XmlBoolean xmlBoolean);

    void unsetBestFit();

    boolean getCustomWidth();

    XmlBoolean xgetCustomWidth();

    boolean isSetCustomWidth();

    void setCustomWidth(boolean z);

    void xsetCustomWidth(XmlBoolean xmlBoolean);

    void unsetCustomWidth();

    boolean getPhonetic();

    XmlBoolean xgetPhonetic();

    boolean isSetPhonetic();

    void setPhonetic(boolean z);

    void xsetPhonetic(XmlBoolean xmlBoolean);

    void unsetPhonetic();

    short getOutlineLevel();

    XmlUnsignedByte xgetOutlineLevel();

    boolean isSetOutlineLevel();

    void setOutlineLevel(short s);

    void xsetOutlineLevel(XmlUnsignedByte xmlUnsignedByte);

    void unsetOutlineLevel();

    boolean getCollapsed();

    XmlBoolean xgetCollapsed();

    boolean isSetCollapsed();

    void setCollapsed(boolean z);

    void xsetCollapsed(XmlBoolean xmlBoolean);

    void unsetCollapsed();
}
