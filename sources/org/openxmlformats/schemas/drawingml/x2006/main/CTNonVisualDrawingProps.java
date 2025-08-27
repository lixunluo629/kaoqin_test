package org.openxmlformats.schemas.drawingml.x2006.main;

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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTNonVisualDrawingProps.class */
public interface CTNonVisualDrawingProps extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTNonVisualDrawingProps.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctnonvisualdrawingprops8fb0type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTNonVisualDrawingProps$Factory.class */
    public static final class Factory {
        public static CTNonVisualDrawingProps newInstance() {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.newInstance(CTNonVisualDrawingProps.type, null);
        }

        public static CTNonVisualDrawingProps newInstance(XmlOptions xmlOptions) {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.newInstance(CTNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTNonVisualDrawingProps parse(String str) throws XmlException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(str, CTNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTNonVisualDrawingProps parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(str, CTNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTNonVisualDrawingProps parse(File file) throws XmlException, IOException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(file, CTNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTNonVisualDrawingProps parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(file, CTNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTNonVisualDrawingProps parse(URL url) throws XmlException, IOException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(url, CTNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTNonVisualDrawingProps parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(url, CTNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTNonVisualDrawingProps parse(InputStream inputStream) throws XmlException, IOException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(inputStream, CTNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTNonVisualDrawingProps parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(inputStream, CTNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTNonVisualDrawingProps parse(Reader reader) throws XmlException, IOException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(reader, CTNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTNonVisualDrawingProps parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(reader, CTNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTNonVisualDrawingProps parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(xMLStreamReader, CTNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTNonVisualDrawingProps parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(xMLStreamReader, CTNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTNonVisualDrawingProps parse(Node node) throws XmlException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(node, CTNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTNonVisualDrawingProps parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(node, CTNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTNonVisualDrawingProps parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(xMLInputStream, CTNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTNonVisualDrawingProps parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTNonVisualDrawingProps) POIXMLTypeLoader.parse(xMLInputStream, CTNonVisualDrawingProps.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNonVisualDrawingProps.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNonVisualDrawingProps.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTHyperlink getHlinkClick();

    boolean isSetHlinkClick();

    void setHlinkClick(CTHyperlink cTHyperlink);

    CTHyperlink addNewHlinkClick();

    void unsetHlinkClick();

    CTHyperlink getHlinkHover();

    boolean isSetHlinkHover();

    void setHlinkHover(CTHyperlink cTHyperlink);

    CTHyperlink addNewHlinkHover();

    void unsetHlinkHover();

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    long getId();

    STDrawingElementId xgetId();

    void setId(long j);

    void xsetId(STDrawingElementId sTDrawingElementId);

    String getName();

    XmlString xgetName();

    void setName(String str);

    void xsetName(XmlString xmlString);

    String getDescr();

    XmlString xgetDescr();

    boolean isSetDescr();

    void setDescr(String str);

    void xsetDescr(XmlString xmlString);

    void unsetDescr();

    boolean getHidden();

    XmlBoolean xgetHidden();

    boolean isSetHidden();

    void setHidden(boolean z);

    void xsetHidden(XmlBoolean xmlBoolean);

    void unsetHidden();
}
