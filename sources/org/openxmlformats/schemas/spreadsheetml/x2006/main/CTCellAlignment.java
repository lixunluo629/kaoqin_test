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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCellAlignment.class */
public interface CTCellAlignment extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCellAlignment.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcellalignmentb580type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCellAlignment$Factory.class */
    public static final class Factory {
        public static CTCellAlignment newInstance() {
            return (CTCellAlignment) POIXMLTypeLoader.newInstance(CTCellAlignment.type, null);
        }

        public static CTCellAlignment newInstance(XmlOptions xmlOptions) {
            return (CTCellAlignment) POIXMLTypeLoader.newInstance(CTCellAlignment.type, xmlOptions);
        }

        public static CTCellAlignment parse(String str) throws XmlException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(str, CTCellAlignment.type, (XmlOptions) null);
        }

        public static CTCellAlignment parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(str, CTCellAlignment.type, xmlOptions);
        }

        public static CTCellAlignment parse(File file) throws XmlException, IOException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(file, CTCellAlignment.type, (XmlOptions) null);
        }

        public static CTCellAlignment parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(file, CTCellAlignment.type, xmlOptions);
        }

        public static CTCellAlignment parse(URL url) throws XmlException, IOException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(url, CTCellAlignment.type, (XmlOptions) null);
        }

        public static CTCellAlignment parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(url, CTCellAlignment.type, xmlOptions);
        }

        public static CTCellAlignment parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(inputStream, CTCellAlignment.type, (XmlOptions) null);
        }

        public static CTCellAlignment parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(inputStream, CTCellAlignment.type, xmlOptions);
        }

        public static CTCellAlignment parse(Reader reader) throws XmlException, IOException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(reader, CTCellAlignment.type, (XmlOptions) null);
        }

        public static CTCellAlignment parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(reader, CTCellAlignment.type, xmlOptions);
        }

        public static CTCellAlignment parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(xMLStreamReader, CTCellAlignment.type, (XmlOptions) null);
        }

        public static CTCellAlignment parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(xMLStreamReader, CTCellAlignment.type, xmlOptions);
        }

        public static CTCellAlignment parse(Node node) throws XmlException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(node, CTCellAlignment.type, (XmlOptions) null);
        }

        public static CTCellAlignment parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(node, CTCellAlignment.type, xmlOptions);
        }

        public static CTCellAlignment parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(xMLInputStream, CTCellAlignment.type, (XmlOptions) null);
        }

        public static CTCellAlignment parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCellAlignment) POIXMLTypeLoader.parse(xMLInputStream, CTCellAlignment.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCellAlignment.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCellAlignment.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STHorizontalAlignment.Enum getHorizontal();

    STHorizontalAlignment xgetHorizontal();

    boolean isSetHorizontal();

    void setHorizontal(STHorizontalAlignment.Enum r1);

    void xsetHorizontal(STHorizontalAlignment sTHorizontalAlignment);

    void unsetHorizontal();

    STVerticalAlignment.Enum getVertical();

    STVerticalAlignment xgetVertical();

    boolean isSetVertical();

    void setVertical(STVerticalAlignment.Enum r1);

    void xsetVertical(STVerticalAlignment sTVerticalAlignment);

    void unsetVertical();

    long getTextRotation();

    XmlUnsignedInt xgetTextRotation();

    boolean isSetTextRotation();

    void setTextRotation(long j);

    void xsetTextRotation(XmlUnsignedInt xmlUnsignedInt);

    void unsetTextRotation();

    boolean getWrapText();

    XmlBoolean xgetWrapText();

    boolean isSetWrapText();

    void setWrapText(boolean z);

    void xsetWrapText(XmlBoolean xmlBoolean);

    void unsetWrapText();

    long getIndent();

    XmlUnsignedInt xgetIndent();

    boolean isSetIndent();

    void setIndent(long j);

    void xsetIndent(XmlUnsignedInt xmlUnsignedInt);

    void unsetIndent();

    int getRelativeIndent();

    XmlInt xgetRelativeIndent();

    boolean isSetRelativeIndent();

    void setRelativeIndent(int i);

    void xsetRelativeIndent(XmlInt xmlInt);

    void unsetRelativeIndent();

    boolean getJustifyLastLine();

    XmlBoolean xgetJustifyLastLine();

    boolean isSetJustifyLastLine();

    void setJustifyLastLine(boolean z);

    void xsetJustifyLastLine(XmlBoolean xmlBoolean);

    void unsetJustifyLastLine();

    boolean getShrinkToFit();

    XmlBoolean xgetShrinkToFit();

    boolean isSetShrinkToFit();

    void setShrinkToFit(boolean z);

    void xsetShrinkToFit(XmlBoolean xmlBoolean);

    void unsetShrinkToFit();

    long getReadingOrder();

    XmlUnsignedInt xgetReadingOrder();

    boolean isSetReadingOrder();

    void setReadingOrder(long j);

    void xsetReadingOrder(XmlUnsignedInt xmlUnsignedInt);

    void unsetReadingOrder();
}
