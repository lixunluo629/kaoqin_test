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
import org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCD;
import org.openxmlformats.schemas.drawingml.x2006.main.CTAudioFile;
import org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile;
import org.openxmlformats.schemas.drawingml.x2006.main.CTQuickTimeFile;
import org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTApplicationNonVisualDrawingProps.class */
public interface CTApplicationNonVisualDrawingProps extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTApplicationNonVisualDrawingProps.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctapplicationnonvisualdrawingprops2fb6type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTApplicationNonVisualDrawingProps$Factory.class */
    public static final class Factory {
        public static CTApplicationNonVisualDrawingProps newInstance() {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.newInstance(CTApplicationNonVisualDrawingProps.type, null);
        }

        public static CTApplicationNonVisualDrawingProps newInstance(XmlOptions xmlOptions) {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.newInstance(CTApplicationNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTApplicationNonVisualDrawingProps parse(String str) throws XmlException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(str, CTApplicationNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTApplicationNonVisualDrawingProps parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(str, CTApplicationNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTApplicationNonVisualDrawingProps parse(File file) throws XmlException, IOException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(file, CTApplicationNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTApplicationNonVisualDrawingProps parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(file, CTApplicationNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTApplicationNonVisualDrawingProps parse(URL url) throws XmlException, IOException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(url, CTApplicationNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTApplicationNonVisualDrawingProps parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(url, CTApplicationNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTApplicationNonVisualDrawingProps parse(InputStream inputStream) throws XmlException, IOException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(inputStream, CTApplicationNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTApplicationNonVisualDrawingProps parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(inputStream, CTApplicationNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTApplicationNonVisualDrawingProps parse(Reader reader) throws XmlException, IOException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(reader, CTApplicationNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTApplicationNonVisualDrawingProps parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(reader, CTApplicationNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTApplicationNonVisualDrawingProps parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(xMLStreamReader, CTApplicationNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTApplicationNonVisualDrawingProps parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(xMLStreamReader, CTApplicationNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTApplicationNonVisualDrawingProps parse(Node node) throws XmlException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(node, CTApplicationNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTApplicationNonVisualDrawingProps parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(node, CTApplicationNonVisualDrawingProps.type, xmlOptions);
        }

        public static CTApplicationNonVisualDrawingProps parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(xMLInputStream, CTApplicationNonVisualDrawingProps.type, (XmlOptions) null);
        }

        public static CTApplicationNonVisualDrawingProps parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTApplicationNonVisualDrawingProps) POIXMLTypeLoader.parse(xMLInputStream, CTApplicationNonVisualDrawingProps.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTApplicationNonVisualDrawingProps.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTApplicationNonVisualDrawingProps.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTPlaceholder getPh();

    boolean isSetPh();

    void setPh(CTPlaceholder cTPlaceholder);

    CTPlaceholder addNewPh();

    void unsetPh();

    CTAudioCD getAudioCd();

    boolean isSetAudioCd();

    void setAudioCd(CTAudioCD cTAudioCD);

    CTAudioCD addNewAudioCd();

    void unsetAudioCd();

    CTEmbeddedWAVAudioFile getWavAudioFile();

    boolean isSetWavAudioFile();

    void setWavAudioFile(CTEmbeddedWAVAudioFile cTEmbeddedWAVAudioFile);

    CTEmbeddedWAVAudioFile addNewWavAudioFile();

    void unsetWavAudioFile();

    CTAudioFile getAudioFile();

    boolean isSetAudioFile();

    void setAudioFile(CTAudioFile cTAudioFile);

    CTAudioFile addNewAudioFile();

    void unsetAudioFile();

    CTVideoFile getVideoFile();

    boolean isSetVideoFile();

    void setVideoFile(CTVideoFile cTVideoFile);

    CTVideoFile addNewVideoFile();

    void unsetVideoFile();

    CTQuickTimeFile getQuickTimeFile();

    boolean isSetQuickTimeFile();

    void setQuickTimeFile(CTQuickTimeFile cTQuickTimeFile);

    CTQuickTimeFile addNewQuickTimeFile();

    void unsetQuickTimeFile();

    CTCustomerDataList getCustDataLst();

    boolean isSetCustDataLst();

    void setCustDataLst(CTCustomerDataList cTCustomerDataList);

    CTCustomerDataList addNewCustDataLst();

    void unsetCustDataLst();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    boolean getIsPhoto();

    XmlBoolean xgetIsPhoto();

    boolean isSetIsPhoto();

    void setIsPhoto(boolean z);

    void xsetIsPhoto(XmlBoolean xmlBoolean);

    void unsetIsPhoto();

    boolean getUserDrawn();

    XmlBoolean xgetUserDrawn();

    boolean isSetUserDrawn();

    void setUserDrawn(boolean z);

    void xsetUserDrawn(XmlBoolean xmlBoolean);

    void unsetUserDrawn();
}
