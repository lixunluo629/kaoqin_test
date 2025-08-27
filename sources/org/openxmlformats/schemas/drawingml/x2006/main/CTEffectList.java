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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTEffectList.class */
public interface CTEffectList extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTEffectList.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cteffectlist6featype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTEffectList$Factory.class */
    public static final class Factory {
        public static CTEffectList newInstance() {
            return (CTEffectList) POIXMLTypeLoader.newInstance(CTEffectList.type, null);
        }

        public static CTEffectList newInstance(XmlOptions xmlOptions) {
            return (CTEffectList) POIXMLTypeLoader.newInstance(CTEffectList.type, xmlOptions);
        }

        public static CTEffectList parse(String str) throws XmlException {
            return (CTEffectList) POIXMLTypeLoader.parse(str, CTEffectList.type, (XmlOptions) null);
        }

        public static CTEffectList parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTEffectList) POIXMLTypeLoader.parse(str, CTEffectList.type, xmlOptions);
        }

        public static CTEffectList parse(File file) throws XmlException, IOException {
            return (CTEffectList) POIXMLTypeLoader.parse(file, CTEffectList.type, (XmlOptions) null);
        }

        public static CTEffectList parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEffectList) POIXMLTypeLoader.parse(file, CTEffectList.type, xmlOptions);
        }

        public static CTEffectList parse(URL url) throws XmlException, IOException {
            return (CTEffectList) POIXMLTypeLoader.parse(url, CTEffectList.type, (XmlOptions) null);
        }

        public static CTEffectList parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEffectList) POIXMLTypeLoader.parse(url, CTEffectList.type, xmlOptions);
        }

        public static CTEffectList parse(InputStream inputStream) throws XmlException, IOException {
            return (CTEffectList) POIXMLTypeLoader.parse(inputStream, CTEffectList.type, (XmlOptions) null);
        }

        public static CTEffectList parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEffectList) POIXMLTypeLoader.parse(inputStream, CTEffectList.type, xmlOptions);
        }

        public static CTEffectList parse(Reader reader) throws XmlException, IOException {
            return (CTEffectList) POIXMLTypeLoader.parse(reader, CTEffectList.type, (XmlOptions) null);
        }

        public static CTEffectList parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEffectList) POIXMLTypeLoader.parse(reader, CTEffectList.type, xmlOptions);
        }

        public static CTEffectList parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTEffectList) POIXMLTypeLoader.parse(xMLStreamReader, CTEffectList.type, (XmlOptions) null);
        }

        public static CTEffectList parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTEffectList) POIXMLTypeLoader.parse(xMLStreamReader, CTEffectList.type, xmlOptions);
        }

        public static CTEffectList parse(Node node) throws XmlException {
            return (CTEffectList) POIXMLTypeLoader.parse(node, CTEffectList.type, (XmlOptions) null);
        }

        public static CTEffectList parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTEffectList) POIXMLTypeLoader.parse(node, CTEffectList.type, xmlOptions);
        }

        public static CTEffectList parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTEffectList) POIXMLTypeLoader.parse(xMLInputStream, CTEffectList.type, (XmlOptions) null);
        }

        public static CTEffectList parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTEffectList) POIXMLTypeLoader.parse(xMLInputStream, CTEffectList.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTEffectList.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTEffectList.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTBlurEffect getBlur();

    boolean isSetBlur();

    void setBlur(CTBlurEffect cTBlurEffect);

    CTBlurEffect addNewBlur();

    void unsetBlur();

    CTFillOverlayEffect getFillOverlay();

    boolean isSetFillOverlay();

    void setFillOverlay(CTFillOverlayEffect cTFillOverlayEffect);

    CTFillOverlayEffect addNewFillOverlay();

    void unsetFillOverlay();

    CTGlowEffect getGlow();

    boolean isSetGlow();

    void setGlow(CTGlowEffect cTGlowEffect);

    CTGlowEffect addNewGlow();

    void unsetGlow();

    CTInnerShadowEffect getInnerShdw();

    boolean isSetInnerShdw();

    void setInnerShdw(CTInnerShadowEffect cTInnerShadowEffect);

    CTInnerShadowEffect addNewInnerShdw();

    void unsetInnerShdw();

    CTOuterShadowEffect getOuterShdw();

    boolean isSetOuterShdw();

    void setOuterShdw(CTOuterShadowEffect cTOuterShadowEffect);

    CTOuterShadowEffect addNewOuterShdw();

    void unsetOuterShdw();

    CTPresetShadowEffect getPrstShdw();

    boolean isSetPrstShdw();

    void setPrstShdw(CTPresetShadowEffect cTPresetShadowEffect);

    CTPresetShadowEffect addNewPrstShdw();

    void unsetPrstShdw();

    CTReflectionEffect getReflection();

    boolean isSetReflection();

    void setReflection(CTReflectionEffect cTReflectionEffect);

    CTReflectionEffect addNewReflection();

    void unsetReflection();

    CTSoftEdgesEffect getSoftEdge();

    boolean isSetSoftEdge();

    void setSoftEdge(CTSoftEdgesEffect cTSoftEdgesEffect);

    CTSoftEdgesEffect addNewSoftEdge();

    void unsetSoftEdge();
}
