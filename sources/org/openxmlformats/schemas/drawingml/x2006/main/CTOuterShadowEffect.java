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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTOuterShadowEffect.class */
public interface CTOuterShadowEffect extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTOuterShadowEffect.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctoutershadoweffect7b5dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTOuterShadowEffect$Factory.class */
    public static final class Factory {
        public static CTOuterShadowEffect newInstance() {
            return (CTOuterShadowEffect) POIXMLTypeLoader.newInstance(CTOuterShadowEffect.type, null);
        }

        public static CTOuterShadowEffect newInstance(XmlOptions xmlOptions) {
            return (CTOuterShadowEffect) POIXMLTypeLoader.newInstance(CTOuterShadowEffect.type, xmlOptions);
        }

        public static CTOuterShadowEffect parse(String str) throws XmlException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(str, CTOuterShadowEffect.type, (XmlOptions) null);
        }

        public static CTOuterShadowEffect parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(str, CTOuterShadowEffect.type, xmlOptions);
        }

        public static CTOuterShadowEffect parse(File file) throws XmlException, IOException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(file, CTOuterShadowEffect.type, (XmlOptions) null);
        }

        public static CTOuterShadowEffect parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(file, CTOuterShadowEffect.type, xmlOptions);
        }

        public static CTOuterShadowEffect parse(URL url) throws XmlException, IOException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(url, CTOuterShadowEffect.type, (XmlOptions) null);
        }

        public static CTOuterShadowEffect parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(url, CTOuterShadowEffect.type, xmlOptions);
        }

        public static CTOuterShadowEffect parse(InputStream inputStream) throws XmlException, IOException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(inputStream, CTOuterShadowEffect.type, (XmlOptions) null);
        }

        public static CTOuterShadowEffect parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(inputStream, CTOuterShadowEffect.type, xmlOptions);
        }

        public static CTOuterShadowEffect parse(Reader reader) throws XmlException, IOException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(reader, CTOuterShadowEffect.type, (XmlOptions) null);
        }

        public static CTOuterShadowEffect parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(reader, CTOuterShadowEffect.type, xmlOptions);
        }

        public static CTOuterShadowEffect parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(xMLStreamReader, CTOuterShadowEffect.type, (XmlOptions) null);
        }

        public static CTOuterShadowEffect parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(xMLStreamReader, CTOuterShadowEffect.type, xmlOptions);
        }

        public static CTOuterShadowEffect parse(Node node) throws XmlException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(node, CTOuterShadowEffect.type, (XmlOptions) null);
        }

        public static CTOuterShadowEffect parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(node, CTOuterShadowEffect.type, xmlOptions);
        }

        public static CTOuterShadowEffect parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(xMLInputStream, CTOuterShadowEffect.type, (XmlOptions) null);
        }

        public static CTOuterShadowEffect parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTOuterShadowEffect) POIXMLTypeLoader.parse(xMLInputStream, CTOuterShadowEffect.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTOuterShadowEffect.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTOuterShadowEffect.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTScRgbColor getScrgbClr();

    boolean isSetScrgbClr();

    void setScrgbClr(CTScRgbColor cTScRgbColor);

    CTScRgbColor addNewScrgbClr();

    void unsetScrgbClr();

    CTSRgbColor getSrgbClr();

    boolean isSetSrgbClr();

    void setSrgbClr(CTSRgbColor cTSRgbColor);

    CTSRgbColor addNewSrgbClr();

    void unsetSrgbClr();

    CTHslColor getHslClr();

    boolean isSetHslClr();

    void setHslClr(CTHslColor cTHslColor);

    CTHslColor addNewHslClr();

    void unsetHslClr();

    CTSystemColor getSysClr();

    boolean isSetSysClr();

    void setSysClr(CTSystemColor cTSystemColor);

    CTSystemColor addNewSysClr();

    void unsetSysClr();

    CTSchemeColor getSchemeClr();

    boolean isSetSchemeClr();

    void setSchemeClr(CTSchemeColor cTSchemeColor);

    CTSchemeColor addNewSchemeClr();

    void unsetSchemeClr();

    CTPresetColor getPrstClr();

    boolean isSetPrstClr();

    void setPrstClr(CTPresetColor cTPresetColor);

    CTPresetColor addNewPrstClr();

    void unsetPrstClr();

    long getBlurRad();

    STPositiveCoordinate xgetBlurRad();

    boolean isSetBlurRad();

    void setBlurRad(long j);

    void xsetBlurRad(STPositiveCoordinate sTPositiveCoordinate);

    void unsetBlurRad();

    long getDist();

    STPositiveCoordinate xgetDist();

    boolean isSetDist();

    void setDist(long j);

    void xsetDist(STPositiveCoordinate sTPositiveCoordinate);

    void unsetDist();

    int getDir();

    STPositiveFixedAngle xgetDir();

    boolean isSetDir();

    void setDir(int i);

    void xsetDir(STPositiveFixedAngle sTPositiveFixedAngle);

    void unsetDir();

    int getSx();

    STPercentage xgetSx();

    boolean isSetSx();

    void setSx(int i);

    void xsetSx(STPercentage sTPercentage);

    void unsetSx();

    int getSy();

    STPercentage xgetSy();

    boolean isSetSy();

    void setSy(int i);

    void xsetSy(STPercentage sTPercentage);

    void unsetSy();

    int getKx();

    STFixedAngle xgetKx();

    boolean isSetKx();

    void setKx(int i);

    void xsetKx(STFixedAngle sTFixedAngle);

    void unsetKx();

    int getKy();

    STFixedAngle xgetKy();

    boolean isSetKy();

    void setKy(int i);

    void xsetKy(STFixedAngle sTFixedAngle);

    void unsetKy();

    STRectAlignment$Enum getAlgn();

    STRectAlignment xgetAlgn();

    boolean isSetAlgn();

    void setAlgn(STRectAlignment$Enum sTRectAlignment$Enum);

    void xsetAlgn(STRectAlignment sTRectAlignment);

    void unsetAlgn();

    boolean getRotWithShape();

    XmlBoolean xgetRotWithShape();

    boolean isSetRotWithShape();

    void setRotWithShape(boolean z);

    void xsetRotWithShape(XmlBoolean xmlBoolean);

    void unsetRotWithShape();
}
