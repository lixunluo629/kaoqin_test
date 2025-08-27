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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STItemType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTItem.class */
public interface CTItem extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTItem.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctitemc69ctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTItem$Factory.class */
    public static final class Factory {
        public static CTItem newInstance() {
            return (CTItem) POIXMLTypeLoader.newInstance(CTItem.type, null);
        }

        public static CTItem newInstance(XmlOptions xmlOptions) {
            return (CTItem) POIXMLTypeLoader.newInstance(CTItem.type, xmlOptions);
        }

        public static CTItem parse(String str) throws XmlException {
            return (CTItem) POIXMLTypeLoader.parse(str, CTItem.type, (XmlOptions) null);
        }

        public static CTItem parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTItem) POIXMLTypeLoader.parse(str, CTItem.type, xmlOptions);
        }

        public static CTItem parse(File file) throws XmlException, IOException {
            return (CTItem) POIXMLTypeLoader.parse(file, CTItem.type, (XmlOptions) null);
        }

        public static CTItem parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTItem) POIXMLTypeLoader.parse(file, CTItem.type, xmlOptions);
        }

        public static CTItem parse(URL url) throws XmlException, IOException {
            return (CTItem) POIXMLTypeLoader.parse(url, CTItem.type, (XmlOptions) null);
        }

        public static CTItem parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTItem) POIXMLTypeLoader.parse(url, CTItem.type, xmlOptions);
        }

        public static CTItem parse(InputStream inputStream) throws XmlException, IOException {
            return (CTItem) POIXMLTypeLoader.parse(inputStream, CTItem.type, (XmlOptions) null);
        }

        public static CTItem parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTItem) POIXMLTypeLoader.parse(inputStream, CTItem.type, xmlOptions);
        }

        public static CTItem parse(Reader reader) throws XmlException, IOException {
            return (CTItem) POIXMLTypeLoader.parse(reader, CTItem.type, (XmlOptions) null);
        }

        public static CTItem parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTItem) POIXMLTypeLoader.parse(reader, CTItem.type, xmlOptions);
        }

        public static CTItem parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTItem) POIXMLTypeLoader.parse(xMLStreamReader, CTItem.type, (XmlOptions) null);
        }

        public static CTItem parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTItem) POIXMLTypeLoader.parse(xMLStreamReader, CTItem.type, xmlOptions);
        }

        public static CTItem parse(Node node) throws XmlException {
            return (CTItem) POIXMLTypeLoader.parse(node, CTItem.type, (XmlOptions) null);
        }

        public static CTItem parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTItem) POIXMLTypeLoader.parse(node, CTItem.type, xmlOptions);
        }

        public static CTItem parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTItem) POIXMLTypeLoader.parse(xMLInputStream, CTItem.type, (XmlOptions) null);
        }

        public static CTItem parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTItem) POIXMLTypeLoader.parse(xMLInputStream, CTItem.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTItem.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTItem.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getN();

    STXstring xgetN();

    boolean isSetN();

    void setN(String str);

    void xsetN(STXstring sTXstring);

    void unsetN();

    STItemType.Enum getT();

    STItemType xgetT();

    boolean isSetT();

    void setT(STItemType.Enum r1);

    void xsetT(STItemType sTItemType);

    void unsetT();

    boolean getH();

    XmlBoolean xgetH();

    boolean isSetH();

    void setH(boolean z);

    void xsetH(XmlBoolean xmlBoolean);

    void unsetH();

    boolean getS();

    XmlBoolean xgetS();

    boolean isSetS();

    void setS(boolean z);

    void xsetS(XmlBoolean xmlBoolean);

    void unsetS();

    boolean getSd();

    XmlBoolean xgetSd();

    boolean isSetSd();

    void setSd(boolean z);

    void xsetSd(XmlBoolean xmlBoolean);

    void unsetSd();

    boolean getF();

    XmlBoolean xgetF();

    boolean isSetF();

    void setF(boolean z);

    void xsetF(XmlBoolean xmlBoolean);

    void unsetF();

    boolean getM();

    XmlBoolean xgetM();

    boolean isSetM();

    void setM(boolean z);

    void xsetM(XmlBoolean xmlBoolean);

    void unsetM();

    boolean getC();

    XmlBoolean xgetC();

    boolean isSetC();

    void setC(boolean z);

    void xsetC(XmlBoolean xmlBoolean);

    void unsetC();

    long getX();

    XmlUnsignedInt xgetX();

    boolean isSetX();

    void setX(long j);

    void xsetX(XmlUnsignedInt xmlUnsignedInt);

    void unsetX();

    boolean getD();

    XmlBoolean xgetD();

    boolean isSetD();

    void setD(boolean z);

    void xsetD(XmlBoolean xmlBoolean);

    void unsetD();

    boolean getE();

    XmlBoolean xgetE();

    boolean isSetE();

    void setE(boolean z);

    void xsetE(XmlBoolean xmlBoolean);

    void unsetE();
}
