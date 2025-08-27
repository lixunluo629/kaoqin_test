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
import org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTSlideMaster.class */
public interface CTSlideMaster extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSlideMaster.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctslidemasterd8fctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTSlideMaster$Factory.class */
    public static final class Factory {
        public static CTSlideMaster newInstance() {
            return (CTSlideMaster) POIXMLTypeLoader.newInstance(CTSlideMaster.type, null);
        }

        public static CTSlideMaster newInstance(XmlOptions xmlOptions) {
            return (CTSlideMaster) POIXMLTypeLoader.newInstance(CTSlideMaster.type, xmlOptions);
        }

        public static CTSlideMaster parse(String str) throws XmlException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(str, CTSlideMaster.type, (XmlOptions) null);
        }

        public static CTSlideMaster parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(str, CTSlideMaster.type, xmlOptions);
        }

        public static CTSlideMaster parse(File file) throws XmlException, IOException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(file, CTSlideMaster.type, (XmlOptions) null);
        }

        public static CTSlideMaster parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(file, CTSlideMaster.type, xmlOptions);
        }

        public static CTSlideMaster parse(URL url) throws XmlException, IOException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(url, CTSlideMaster.type, (XmlOptions) null);
        }

        public static CTSlideMaster parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(url, CTSlideMaster.type, xmlOptions);
        }

        public static CTSlideMaster parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(inputStream, CTSlideMaster.type, (XmlOptions) null);
        }

        public static CTSlideMaster parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(inputStream, CTSlideMaster.type, xmlOptions);
        }

        public static CTSlideMaster parse(Reader reader) throws XmlException, IOException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(reader, CTSlideMaster.type, (XmlOptions) null);
        }

        public static CTSlideMaster parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(reader, CTSlideMaster.type, xmlOptions);
        }

        public static CTSlideMaster parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(xMLStreamReader, CTSlideMaster.type, (XmlOptions) null);
        }

        public static CTSlideMaster parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(xMLStreamReader, CTSlideMaster.type, xmlOptions);
        }

        public static CTSlideMaster parse(Node node) throws XmlException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(node, CTSlideMaster.type, (XmlOptions) null);
        }

        public static CTSlideMaster parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(node, CTSlideMaster.type, xmlOptions);
        }

        public static CTSlideMaster parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(xMLInputStream, CTSlideMaster.type, (XmlOptions) null);
        }

        public static CTSlideMaster parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSlideMaster) POIXMLTypeLoader.parse(xMLInputStream, CTSlideMaster.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSlideMaster.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSlideMaster.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTCommonSlideData getCSld();

    void setCSld(CTCommonSlideData cTCommonSlideData);

    CTCommonSlideData addNewCSld();

    CTColorMapping getClrMap();

    void setClrMap(CTColorMapping cTColorMapping);

    CTColorMapping addNewClrMap();

    CTSlideLayoutIdList getSldLayoutIdLst();

    boolean isSetSldLayoutIdLst();

    void setSldLayoutIdLst(CTSlideLayoutIdList cTSlideLayoutIdList);

    CTSlideLayoutIdList addNewSldLayoutIdLst();

    void unsetSldLayoutIdLst();

    CTSlideTransition getTransition();

    boolean isSetTransition();

    void setTransition(CTSlideTransition cTSlideTransition);

    CTSlideTransition addNewTransition();

    void unsetTransition();

    CTSlideTiming getTiming();

    boolean isSetTiming();

    void setTiming(CTSlideTiming cTSlideTiming);

    CTSlideTiming addNewTiming();

    void unsetTiming();

    CTHeaderFooter getHf();

    boolean isSetHf();

    void setHf(CTHeaderFooter cTHeaderFooter);

    CTHeaderFooter addNewHf();

    void unsetHf();

    CTSlideMasterTextStyles getTxStyles();

    boolean isSetTxStyles();

    void setTxStyles(CTSlideMasterTextStyles cTSlideMasterTextStyles);

    CTSlideMasterTextStyles addNewTxStyles();

    void unsetTxStyles();

    CTExtensionListModify getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionListModify cTExtensionListModify);

    CTExtensionListModify addNewExtLst();

    void unsetExtLst();

    boolean getPreserve();

    XmlBoolean xgetPreserve();

    boolean isSetPreserve();

    void setPreserve(boolean z);

    void xsetPreserve(XmlBoolean xmlBoolean);

    void unsetPreserve();
}
