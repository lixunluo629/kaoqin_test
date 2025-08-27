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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellFormulaType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCellFormula.class */
public interface CTCellFormula extends STFormula {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCellFormula.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcellformula3583type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCellFormula$Factory.class */
    public static final class Factory {
        public static CTCellFormula newInstance() {
            return (CTCellFormula) POIXMLTypeLoader.newInstance(CTCellFormula.type, null);
        }

        public static CTCellFormula newInstance(XmlOptions xmlOptions) {
            return (CTCellFormula) POIXMLTypeLoader.newInstance(CTCellFormula.type, xmlOptions);
        }

        public static CTCellFormula parse(String str) throws XmlException {
            return (CTCellFormula) POIXMLTypeLoader.parse(str, CTCellFormula.type, (XmlOptions) null);
        }

        public static CTCellFormula parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCellFormula) POIXMLTypeLoader.parse(str, CTCellFormula.type, xmlOptions);
        }

        public static CTCellFormula parse(File file) throws XmlException, IOException {
            return (CTCellFormula) POIXMLTypeLoader.parse(file, CTCellFormula.type, (XmlOptions) null);
        }

        public static CTCellFormula parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellFormula) POIXMLTypeLoader.parse(file, CTCellFormula.type, xmlOptions);
        }

        public static CTCellFormula parse(URL url) throws XmlException, IOException {
            return (CTCellFormula) POIXMLTypeLoader.parse(url, CTCellFormula.type, (XmlOptions) null);
        }

        public static CTCellFormula parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellFormula) POIXMLTypeLoader.parse(url, CTCellFormula.type, xmlOptions);
        }

        public static CTCellFormula parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCellFormula) POIXMLTypeLoader.parse(inputStream, CTCellFormula.type, (XmlOptions) null);
        }

        public static CTCellFormula parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellFormula) POIXMLTypeLoader.parse(inputStream, CTCellFormula.type, xmlOptions);
        }

        public static CTCellFormula parse(Reader reader) throws XmlException, IOException {
            return (CTCellFormula) POIXMLTypeLoader.parse(reader, CTCellFormula.type, (XmlOptions) null);
        }

        public static CTCellFormula parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellFormula) POIXMLTypeLoader.parse(reader, CTCellFormula.type, xmlOptions);
        }

        public static CTCellFormula parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCellFormula) POIXMLTypeLoader.parse(xMLStreamReader, CTCellFormula.type, (XmlOptions) null);
        }

        public static CTCellFormula parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCellFormula) POIXMLTypeLoader.parse(xMLStreamReader, CTCellFormula.type, xmlOptions);
        }

        public static CTCellFormula parse(Node node) throws XmlException {
            return (CTCellFormula) POIXMLTypeLoader.parse(node, CTCellFormula.type, (XmlOptions) null);
        }

        public static CTCellFormula parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCellFormula) POIXMLTypeLoader.parse(node, CTCellFormula.type, xmlOptions);
        }

        public static CTCellFormula parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCellFormula) POIXMLTypeLoader.parse(xMLInputStream, CTCellFormula.type, (XmlOptions) null);
        }

        public static CTCellFormula parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCellFormula) POIXMLTypeLoader.parse(xMLInputStream, CTCellFormula.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCellFormula.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCellFormula.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STCellFormulaType.Enum getT();

    STCellFormulaType xgetT();

    boolean isSetT();

    void setT(STCellFormulaType.Enum r1);

    void xsetT(STCellFormulaType sTCellFormulaType);

    void unsetT();

    boolean getAca();

    XmlBoolean xgetAca();

    boolean isSetAca();

    void setAca(boolean z);

    void xsetAca(XmlBoolean xmlBoolean);

    void unsetAca();

    String getRef();

    STRef xgetRef();

    boolean isSetRef();

    void setRef(String str);

    void xsetRef(STRef sTRef);

    void unsetRef();

    boolean getDt2D();

    XmlBoolean xgetDt2D();

    boolean isSetDt2D();

    void setDt2D(boolean z);

    void xsetDt2D(XmlBoolean xmlBoolean);

    void unsetDt2D();

    boolean getDtr();

    XmlBoolean xgetDtr();

    boolean isSetDtr();

    void setDtr(boolean z);

    void xsetDtr(XmlBoolean xmlBoolean);

    void unsetDtr();

    boolean getDel1();

    XmlBoolean xgetDel1();

    boolean isSetDel1();

    void setDel1(boolean z);

    void xsetDel1(XmlBoolean xmlBoolean);

    void unsetDel1();

    boolean getDel2();

    XmlBoolean xgetDel2();

    boolean isSetDel2();

    void setDel2(boolean z);

    void xsetDel2(XmlBoolean xmlBoolean);

    void unsetDel2();

    String getR1();

    STCellRef xgetR1();

    boolean isSetR1();

    void setR1(String str);

    void xsetR1(STCellRef sTCellRef);

    void unsetR1();

    String getR2();

    STCellRef xgetR2();

    boolean isSetR2();

    void setR2(String str);

    void xsetR2(STCellRef sTCellRef);

    void unsetR2();

    boolean getCa();

    XmlBoolean xgetCa();

    boolean isSetCa();

    void setCa(boolean z);

    void xsetCa(XmlBoolean xmlBoolean);

    void unsetCa();

    long getSi();

    XmlUnsignedInt xgetSi();

    boolean isSetSi();

    void setSi(long j);

    void xsetSi(XmlUnsignedInt xmlUnsignedInt);

    void unsetSi();

    boolean getBx();

    XmlBoolean xgetBx();

    boolean isSetBx();

    void setBx(boolean z);

    void xsetBx(XmlBoolean xmlBoolean);

    void unsetBx();
}
