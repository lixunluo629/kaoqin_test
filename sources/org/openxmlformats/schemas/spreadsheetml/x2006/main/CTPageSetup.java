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
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellComments;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STOrientation;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPageOrder;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPageSetup.class */
public interface CTPageSetup extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPageSetup.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpagesetup534dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPageSetup$Factory.class */
    public static final class Factory {
        public static CTPageSetup newInstance() {
            return (CTPageSetup) POIXMLTypeLoader.newInstance(CTPageSetup.type, null);
        }

        public static CTPageSetup newInstance(XmlOptions xmlOptions) {
            return (CTPageSetup) POIXMLTypeLoader.newInstance(CTPageSetup.type, xmlOptions);
        }

        public static CTPageSetup parse(String str) throws XmlException {
            return (CTPageSetup) POIXMLTypeLoader.parse(str, CTPageSetup.type, (XmlOptions) null);
        }

        public static CTPageSetup parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPageSetup) POIXMLTypeLoader.parse(str, CTPageSetup.type, xmlOptions);
        }

        public static CTPageSetup parse(File file) throws XmlException, IOException {
            return (CTPageSetup) POIXMLTypeLoader.parse(file, CTPageSetup.type, (XmlOptions) null);
        }

        public static CTPageSetup parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageSetup) POIXMLTypeLoader.parse(file, CTPageSetup.type, xmlOptions);
        }

        public static CTPageSetup parse(URL url) throws XmlException, IOException {
            return (CTPageSetup) POIXMLTypeLoader.parse(url, CTPageSetup.type, (XmlOptions) null);
        }

        public static CTPageSetup parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageSetup) POIXMLTypeLoader.parse(url, CTPageSetup.type, xmlOptions);
        }

        public static CTPageSetup parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPageSetup) POIXMLTypeLoader.parse(inputStream, CTPageSetup.type, (XmlOptions) null);
        }

        public static CTPageSetup parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageSetup) POIXMLTypeLoader.parse(inputStream, CTPageSetup.type, xmlOptions);
        }

        public static CTPageSetup parse(Reader reader) throws XmlException, IOException {
            return (CTPageSetup) POIXMLTypeLoader.parse(reader, CTPageSetup.type, (XmlOptions) null);
        }

        public static CTPageSetup parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageSetup) POIXMLTypeLoader.parse(reader, CTPageSetup.type, xmlOptions);
        }

        public static CTPageSetup parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPageSetup) POIXMLTypeLoader.parse(xMLStreamReader, CTPageSetup.type, (XmlOptions) null);
        }

        public static CTPageSetup parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPageSetup) POIXMLTypeLoader.parse(xMLStreamReader, CTPageSetup.type, xmlOptions);
        }

        public static CTPageSetup parse(Node node) throws XmlException {
            return (CTPageSetup) POIXMLTypeLoader.parse(node, CTPageSetup.type, (XmlOptions) null);
        }

        public static CTPageSetup parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPageSetup) POIXMLTypeLoader.parse(node, CTPageSetup.type, xmlOptions);
        }

        public static CTPageSetup parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPageSetup) POIXMLTypeLoader.parse(xMLInputStream, CTPageSetup.type, (XmlOptions) null);
        }

        public static CTPageSetup parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPageSetup) POIXMLTypeLoader.parse(xMLInputStream, CTPageSetup.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPageSetup.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPageSetup.type, xmlOptions);
        }

        private Factory() {
        }
    }

    long getPaperSize();

    XmlUnsignedInt xgetPaperSize();

    boolean isSetPaperSize();

    void setPaperSize(long j);

    void xsetPaperSize(XmlUnsignedInt xmlUnsignedInt);

    void unsetPaperSize();

    long getScale();

    XmlUnsignedInt xgetScale();

    boolean isSetScale();

    void setScale(long j);

    void xsetScale(XmlUnsignedInt xmlUnsignedInt);

    void unsetScale();

    long getFirstPageNumber();

    XmlUnsignedInt xgetFirstPageNumber();

    boolean isSetFirstPageNumber();

    void setFirstPageNumber(long j);

    void xsetFirstPageNumber(XmlUnsignedInt xmlUnsignedInt);

    void unsetFirstPageNumber();

    long getFitToWidth();

    XmlUnsignedInt xgetFitToWidth();

    boolean isSetFitToWidth();

    void setFitToWidth(long j);

    void xsetFitToWidth(XmlUnsignedInt xmlUnsignedInt);

    void unsetFitToWidth();

    long getFitToHeight();

    XmlUnsignedInt xgetFitToHeight();

    boolean isSetFitToHeight();

    void setFitToHeight(long j);

    void xsetFitToHeight(XmlUnsignedInt xmlUnsignedInt);

    void unsetFitToHeight();

    STPageOrder.Enum getPageOrder();

    STPageOrder xgetPageOrder();

    boolean isSetPageOrder();

    void setPageOrder(STPageOrder.Enum r1);

    void xsetPageOrder(STPageOrder sTPageOrder);

    void unsetPageOrder();

    STOrientation.Enum getOrientation();

    STOrientation xgetOrientation();

    boolean isSetOrientation();

    void setOrientation(STOrientation.Enum r1);

    void xsetOrientation(STOrientation sTOrientation);

    void unsetOrientation();

    boolean getUsePrinterDefaults();

    XmlBoolean xgetUsePrinterDefaults();

    boolean isSetUsePrinterDefaults();

    void setUsePrinterDefaults(boolean z);

    void xsetUsePrinterDefaults(XmlBoolean xmlBoolean);

    void unsetUsePrinterDefaults();

    boolean getBlackAndWhite();

    XmlBoolean xgetBlackAndWhite();

    boolean isSetBlackAndWhite();

    void setBlackAndWhite(boolean z);

    void xsetBlackAndWhite(XmlBoolean xmlBoolean);

    void unsetBlackAndWhite();

    boolean getDraft();

    XmlBoolean xgetDraft();

    boolean isSetDraft();

    void setDraft(boolean z);

    void xsetDraft(XmlBoolean xmlBoolean);

    void unsetDraft();

    STCellComments.Enum getCellComments();

    STCellComments xgetCellComments();

    boolean isSetCellComments();

    void setCellComments(STCellComments.Enum r1);

    void xsetCellComments(STCellComments sTCellComments);

    void unsetCellComments();

    boolean getUseFirstPageNumber();

    XmlBoolean xgetUseFirstPageNumber();

    boolean isSetUseFirstPageNumber();

    void setUseFirstPageNumber(boolean z);

    void xsetUseFirstPageNumber(XmlBoolean xmlBoolean);

    void unsetUseFirstPageNumber();

    STPrintError$Enum getErrors();

    STPrintError xgetErrors();

    boolean isSetErrors();

    void setErrors(STPrintError$Enum sTPrintError$Enum);

    void xsetErrors(STPrintError sTPrintError);

    void unsetErrors();

    long getHorizontalDpi();

    XmlUnsignedInt xgetHorizontalDpi();

    boolean isSetHorizontalDpi();

    void setHorizontalDpi(long j);

    void xsetHorizontalDpi(XmlUnsignedInt xmlUnsignedInt);

    void unsetHorizontalDpi();

    long getVerticalDpi();

    XmlUnsignedInt xgetVerticalDpi();

    boolean isSetVerticalDpi();

    void setVerticalDpi(long j);

    void xsetVerticalDpi(XmlUnsignedInt xmlUnsignedInt);

    void unsetVerticalDpi();

    long getCopies();

    XmlUnsignedInt xgetCopies();

    boolean isSetCopies();

    void setCopies(long j);

    void xsetCopies(XmlUnsignedInt xmlUnsignedInt);

    void unsetCopies();

    String getId();

    STRelationshipId xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(STRelationshipId sTRelationshipId);

    void unsetId();
}
