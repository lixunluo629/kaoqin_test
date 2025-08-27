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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride;
import org.openxmlformats.schemas.presentationml.x2006.main.STSlideLayoutType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTSlideLayout.class */
public interface CTSlideLayout extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSlideLayout.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctslidelayouteb34type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTSlideLayout$Factory.class */
    public static final class Factory {
        public static CTSlideLayout newInstance() {
            return (CTSlideLayout) POIXMLTypeLoader.newInstance(CTSlideLayout.type, null);
        }

        public static CTSlideLayout newInstance(XmlOptions xmlOptions) {
            return (CTSlideLayout) POIXMLTypeLoader.newInstance(CTSlideLayout.type, xmlOptions);
        }

        public static CTSlideLayout parse(String str) throws XmlException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(str, CTSlideLayout.type, (XmlOptions) null);
        }

        public static CTSlideLayout parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(str, CTSlideLayout.type, xmlOptions);
        }

        public static CTSlideLayout parse(File file) throws XmlException, IOException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(file, CTSlideLayout.type, (XmlOptions) null);
        }

        public static CTSlideLayout parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(file, CTSlideLayout.type, xmlOptions);
        }

        public static CTSlideLayout parse(URL url) throws XmlException, IOException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(url, CTSlideLayout.type, (XmlOptions) null);
        }

        public static CTSlideLayout parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(url, CTSlideLayout.type, xmlOptions);
        }

        public static CTSlideLayout parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(inputStream, CTSlideLayout.type, (XmlOptions) null);
        }

        public static CTSlideLayout parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(inputStream, CTSlideLayout.type, xmlOptions);
        }

        public static CTSlideLayout parse(Reader reader) throws XmlException, IOException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(reader, CTSlideLayout.type, (XmlOptions) null);
        }

        public static CTSlideLayout parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(reader, CTSlideLayout.type, xmlOptions);
        }

        public static CTSlideLayout parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(xMLStreamReader, CTSlideLayout.type, (XmlOptions) null);
        }

        public static CTSlideLayout parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(xMLStreamReader, CTSlideLayout.type, xmlOptions);
        }

        public static CTSlideLayout parse(Node node) throws XmlException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(node, CTSlideLayout.type, (XmlOptions) null);
        }

        public static CTSlideLayout parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(node, CTSlideLayout.type, xmlOptions);
        }

        public static CTSlideLayout parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(xMLInputStream, CTSlideLayout.type, (XmlOptions) null);
        }

        public static CTSlideLayout parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSlideLayout) POIXMLTypeLoader.parse(xMLInputStream, CTSlideLayout.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSlideLayout.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSlideLayout.type, xmlOptions);
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

    String getMatchingName();

    XmlString xgetMatchingName();

    boolean isSetMatchingName();

    void setMatchingName(String str);

    void xsetMatchingName(XmlString xmlString);

    void unsetMatchingName();

    STSlideLayoutType.Enum getType();

    STSlideLayoutType xgetType();

    boolean isSetType();

    void setType(STSlideLayoutType.Enum r1);

    void xsetType(STSlideLayoutType sTSlideLayoutType);

    void unsetType();

    boolean getPreserve();

    XmlBoolean xgetPreserve();

    boolean isSetPreserve();

    void setPreserve(boolean z);

    void xsetPreserve(XmlBoolean xmlBoolean);

    void unsetPreserve();

    boolean getUserDrawn();

    XmlBoolean xgetUserDrawn();

    boolean isSetUserDrawn();

    void setUserDrawn(boolean z);

    void xsetUserDrawn(XmlBoolean xmlBoolean);

    void unsetUserDrawn();
}
