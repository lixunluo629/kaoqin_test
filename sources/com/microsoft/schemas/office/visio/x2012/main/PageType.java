package com.microsoft.schemas.office.visio.x2012.main;

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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/PageType.class */
public interface PageType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(PageType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("pagetype2fcatype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/PageType$Factory.class */
    public static final class Factory {
        public static PageType newInstance() {
            return (PageType) POIXMLTypeLoader.newInstance(PageType.type, null);
        }

        public static PageType newInstance(XmlOptions xmlOptions) {
            return (PageType) POIXMLTypeLoader.newInstance(PageType.type, xmlOptions);
        }

        public static PageType parse(String str) throws XmlException {
            return (PageType) POIXMLTypeLoader.parse(str, PageType.type, (XmlOptions) null);
        }

        public static PageType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (PageType) POIXMLTypeLoader.parse(str, PageType.type, xmlOptions);
        }

        public static PageType parse(File file) throws XmlException, IOException {
            return (PageType) POIXMLTypeLoader.parse(file, PageType.type, (XmlOptions) null);
        }

        public static PageType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PageType) POIXMLTypeLoader.parse(file, PageType.type, xmlOptions);
        }

        public static PageType parse(URL url) throws XmlException, IOException {
            return (PageType) POIXMLTypeLoader.parse(url, PageType.type, (XmlOptions) null);
        }

        public static PageType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PageType) POIXMLTypeLoader.parse(url, PageType.type, xmlOptions);
        }

        public static PageType parse(InputStream inputStream) throws XmlException, IOException {
            return (PageType) POIXMLTypeLoader.parse(inputStream, PageType.type, (XmlOptions) null);
        }

        public static PageType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PageType) POIXMLTypeLoader.parse(inputStream, PageType.type, xmlOptions);
        }

        public static PageType parse(Reader reader) throws XmlException, IOException {
            return (PageType) POIXMLTypeLoader.parse(reader, PageType.type, (XmlOptions) null);
        }

        public static PageType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PageType) POIXMLTypeLoader.parse(reader, PageType.type, xmlOptions);
        }

        public static PageType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (PageType) POIXMLTypeLoader.parse(xMLStreamReader, PageType.type, (XmlOptions) null);
        }

        public static PageType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (PageType) POIXMLTypeLoader.parse(xMLStreamReader, PageType.type, xmlOptions);
        }

        public static PageType parse(Node node) throws XmlException {
            return (PageType) POIXMLTypeLoader.parse(node, PageType.type, (XmlOptions) null);
        }

        public static PageType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (PageType) POIXMLTypeLoader.parse(node, PageType.type, xmlOptions);
        }

        public static PageType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (PageType) POIXMLTypeLoader.parse(xMLInputStream, PageType.type, (XmlOptions) null);
        }

        public static PageType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (PageType) POIXMLTypeLoader.parse(xMLInputStream, PageType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, PageType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, PageType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    PageSheetType getPageSheet();

    boolean isSetPageSheet();

    void setPageSheet(PageSheetType pageSheetType);

    PageSheetType addNewPageSheet();

    void unsetPageSheet();

    RelType getRel();

    void setRel(RelType relType);

    RelType addNewRel();

    long getID();

    XmlUnsignedInt xgetID();

    void setID(long j);

    void xsetID(XmlUnsignedInt xmlUnsignedInt);

    String getName();

    XmlString xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(XmlString xmlString);

    void unsetName();

    String getNameU();

    XmlString xgetNameU();

    boolean isSetNameU();

    void setNameU(String str);

    void xsetNameU(XmlString xmlString);

    void unsetNameU();

    boolean getIsCustomName();

    XmlBoolean xgetIsCustomName();

    boolean isSetIsCustomName();

    void setIsCustomName(boolean z);

    void xsetIsCustomName(XmlBoolean xmlBoolean);

    void unsetIsCustomName();

    boolean getIsCustomNameU();

    XmlBoolean xgetIsCustomNameU();

    boolean isSetIsCustomNameU();

    void setIsCustomNameU(boolean z);

    void xsetIsCustomNameU(XmlBoolean xmlBoolean);

    void unsetIsCustomNameU();

    boolean getBackground();

    XmlBoolean xgetBackground();

    boolean isSetBackground();

    void setBackground(boolean z);

    void xsetBackground(XmlBoolean xmlBoolean);

    void unsetBackground();

    long getBackPage();

    XmlUnsignedInt xgetBackPage();

    boolean isSetBackPage();

    void setBackPage(long j);

    void xsetBackPage(XmlUnsignedInt xmlUnsignedInt);

    void unsetBackPage();

    double getViewScale();

    XmlDouble xgetViewScale();

    boolean isSetViewScale();

    void setViewScale(double d);

    void xsetViewScale(XmlDouble xmlDouble);

    void unsetViewScale();

    double getViewCenterX();

    XmlDouble xgetViewCenterX();

    boolean isSetViewCenterX();

    void setViewCenterX(double d);

    void xsetViewCenterX(XmlDouble xmlDouble);

    void unsetViewCenterX();

    double getViewCenterY();

    XmlDouble xgetViewCenterY();

    boolean isSetViewCenterY();

    void setViewCenterY(double d);

    void xsetViewCenterY(XmlDouble xmlDouble);

    void unsetViewCenterY();

    long getReviewerID();

    XmlUnsignedInt xgetReviewerID();

    boolean isSetReviewerID();

    void setReviewerID(long j);

    void xsetReviewerID(XmlUnsignedInt xmlUnsignedInt);

    void unsetReviewerID();

    long getAssociatedPage();

    XmlUnsignedInt xgetAssociatedPage();

    boolean isSetAssociatedPage();

    void setAssociatedPage(long j);

    void xsetAssociatedPage(XmlUnsignedInt xmlUnsignedInt);

    void unsetAssociatedPage();
}
