package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTRow.class */
public interface CTRow extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTRow.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctrowdd39type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTRow$Factory.class */
    public static final class Factory {
        public static CTRow newInstance() {
            return (CTRow) POIXMLTypeLoader.newInstance(CTRow.type, null);
        }

        public static CTRow newInstance(XmlOptions xmlOptions) {
            return (CTRow) POIXMLTypeLoader.newInstance(CTRow.type, xmlOptions);
        }

        public static CTRow parse(String str) throws XmlException {
            return (CTRow) POIXMLTypeLoader.parse(str, CTRow.type, (XmlOptions) null);
        }

        public static CTRow parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTRow) POIXMLTypeLoader.parse(str, CTRow.type, xmlOptions);
        }

        public static CTRow parse(File file) throws XmlException, IOException {
            return (CTRow) POIXMLTypeLoader.parse(file, CTRow.type, (XmlOptions) null);
        }

        public static CTRow parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRow) POIXMLTypeLoader.parse(file, CTRow.type, xmlOptions);
        }

        public static CTRow parse(URL url) throws XmlException, IOException {
            return (CTRow) POIXMLTypeLoader.parse(url, CTRow.type, (XmlOptions) null);
        }

        public static CTRow parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRow) POIXMLTypeLoader.parse(url, CTRow.type, xmlOptions);
        }

        public static CTRow parse(InputStream inputStream) throws XmlException, IOException {
            return (CTRow) POIXMLTypeLoader.parse(inputStream, CTRow.type, (XmlOptions) null);
        }

        public static CTRow parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRow) POIXMLTypeLoader.parse(inputStream, CTRow.type, xmlOptions);
        }

        public static CTRow parse(Reader reader) throws XmlException, IOException {
            return (CTRow) POIXMLTypeLoader.parse(reader, CTRow.type, (XmlOptions) null);
        }

        public static CTRow parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRow) POIXMLTypeLoader.parse(reader, CTRow.type, xmlOptions);
        }

        public static CTRow parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTRow) POIXMLTypeLoader.parse(xMLStreamReader, CTRow.type, (XmlOptions) null);
        }

        public static CTRow parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTRow) POIXMLTypeLoader.parse(xMLStreamReader, CTRow.type, xmlOptions);
        }

        public static CTRow parse(Node node) throws XmlException {
            return (CTRow) POIXMLTypeLoader.parse(node, CTRow.type, (XmlOptions) null);
        }

        public static CTRow parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTRow) POIXMLTypeLoader.parse(node, CTRow.type, xmlOptions);
        }

        public static CTRow parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTRow) POIXMLTypeLoader.parse(xMLInputStream, CTRow.type, (XmlOptions) null);
        }

        public static CTRow parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTRow) POIXMLTypeLoader.parse(xMLInputStream, CTRow.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRow.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRow.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTCell> getCList();

    CTCell[] getCArray();

    CTCell getCArray(int i);

    int sizeOfCArray();

    void setCArray(CTCell[] cTCellArr);

    void setCArray(int i, CTCell cTCell);

    CTCell insertNewC(int i);

    CTCell addNewC();

    void removeC(int i);

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    long getR();

    XmlUnsignedInt xgetR();

    boolean isSetR();

    void setR(long j);

    void xsetR(XmlUnsignedInt xmlUnsignedInt);

    void unsetR();

    List getSpans();

    STCellSpans xgetSpans();

    boolean isSetSpans();

    void setSpans(List list);

    void xsetSpans(STCellSpans sTCellSpans);

    void unsetSpans();

    long getS();

    XmlUnsignedInt xgetS();

    boolean isSetS();

    void setS(long j);

    void xsetS(XmlUnsignedInt xmlUnsignedInt);

    void unsetS();

    boolean getCustomFormat();

    XmlBoolean xgetCustomFormat();

    boolean isSetCustomFormat();

    void setCustomFormat(boolean z);

    void xsetCustomFormat(XmlBoolean xmlBoolean);

    void unsetCustomFormat();

    double getHt();

    XmlDouble xgetHt();

    boolean isSetHt();

    void setHt(double d);

    void xsetHt(XmlDouble xmlDouble);

    void unsetHt();

    boolean getHidden();

    XmlBoolean xgetHidden();

    boolean isSetHidden();

    void setHidden(boolean z);

    void xsetHidden(XmlBoolean xmlBoolean);

    void unsetHidden();

    boolean getCustomHeight();

    XmlBoolean xgetCustomHeight();

    boolean isSetCustomHeight();

    void setCustomHeight(boolean z);

    void xsetCustomHeight(XmlBoolean xmlBoolean);

    void unsetCustomHeight();

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

    boolean getThickTop();

    XmlBoolean xgetThickTop();

    boolean isSetThickTop();

    void setThickTop(boolean z);

    void xsetThickTop(XmlBoolean xmlBoolean);

    void unsetThickTop();

    boolean getThickBot();

    XmlBoolean xgetThickBot();

    boolean isSetThickBot();

    void setThickBot(boolean z);

    void xsetThickBot(XmlBoolean xmlBoolean);

    void unsetThickBot();

    boolean getPh();

    XmlBoolean xgetPh();

    boolean isSetPh();

    void setPh(boolean z);

    void xsetPh(XmlBoolean xmlBoolean);

    void unsetPh();
}
