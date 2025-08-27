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
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCell.class */
public interface CTCell extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCell.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcell842btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCell$Factory.class */
    public static final class Factory {
        public static CTCell newInstance() {
            return (CTCell) POIXMLTypeLoader.newInstance(CTCell.type, null);
        }

        public static CTCell newInstance(XmlOptions xmlOptions) {
            return (CTCell) POIXMLTypeLoader.newInstance(CTCell.type, xmlOptions);
        }

        public static CTCell parse(String str) throws XmlException {
            return (CTCell) POIXMLTypeLoader.parse(str, CTCell.type, (XmlOptions) null);
        }

        public static CTCell parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCell) POIXMLTypeLoader.parse(str, CTCell.type, xmlOptions);
        }

        public static CTCell parse(File file) throws XmlException, IOException {
            return (CTCell) POIXMLTypeLoader.parse(file, CTCell.type, (XmlOptions) null);
        }

        public static CTCell parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCell) POIXMLTypeLoader.parse(file, CTCell.type, xmlOptions);
        }

        public static CTCell parse(URL url) throws XmlException, IOException {
            return (CTCell) POIXMLTypeLoader.parse(url, CTCell.type, (XmlOptions) null);
        }

        public static CTCell parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCell) POIXMLTypeLoader.parse(url, CTCell.type, xmlOptions);
        }

        public static CTCell parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCell) POIXMLTypeLoader.parse(inputStream, CTCell.type, (XmlOptions) null);
        }

        public static CTCell parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCell) POIXMLTypeLoader.parse(inputStream, CTCell.type, xmlOptions);
        }

        public static CTCell parse(Reader reader) throws XmlException, IOException {
            return (CTCell) POIXMLTypeLoader.parse(reader, CTCell.type, (XmlOptions) null);
        }

        public static CTCell parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCell) POIXMLTypeLoader.parse(reader, CTCell.type, xmlOptions);
        }

        public static CTCell parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCell) POIXMLTypeLoader.parse(xMLStreamReader, CTCell.type, (XmlOptions) null);
        }

        public static CTCell parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCell) POIXMLTypeLoader.parse(xMLStreamReader, CTCell.type, xmlOptions);
        }

        public static CTCell parse(Node node) throws XmlException {
            return (CTCell) POIXMLTypeLoader.parse(node, CTCell.type, (XmlOptions) null);
        }

        public static CTCell parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCell) POIXMLTypeLoader.parse(node, CTCell.type, xmlOptions);
        }

        public static CTCell parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCell) POIXMLTypeLoader.parse(xMLInputStream, CTCell.type, (XmlOptions) null);
        }

        public static CTCell parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCell) POIXMLTypeLoader.parse(xMLInputStream, CTCell.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCell.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCell.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTCellFormula getF();

    boolean isSetF();

    void setF(CTCellFormula cTCellFormula);

    CTCellFormula addNewF();

    void unsetF();

    String getV();

    STXstring xgetV();

    boolean isSetV();

    void setV(String str);

    void xsetV(STXstring sTXstring);

    void unsetV();

    CTRst getIs();

    boolean isSetIs();

    void setIs(CTRst cTRst);

    CTRst addNewIs();

    void unsetIs();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    String getR();

    STCellRef xgetR();

    boolean isSetR();

    void setR(String str);

    void xsetR(STCellRef sTCellRef);

    void unsetR();

    long getS();

    XmlUnsignedInt xgetS();

    boolean isSetS();

    void setS(long j);

    void xsetS(XmlUnsignedInt xmlUnsignedInt);

    void unsetS();

    STCellType.Enum getT();

    STCellType xgetT();

    boolean isSetT();

    void setT(STCellType.Enum r1);

    void xsetT(STCellType sTCellType);

    void unsetT();

    long getCm();

    XmlUnsignedInt xgetCm();

    boolean isSetCm();

    void setCm(long j);

    void xsetCm(XmlUnsignedInt xmlUnsignedInt);

    void unsetCm();

    long getVm();

    XmlUnsignedInt xgetVm();

    boolean isSetVm();

    void setVm(long j);

    void xsetVm(XmlUnsignedInt xmlUnsignedInt);

    void unsetVm();

    boolean getPh();

    XmlBoolean xgetPh();

    boolean isSetPh();

    void setPh(boolean z);

    void xsetPh(XmlBoolean xmlBoolean);

    void unsetPh();
}
