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
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCalcCell.class */
public interface CTCalcCell extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCalcCell.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcalccellb960type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCalcCell$Factory.class */
    public static final class Factory {
        public static CTCalcCell newInstance() {
            return (CTCalcCell) POIXMLTypeLoader.newInstance(CTCalcCell.type, null);
        }

        public static CTCalcCell newInstance(XmlOptions xmlOptions) {
            return (CTCalcCell) POIXMLTypeLoader.newInstance(CTCalcCell.type, xmlOptions);
        }

        public static CTCalcCell parse(String str) throws XmlException {
            return (CTCalcCell) POIXMLTypeLoader.parse(str, CTCalcCell.type, (XmlOptions) null);
        }

        public static CTCalcCell parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCalcCell) POIXMLTypeLoader.parse(str, CTCalcCell.type, xmlOptions);
        }

        public static CTCalcCell parse(File file) throws XmlException, IOException {
            return (CTCalcCell) POIXMLTypeLoader.parse(file, CTCalcCell.type, (XmlOptions) null);
        }

        public static CTCalcCell parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCalcCell) POIXMLTypeLoader.parse(file, CTCalcCell.type, xmlOptions);
        }

        public static CTCalcCell parse(URL url) throws XmlException, IOException {
            return (CTCalcCell) POIXMLTypeLoader.parse(url, CTCalcCell.type, (XmlOptions) null);
        }

        public static CTCalcCell parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCalcCell) POIXMLTypeLoader.parse(url, CTCalcCell.type, xmlOptions);
        }

        public static CTCalcCell parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCalcCell) POIXMLTypeLoader.parse(inputStream, CTCalcCell.type, (XmlOptions) null);
        }

        public static CTCalcCell parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCalcCell) POIXMLTypeLoader.parse(inputStream, CTCalcCell.type, xmlOptions);
        }

        public static CTCalcCell parse(Reader reader) throws XmlException, IOException {
            return (CTCalcCell) POIXMLTypeLoader.parse(reader, CTCalcCell.type, (XmlOptions) null);
        }

        public static CTCalcCell parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCalcCell) POIXMLTypeLoader.parse(reader, CTCalcCell.type, xmlOptions);
        }

        public static CTCalcCell parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCalcCell) POIXMLTypeLoader.parse(xMLStreamReader, CTCalcCell.type, (XmlOptions) null);
        }

        public static CTCalcCell parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCalcCell) POIXMLTypeLoader.parse(xMLStreamReader, CTCalcCell.type, xmlOptions);
        }

        public static CTCalcCell parse(Node node) throws XmlException {
            return (CTCalcCell) POIXMLTypeLoader.parse(node, CTCalcCell.type, (XmlOptions) null);
        }

        public static CTCalcCell parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCalcCell) POIXMLTypeLoader.parse(node, CTCalcCell.type, xmlOptions);
        }

        public static CTCalcCell parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCalcCell) POIXMLTypeLoader.parse(xMLInputStream, CTCalcCell.type, (XmlOptions) null);
        }

        public static CTCalcCell parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCalcCell) POIXMLTypeLoader.parse(xMLInputStream, CTCalcCell.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCalcCell.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCalcCell.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getR();

    STCellRef xgetR();

    void setR(String str);

    void xsetR(STCellRef sTCellRef);

    int getI();

    XmlInt xgetI();

    boolean isSetI();

    void setI(int i);

    void xsetI(XmlInt xmlInt);

    void unsetI();

    boolean getS();

    XmlBoolean xgetS();

    boolean isSetS();

    void setS(boolean z);

    void xsetS(XmlBoolean xmlBoolean);

    void unsetS();

    boolean getL();

    XmlBoolean xgetL();

    boolean isSetL();

    void setL(boolean z);

    void xsetL(XmlBoolean xmlBoolean);

    void unsetL();

    boolean getT();

    XmlBoolean xgetT();

    boolean isSetT();

    void setT(boolean z);

    void xsetT(XmlBoolean xmlBoolean);

    void unsetT();

    boolean getA();

    XmlBoolean xgetA();

    boolean isSetA();

    void setA(boolean z);

    void xsetA(XmlBoolean xmlBoolean);

    void unsetA();
}
