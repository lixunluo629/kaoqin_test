package com.microsoft.schemas.vml;

import com.microsoft.schemas.office.office.STConnectType;
import com.microsoft.schemas.vml.STTrueFalse;
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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTPath.class */
public interface CTPath extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPath.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpath5963type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTPath$Factory.class */
    public static final class Factory {
        public static CTPath newInstance() {
            return (CTPath) POIXMLTypeLoader.newInstance(CTPath.type, null);
        }

        public static CTPath newInstance(XmlOptions xmlOptions) {
            return (CTPath) POIXMLTypeLoader.newInstance(CTPath.type, xmlOptions);
        }

        public static CTPath parse(String str) throws XmlException {
            return (CTPath) POIXMLTypeLoader.parse(str, CTPath.type, (XmlOptions) null);
        }

        public static CTPath parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPath) POIXMLTypeLoader.parse(str, CTPath.type, xmlOptions);
        }

        public static CTPath parse(File file) throws XmlException, IOException {
            return (CTPath) POIXMLTypeLoader.parse(file, CTPath.type, (XmlOptions) null);
        }

        public static CTPath parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPath) POIXMLTypeLoader.parse(file, CTPath.type, xmlOptions);
        }

        public static CTPath parse(URL url) throws XmlException, IOException {
            return (CTPath) POIXMLTypeLoader.parse(url, CTPath.type, (XmlOptions) null);
        }

        public static CTPath parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPath) POIXMLTypeLoader.parse(url, CTPath.type, xmlOptions);
        }

        public static CTPath parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPath) POIXMLTypeLoader.parse(inputStream, CTPath.type, (XmlOptions) null);
        }

        public static CTPath parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPath) POIXMLTypeLoader.parse(inputStream, CTPath.type, xmlOptions);
        }

        public static CTPath parse(Reader reader) throws XmlException, IOException {
            return (CTPath) POIXMLTypeLoader.parse(reader, CTPath.type, (XmlOptions) null);
        }

        public static CTPath parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPath) POIXMLTypeLoader.parse(reader, CTPath.type, xmlOptions);
        }

        public static CTPath parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPath) POIXMLTypeLoader.parse(xMLStreamReader, CTPath.type, (XmlOptions) null);
        }

        public static CTPath parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPath) POIXMLTypeLoader.parse(xMLStreamReader, CTPath.type, xmlOptions);
        }

        public static CTPath parse(Node node) throws XmlException {
            return (CTPath) POIXMLTypeLoader.parse(node, CTPath.type, (XmlOptions) null);
        }

        public static CTPath parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPath) POIXMLTypeLoader.parse(node, CTPath.type, xmlOptions);
        }

        public static CTPath parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPath) POIXMLTypeLoader.parse(xMLInputStream, CTPath.type, (XmlOptions) null);
        }

        public static CTPath parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPath) POIXMLTypeLoader.parse(xMLInputStream, CTPath.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPath.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPath.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getId();

    XmlString xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlString xmlString);

    void unsetId();

    String getV();

    XmlString xgetV();

    boolean isSetV();

    void setV(String str);

    void xsetV(XmlString xmlString);

    void unsetV();

    String getLimo();

    XmlString xgetLimo();

    boolean isSetLimo();

    void setLimo(String str);

    void xsetLimo(XmlString xmlString);

    void unsetLimo();

    String getTextboxrect();

    XmlString xgetTextboxrect();

    boolean isSetTextboxrect();

    void setTextboxrect(String str);

    void xsetTextboxrect(XmlString xmlString);

    void unsetTextboxrect();

    STTrueFalse.Enum getFillok();

    STTrueFalse xgetFillok();

    boolean isSetFillok();

    void setFillok(STTrueFalse.Enum r1);

    void xsetFillok(STTrueFalse sTTrueFalse);

    void unsetFillok();

    STTrueFalse.Enum getStrokeok();

    STTrueFalse xgetStrokeok();

    boolean isSetStrokeok();

    void setStrokeok(STTrueFalse.Enum r1);

    void xsetStrokeok(STTrueFalse sTTrueFalse);

    void unsetStrokeok();

    STTrueFalse.Enum getShadowok();

    STTrueFalse xgetShadowok();

    boolean isSetShadowok();

    void setShadowok(STTrueFalse.Enum r1);

    void xsetShadowok(STTrueFalse sTTrueFalse);

    void unsetShadowok();

    STTrueFalse.Enum getArrowok();

    STTrueFalse xgetArrowok();

    boolean isSetArrowok();

    void setArrowok(STTrueFalse.Enum r1);

    void xsetArrowok(STTrueFalse sTTrueFalse);

    void unsetArrowok();

    STTrueFalse.Enum getGradientshapeok();

    STTrueFalse xgetGradientshapeok();

    boolean isSetGradientshapeok();

    void setGradientshapeok(STTrueFalse.Enum r1);

    void xsetGradientshapeok(STTrueFalse sTTrueFalse);

    void unsetGradientshapeok();

    STTrueFalse.Enum getTextpathok();

    STTrueFalse xgetTextpathok();

    boolean isSetTextpathok();

    void setTextpathok(STTrueFalse.Enum r1);

    void xsetTextpathok(STTrueFalse sTTrueFalse);

    void unsetTextpathok();

    STTrueFalse.Enum getInsetpenok();

    STTrueFalse xgetInsetpenok();

    boolean isSetInsetpenok();

    void setInsetpenok(STTrueFalse.Enum r1);

    void xsetInsetpenok(STTrueFalse sTTrueFalse);

    void unsetInsetpenok();

    STConnectType.Enum getConnecttype();

    STConnectType xgetConnecttype();

    boolean isSetConnecttype();

    void setConnecttype(STConnectType.Enum r1);

    void xsetConnecttype(STConnectType sTConnectType);

    void unsetConnecttype();

    String getConnectlocs();

    XmlString xgetConnectlocs();

    boolean isSetConnectlocs();

    void setConnectlocs(String str);

    void xsetConnectlocs(XmlString xmlString);

    void unsetConnectlocs();

    String getConnectangles();

    XmlString xgetConnectangles();

    boolean isSetConnectangles();

    void setConnectangles(String str);

    void xsetConnectangles(XmlString xmlString);

    void unsetConnectangles();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getExtrusionok();

    com.microsoft.schemas.office.office.STTrueFalse xgetExtrusionok();

    boolean isSetExtrusionok();

    void setExtrusionok(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetExtrusionok(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetExtrusionok();
}
