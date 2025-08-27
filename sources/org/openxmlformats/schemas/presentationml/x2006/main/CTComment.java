package org.openxmlformats.schemas.presentationml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Calendar;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTComment.class */
public interface CTComment extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTComment.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcomment2d10type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTComment$Factory.class */
    public static final class Factory {
        public static CTComment newInstance() {
            return (CTComment) POIXMLTypeLoader.newInstance(CTComment.type, null);
        }

        public static CTComment newInstance(XmlOptions xmlOptions) {
            return (CTComment) POIXMLTypeLoader.newInstance(CTComment.type, xmlOptions);
        }

        public static CTComment parse(String str) throws XmlException {
            return (CTComment) POIXMLTypeLoader.parse(str, CTComment.type, (XmlOptions) null);
        }

        public static CTComment parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTComment) POIXMLTypeLoader.parse(str, CTComment.type, xmlOptions);
        }

        public static CTComment parse(File file) throws XmlException, IOException {
            return (CTComment) POIXMLTypeLoader.parse(file, CTComment.type, (XmlOptions) null);
        }

        public static CTComment parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTComment) POIXMLTypeLoader.parse(file, CTComment.type, xmlOptions);
        }

        public static CTComment parse(URL url) throws XmlException, IOException {
            return (CTComment) POIXMLTypeLoader.parse(url, CTComment.type, (XmlOptions) null);
        }

        public static CTComment parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTComment) POIXMLTypeLoader.parse(url, CTComment.type, xmlOptions);
        }

        public static CTComment parse(InputStream inputStream) throws XmlException, IOException {
            return (CTComment) POIXMLTypeLoader.parse(inputStream, CTComment.type, (XmlOptions) null);
        }

        public static CTComment parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTComment) POIXMLTypeLoader.parse(inputStream, CTComment.type, xmlOptions);
        }

        public static CTComment parse(Reader reader) throws XmlException, IOException {
            return (CTComment) POIXMLTypeLoader.parse(reader, CTComment.type, (XmlOptions) null);
        }

        public static CTComment parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTComment) POIXMLTypeLoader.parse(reader, CTComment.type, xmlOptions);
        }

        public static CTComment parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTComment) POIXMLTypeLoader.parse(xMLStreamReader, CTComment.type, (XmlOptions) null);
        }

        public static CTComment parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTComment) POIXMLTypeLoader.parse(xMLStreamReader, CTComment.type, xmlOptions);
        }

        public static CTComment parse(Node node) throws XmlException {
            return (CTComment) POIXMLTypeLoader.parse(node, CTComment.type, (XmlOptions) null);
        }

        public static CTComment parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTComment) POIXMLTypeLoader.parse(node, CTComment.type, xmlOptions);
        }

        public static CTComment parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTComment) POIXMLTypeLoader.parse(xMLInputStream, CTComment.type, (XmlOptions) null);
        }

        public static CTComment parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTComment) POIXMLTypeLoader.parse(xMLInputStream, CTComment.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTComment.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTComment.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTPoint2D getPos();

    void setPos(CTPoint2D cTPoint2D);

    CTPoint2D addNewPos();

    String getText();

    XmlString xgetText();

    void setText(String str);

    void xsetText(XmlString xmlString);

    CTExtensionListModify getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionListModify cTExtensionListModify);

    CTExtensionListModify addNewExtLst();

    void unsetExtLst();

    long getAuthorId();

    XmlUnsignedInt xgetAuthorId();

    void setAuthorId(long j);

    void xsetAuthorId(XmlUnsignedInt xmlUnsignedInt);

    Calendar getDt();

    XmlDateTime xgetDt();

    boolean isSetDt();

    void setDt(Calendar calendar);

    void xsetDt(XmlDateTime xmlDateTime);

    void unsetDt();

    long getIdx();

    STIndex xgetIdx();

    void setIdx(long j);

    void xsetIdx(STIndex sTIndex);
}
