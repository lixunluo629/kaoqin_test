package com.microsoft.schemas.vml;

import com.microsoft.schemas.vml.STTrueFalse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlDecimal;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTFill.class */
public interface CTFill extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTFill.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctfillb241type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTFill$Factory.class */
    public static final class Factory {
        public static CTFill newInstance() {
            return (CTFill) POIXMLTypeLoader.newInstance(CTFill.type, null);
        }

        public static CTFill newInstance(XmlOptions xmlOptions) {
            return (CTFill) POIXMLTypeLoader.newInstance(CTFill.type, xmlOptions);
        }

        public static CTFill parse(String str) throws XmlException {
            return (CTFill) POIXMLTypeLoader.parse(str, CTFill.type, (XmlOptions) null);
        }

        public static CTFill parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTFill) POIXMLTypeLoader.parse(str, CTFill.type, xmlOptions);
        }

        public static CTFill parse(File file) throws XmlException, IOException {
            return (CTFill) POIXMLTypeLoader.parse(file, CTFill.type, (XmlOptions) null);
        }

        public static CTFill parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFill) POIXMLTypeLoader.parse(file, CTFill.type, xmlOptions);
        }

        public static CTFill parse(URL url) throws XmlException, IOException {
            return (CTFill) POIXMLTypeLoader.parse(url, CTFill.type, (XmlOptions) null);
        }

        public static CTFill parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFill) POIXMLTypeLoader.parse(url, CTFill.type, xmlOptions);
        }

        public static CTFill parse(InputStream inputStream) throws XmlException, IOException {
            return (CTFill) POIXMLTypeLoader.parse(inputStream, CTFill.type, (XmlOptions) null);
        }

        public static CTFill parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFill) POIXMLTypeLoader.parse(inputStream, CTFill.type, xmlOptions);
        }

        public static CTFill parse(Reader reader) throws XmlException, IOException {
            return (CTFill) POIXMLTypeLoader.parse(reader, CTFill.type, (XmlOptions) null);
        }

        public static CTFill parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTFill) POIXMLTypeLoader.parse(reader, CTFill.type, xmlOptions);
        }

        public static CTFill parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTFill) POIXMLTypeLoader.parse(xMLStreamReader, CTFill.type, (XmlOptions) null);
        }

        public static CTFill parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTFill) POIXMLTypeLoader.parse(xMLStreamReader, CTFill.type, xmlOptions);
        }

        public static CTFill parse(Node node) throws XmlException {
            return (CTFill) POIXMLTypeLoader.parse(node, CTFill.type, (XmlOptions) null);
        }

        public static CTFill parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTFill) POIXMLTypeLoader.parse(node, CTFill.type, xmlOptions);
        }

        public static CTFill parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTFill) POIXMLTypeLoader.parse(xMLInputStream, CTFill.type, (XmlOptions) null);
        }

        public static CTFill parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTFill) POIXMLTypeLoader.parse(xMLInputStream, CTFill.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFill.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTFill.type, xmlOptions);
        }

        private Factory() {
        }
    }

    com.microsoft.schemas.office.office.CTFill getFill();

    boolean isSetFill();

    void setFill(com.microsoft.schemas.office.office.CTFill cTFill);

    com.microsoft.schemas.office.office.CTFill addNewFill();

    void unsetFill();

    String getId();

    XmlString xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlString xmlString);

    void unsetId();

    STFillType$Enum getType();

    STFillType xgetType();

    boolean isSetType();

    void setType(STFillType$Enum sTFillType$Enum);

    void xsetType(STFillType sTFillType);

    void unsetType();

    STTrueFalse.Enum getOn();

    STTrueFalse xgetOn();

    boolean isSetOn();

    void setOn(STTrueFalse.Enum r1);

    void xsetOn(STTrueFalse sTTrueFalse);

    void unsetOn();

    String getColor();

    STColorType xgetColor();

    boolean isSetColor();

    void setColor(String str);

    void xsetColor(STColorType sTColorType);

    void unsetColor();

    String getOpacity();

    XmlString xgetOpacity();

    boolean isSetOpacity();

    void setOpacity(String str);

    void xsetOpacity(XmlString xmlString);

    void unsetOpacity();

    String getColor2();

    STColorType xgetColor2();

    boolean isSetColor2();

    void setColor2(String str);

    void xsetColor2(STColorType sTColorType);

    void unsetColor2();

    String getSrc();

    XmlString xgetSrc();

    boolean isSetSrc();

    void setSrc(String str);

    void xsetSrc(XmlString xmlString);

    void unsetSrc();

    String getHref();

    XmlString xgetHref();

    boolean isSetHref();

    void setHref(String str);

    void xsetHref(XmlString xmlString);

    void unsetHref();

    String getAlthref();

    XmlString xgetAlthref();

    boolean isSetAlthref();

    void setAlthref(String str);

    void xsetAlthref(XmlString xmlString);

    void unsetAlthref();

    String getSize();

    XmlString xgetSize();

    boolean isSetSize();

    void setSize(String str);

    void xsetSize(XmlString xmlString);

    void unsetSize();

    String getOrigin();

    XmlString xgetOrigin();

    boolean isSetOrigin();

    void setOrigin(String str);

    void xsetOrigin(XmlString xmlString);

    void unsetOrigin();

    String getPosition();

    XmlString xgetPosition();

    boolean isSetPosition();

    void setPosition(String str);

    void xsetPosition(XmlString xmlString);

    void unsetPosition();

    STImageAspect$Enum getAspect();

    STImageAspect xgetAspect();

    boolean isSetAspect();

    void setAspect(STImageAspect$Enum sTImageAspect$Enum);

    void xsetAspect(STImageAspect sTImageAspect);

    void unsetAspect();

    String getColors();

    XmlString xgetColors();

    boolean isSetColors();

    void setColors(String str);

    void xsetColors(XmlString xmlString);

    void unsetColors();

    BigDecimal getAngle();

    XmlDecimal xgetAngle();

    boolean isSetAngle();

    void setAngle(BigDecimal bigDecimal);

    void xsetAngle(XmlDecimal xmlDecimal);

    void unsetAngle();

    STTrueFalse.Enum getAlignshape();

    STTrueFalse xgetAlignshape();

    boolean isSetAlignshape();

    void setAlignshape(STTrueFalse.Enum r1);

    void xsetAlignshape(STTrueFalse sTTrueFalse);

    void unsetAlignshape();

    String getFocus();

    XmlString xgetFocus();

    boolean isSetFocus();

    void setFocus(String str);

    void xsetFocus(XmlString xmlString);

    void unsetFocus();

    String getFocussize();

    XmlString xgetFocussize();

    boolean isSetFocussize();

    void setFocussize(String str);

    void xsetFocussize(XmlString xmlString);

    void unsetFocussize();

    String getFocusposition();

    XmlString xgetFocusposition();

    boolean isSetFocusposition();

    void setFocusposition(String str);

    void xsetFocusposition(XmlString xmlString);

    void unsetFocusposition();

    STFillMethod$Enum getMethod();

    STFillMethod xgetMethod();

    boolean isSetMethod();

    void setMethod(STFillMethod$Enum sTFillMethod$Enum);

    void xsetMethod(STFillMethod sTFillMethod);

    void unsetMethod();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getDetectmouseclick();

    com.microsoft.schemas.office.office.STTrueFalse xgetDetectmouseclick();

    boolean isSetDetectmouseclick();

    void setDetectmouseclick(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetDetectmouseclick(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetDetectmouseclick();

    String getTitle();

    XmlString xgetTitle();

    boolean isSetTitle();

    void setTitle(String str);

    void xsetTitle(XmlString xmlString);

    void unsetTitle();

    String getOpacity2();

    XmlString xgetOpacity2();

    boolean isSetOpacity2();

    void setOpacity2(String str);

    void xsetOpacity2(XmlString xmlString);

    void unsetOpacity2();

    STTrueFalse.Enum getRecolor();

    STTrueFalse xgetRecolor();

    boolean isSetRecolor();

    void setRecolor(STTrueFalse.Enum r1);

    void xsetRecolor(STTrueFalse sTTrueFalse);

    void unsetRecolor();

    STTrueFalse.Enum getRotate();

    STTrueFalse xgetRotate();

    boolean isSetRotate();

    void setRotate(STTrueFalse.Enum r1);

    void xsetRotate(STTrueFalse sTTrueFalse);

    void unsetRotate();

    String getId2();

    STRelationshipId xgetId2();

    boolean isSetId2();

    void setId2(String str);

    void xsetId2(STRelationshipId sTRelationshipId);

    void unsetId2();

    String getRelid();

    com.microsoft.schemas.office.office.STRelationshipId xgetRelid();

    boolean isSetRelid();

    void setRelid(String str);

    void xsetRelid(com.microsoft.schemas.office.office.STRelationshipId sTRelationshipId);

    void unsetRelid();
}
