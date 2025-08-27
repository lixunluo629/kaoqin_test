package org.openxmlformats.schemas.presentationml.x2006.main;

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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTNotesSlide.class */
public interface CTNotesSlide extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTNotesSlide.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctnotesslideab75type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTNotesSlide$Factory.class */
    public static final class Factory {
        public static CTNotesSlide newInstance() {
            return (CTNotesSlide) POIXMLTypeLoader.newInstance(CTNotesSlide.type, null);
        }

        public static CTNotesSlide newInstance(XmlOptions xmlOptions) {
            return (CTNotesSlide) POIXMLTypeLoader.newInstance(CTNotesSlide.type, xmlOptions);
        }

        public static CTNotesSlide parse(String str) throws XmlException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(str, CTNotesSlide.type, (XmlOptions) null);
        }

        public static CTNotesSlide parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(str, CTNotesSlide.type, xmlOptions);
        }

        public static CTNotesSlide parse(File file) throws XmlException, IOException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(file, CTNotesSlide.type, (XmlOptions) null);
        }

        public static CTNotesSlide parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(file, CTNotesSlide.type, xmlOptions);
        }

        public static CTNotesSlide parse(URL url) throws XmlException, IOException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(url, CTNotesSlide.type, (XmlOptions) null);
        }

        public static CTNotesSlide parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(url, CTNotesSlide.type, xmlOptions);
        }

        public static CTNotesSlide parse(InputStream inputStream) throws XmlException, IOException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(inputStream, CTNotesSlide.type, (XmlOptions) null);
        }

        public static CTNotesSlide parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(inputStream, CTNotesSlide.type, xmlOptions);
        }

        public static CTNotesSlide parse(Reader reader) throws XmlException, IOException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(reader, CTNotesSlide.type, (XmlOptions) null);
        }

        public static CTNotesSlide parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(reader, CTNotesSlide.type, xmlOptions);
        }

        public static CTNotesSlide parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(xMLStreamReader, CTNotesSlide.type, (XmlOptions) null);
        }

        public static CTNotesSlide parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(xMLStreamReader, CTNotesSlide.type, xmlOptions);
        }

        public static CTNotesSlide parse(Node node) throws XmlException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(node, CTNotesSlide.type, (XmlOptions) null);
        }

        public static CTNotesSlide parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(node, CTNotesSlide.type, xmlOptions);
        }

        public static CTNotesSlide parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(xMLInputStream, CTNotesSlide.type, (XmlOptions) null);
        }

        public static CTNotesSlide parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTNotesSlide) POIXMLTypeLoader.parse(xMLInputStream, CTNotesSlide.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNotesSlide.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNotesSlide.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTCommonSlideData getCSld();

    void setCSld(CTCommonSlideData cTCommonSlideData);

    CTCommonSlideData addNewCSld();

    CTColorMappingOverride getClrMapOvr();

    boolean isSetClrMapOvr();

    void setClrMapOvr(CTColorMappingOverride cTColorMappingOverride);

    CTColorMappingOverride addNewClrMapOvr();

    void unsetClrMapOvr();

    CTExtensionListModify getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionListModify cTExtensionListModify);

    CTExtensionListModify addNewExtLst();

    void unsetExtLst();

    boolean getShowMasterSp();

    XmlBoolean xgetShowMasterSp();

    boolean isSetShowMasterSp();

    void setShowMasterSp(boolean z);

    void xsetShowMasterSp(XmlBoolean xmlBoolean);

    void unsetShowMasterSp();

    boolean getShowMasterPhAnim();

    XmlBoolean xgetShowMasterPhAnim();

    boolean isSetShowMasterPhAnim();

    void setShowMasterPhAnim(boolean z);

    void xsetShowMasterPhAnim(XmlBoolean xmlBoolean);

    void unsetShowMasterPhAnim();
}
