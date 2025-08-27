package com.microsoft.schemas.vml;

import com.microsoft.schemas.office.office.CTStrokeChild;
import com.microsoft.schemas.vml.STStrokeJoinStyle;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTStroke.class */
public interface CTStroke extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTStroke.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctstrokee2f6type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/CTStroke$Factory.class */
    public static final class Factory {
        public static CTStroke newInstance() {
            return (CTStroke) POIXMLTypeLoader.newInstance(CTStroke.type, null);
        }

        public static CTStroke newInstance(XmlOptions xmlOptions) {
            return (CTStroke) POIXMLTypeLoader.newInstance(CTStroke.type, xmlOptions);
        }

        public static CTStroke parse(String str) throws XmlException {
            return (CTStroke) POIXMLTypeLoader.parse(str, CTStroke.type, (XmlOptions) null);
        }

        public static CTStroke parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTStroke) POIXMLTypeLoader.parse(str, CTStroke.type, xmlOptions);
        }

        public static CTStroke parse(File file) throws XmlException, IOException {
            return (CTStroke) POIXMLTypeLoader.parse(file, CTStroke.type, (XmlOptions) null);
        }

        public static CTStroke parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStroke) POIXMLTypeLoader.parse(file, CTStroke.type, xmlOptions);
        }

        public static CTStroke parse(URL url) throws XmlException, IOException {
            return (CTStroke) POIXMLTypeLoader.parse(url, CTStroke.type, (XmlOptions) null);
        }

        public static CTStroke parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStroke) POIXMLTypeLoader.parse(url, CTStroke.type, xmlOptions);
        }

        public static CTStroke parse(InputStream inputStream) throws XmlException, IOException {
            return (CTStroke) POIXMLTypeLoader.parse(inputStream, CTStroke.type, (XmlOptions) null);
        }

        public static CTStroke parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStroke) POIXMLTypeLoader.parse(inputStream, CTStroke.type, xmlOptions);
        }

        public static CTStroke parse(Reader reader) throws XmlException, IOException {
            return (CTStroke) POIXMLTypeLoader.parse(reader, CTStroke.type, (XmlOptions) null);
        }

        public static CTStroke parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStroke) POIXMLTypeLoader.parse(reader, CTStroke.type, xmlOptions);
        }

        public static CTStroke parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTStroke) POIXMLTypeLoader.parse(xMLStreamReader, CTStroke.type, (XmlOptions) null);
        }

        public static CTStroke parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTStroke) POIXMLTypeLoader.parse(xMLStreamReader, CTStroke.type, xmlOptions);
        }

        public static CTStroke parse(Node node) throws XmlException {
            return (CTStroke) POIXMLTypeLoader.parse(node, CTStroke.type, (XmlOptions) null);
        }

        public static CTStroke parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTStroke) POIXMLTypeLoader.parse(node, CTStroke.type, xmlOptions);
        }

        public static CTStroke parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTStroke) POIXMLTypeLoader.parse(xMLInputStream, CTStroke.type, (XmlOptions) null);
        }

        public static CTStroke parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTStroke) POIXMLTypeLoader.parse(xMLInputStream, CTStroke.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStroke.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStroke.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTStrokeChild getLeft();

    boolean isSetLeft();

    void setLeft(CTStrokeChild cTStrokeChild);

    CTStrokeChild addNewLeft();

    void unsetLeft();

    CTStrokeChild getTop();

    boolean isSetTop();

    void setTop(CTStrokeChild cTStrokeChild);

    CTStrokeChild addNewTop();

    void unsetTop();

    CTStrokeChild getRight();

    boolean isSetRight();

    void setRight(CTStrokeChild cTStrokeChild);

    CTStrokeChild addNewRight();

    void unsetRight();

    CTStrokeChild getBottom();

    boolean isSetBottom();

    void setBottom(CTStrokeChild cTStrokeChild);

    CTStrokeChild addNewBottom();

    void unsetBottom();

    CTStrokeChild getColumn();

    boolean isSetColumn();

    void setColumn(CTStrokeChild cTStrokeChild);

    CTStrokeChild addNewColumn();

    void unsetColumn();

    String getId();

    XmlString xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlString xmlString);

    void unsetId();

    STTrueFalse.Enum getOn();

    STTrueFalse xgetOn();

    boolean isSetOn();

    void setOn(STTrueFalse.Enum r1);

    void xsetOn(STTrueFalse sTTrueFalse);

    void unsetOn();

    String getWeight();

    XmlString xgetWeight();

    boolean isSetWeight();

    void setWeight(String str);

    void xsetWeight(XmlString xmlString);

    void unsetWeight();

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

    STStrokeLineStyle$Enum getLinestyle();

    STStrokeLineStyle xgetLinestyle();

    boolean isSetLinestyle();

    void setLinestyle(STStrokeLineStyle$Enum sTStrokeLineStyle$Enum);

    void xsetLinestyle(STStrokeLineStyle sTStrokeLineStyle);

    void unsetLinestyle();

    BigDecimal getMiterlimit();

    XmlDecimal xgetMiterlimit();

    boolean isSetMiterlimit();

    void setMiterlimit(BigDecimal bigDecimal);

    void xsetMiterlimit(XmlDecimal xmlDecimal);

    void unsetMiterlimit();

    STStrokeJoinStyle.Enum getJoinstyle();

    STStrokeJoinStyle xgetJoinstyle();

    boolean isSetJoinstyle();

    void setJoinstyle(STStrokeJoinStyle.Enum r1);

    void xsetJoinstyle(STStrokeJoinStyle sTStrokeJoinStyle);

    void unsetJoinstyle();

    STStrokeEndCap$Enum getEndcap();

    STStrokeEndCap xgetEndcap();

    boolean isSetEndcap();

    void setEndcap(STStrokeEndCap$Enum sTStrokeEndCap$Enum);

    void xsetEndcap(STStrokeEndCap sTStrokeEndCap);

    void unsetEndcap();

    String getDashstyle();

    XmlString xgetDashstyle();

    boolean isSetDashstyle();

    void setDashstyle(String str);

    void xsetDashstyle(XmlString xmlString);

    void unsetDashstyle();

    STFillType$Enum getFilltype();

    STFillType xgetFilltype();

    boolean isSetFilltype();

    void setFilltype(STFillType$Enum sTFillType$Enum);

    void xsetFilltype(STFillType sTFillType);

    void unsetFilltype();

    String getSrc();

    XmlString xgetSrc();

    boolean isSetSrc();

    void setSrc(String str);

    void xsetSrc(XmlString xmlString);

    void unsetSrc();

    STImageAspect$Enum getImageaspect();

    STImageAspect xgetImageaspect();

    boolean isSetImageaspect();

    void setImageaspect(STImageAspect$Enum sTImageAspect$Enum);

    void xsetImageaspect(STImageAspect sTImageAspect);

    void unsetImageaspect();

    String getImagesize();

    XmlString xgetImagesize();

    boolean isSetImagesize();

    void setImagesize(String str);

    void xsetImagesize(XmlString xmlString);

    void unsetImagesize();

    STTrueFalse.Enum getImagealignshape();

    STTrueFalse xgetImagealignshape();

    boolean isSetImagealignshape();

    void setImagealignshape(STTrueFalse.Enum r1);

    void xsetImagealignshape(STTrueFalse sTTrueFalse);

    void unsetImagealignshape();

    String getColor2();

    STColorType xgetColor2();

    boolean isSetColor2();

    void setColor2(String str);

    void xsetColor2(STColorType sTColorType);

    void unsetColor2();

    STStrokeArrowType$Enum getStartarrow();

    STStrokeArrowType xgetStartarrow();

    boolean isSetStartarrow();

    void setStartarrow(STStrokeArrowType$Enum sTStrokeArrowType$Enum);

    void xsetStartarrow(STStrokeArrowType sTStrokeArrowType);

    void unsetStartarrow();

    STStrokeArrowWidth$Enum getStartarrowwidth();

    STStrokeArrowWidth xgetStartarrowwidth();

    boolean isSetStartarrowwidth();

    void setStartarrowwidth(STStrokeArrowWidth$Enum sTStrokeArrowWidth$Enum);

    void xsetStartarrowwidth(STStrokeArrowWidth sTStrokeArrowWidth);

    void unsetStartarrowwidth();

    STStrokeArrowLength$Enum getStartarrowlength();

    STStrokeArrowLength xgetStartarrowlength();

    boolean isSetStartarrowlength();

    void setStartarrowlength(STStrokeArrowLength$Enum sTStrokeArrowLength$Enum);

    void xsetStartarrowlength(STStrokeArrowLength sTStrokeArrowLength);

    void unsetStartarrowlength();

    STStrokeArrowType$Enum getEndarrow();

    STStrokeArrowType xgetEndarrow();

    boolean isSetEndarrow();

    void setEndarrow(STStrokeArrowType$Enum sTStrokeArrowType$Enum);

    void xsetEndarrow(STStrokeArrowType sTStrokeArrowType);

    void unsetEndarrow();

    STStrokeArrowWidth$Enum getEndarrowwidth();

    STStrokeArrowWidth xgetEndarrowwidth();

    boolean isSetEndarrowwidth();

    void setEndarrowwidth(STStrokeArrowWidth$Enum sTStrokeArrowWidth$Enum);

    void xsetEndarrowwidth(STStrokeArrowWidth sTStrokeArrowWidth);

    void unsetEndarrowwidth();

    STStrokeArrowLength$Enum getEndarrowlength();

    STStrokeArrowLength xgetEndarrowlength();

    boolean isSetEndarrowlength();

    void setEndarrowlength(STStrokeArrowLength$Enum sTStrokeArrowLength$Enum);

    void xsetEndarrowlength(STStrokeArrowLength sTStrokeArrowLength);

    void unsetEndarrowlength();

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

    String getTitle();

    XmlString xgetTitle();

    boolean isSetTitle();

    void setTitle(String str);

    void xsetTitle(XmlString xmlString);

    void unsetTitle();

    com.microsoft.schemas.office.office.STTrueFalse$Enum getForcedash();

    com.microsoft.schemas.office.office.STTrueFalse xgetForcedash();

    boolean isSetForcedash();

    void setForcedash(com.microsoft.schemas.office.office.STTrueFalse$Enum sTTrueFalse$Enum);

    void xsetForcedash(com.microsoft.schemas.office.office.STTrueFalse sTTrueFalse);

    void unsetForcedash();

    String getId2();

    STRelationshipId xgetId2();

    boolean isSetId2();

    void setId2(String str);

    void xsetId2(STRelationshipId sTRelationshipId);

    void unsetId2();

    STTrueFalse.Enum getInsetpen();

    STTrueFalse xgetInsetpen();

    boolean isSetInsetpen();

    void setInsetpen(STTrueFalse.Enum r1);

    void xsetInsetpen(STTrueFalse sTTrueFalse);

    void unsetInsetpen();

    String getRelid();

    com.microsoft.schemas.office.office.STRelationshipId xgetRelid();

    boolean isSetRelid();

    void setRelid(String str);

    void xsetRelid(com.microsoft.schemas.office.office.STRelationshipId sTRelationshipId);

    void unsetRelid();
}
