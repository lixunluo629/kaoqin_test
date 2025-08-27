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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTAlphaModulateFixedEffect.class */
public interface CTAlphaModulateFixedEffect extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTAlphaModulateFixedEffect.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctalphamodulatefixedeffect9769type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTAlphaModulateFixedEffect$Factory.class */
    public static final class Factory {
        public static CTAlphaModulateFixedEffect newInstance() {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.newInstance(CTAlphaModulateFixedEffect.type, null);
        }

        public static CTAlphaModulateFixedEffect newInstance(XmlOptions xmlOptions) {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.newInstance(CTAlphaModulateFixedEffect.type, xmlOptions);
        }

        public static CTAlphaModulateFixedEffect parse(String str) throws XmlException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(str, CTAlphaModulateFixedEffect.type, (XmlOptions) null);
        }

        public static CTAlphaModulateFixedEffect parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(str, CTAlphaModulateFixedEffect.type, xmlOptions);
        }

        public static CTAlphaModulateFixedEffect parse(File file) throws XmlException, IOException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(file, CTAlphaModulateFixedEffect.type, (XmlOptions) null);
        }

        public static CTAlphaModulateFixedEffect parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(file, CTAlphaModulateFixedEffect.type, xmlOptions);
        }

        public static CTAlphaModulateFixedEffect parse(URL url) throws XmlException, IOException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(url, CTAlphaModulateFixedEffect.type, (XmlOptions) null);
        }

        public static CTAlphaModulateFixedEffect parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(url, CTAlphaModulateFixedEffect.type, xmlOptions);
        }

        public static CTAlphaModulateFixedEffect parse(InputStream inputStream) throws XmlException, IOException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(inputStream, CTAlphaModulateFixedEffect.type, (XmlOptions) null);
        }

        public static CTAlphaModulateFixedEffect parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(inputStream, CTAlphaModulateFixedEffect.type, xmlOptions);
        }

        public static CTAlphaModulateFixedEffect parse(Reader reader) throws XmlException, IOException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(reader, CTAlphaModulateFixedEffect.type, (XmlOptions) null);
        }

        public static CTAlphaModulateFixedEffect parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(reader, CTAlphaModulateFixedEffect.type, xmlOptions);
        }

        public static CTAlphaModulateFixedEffect parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(xMLStreamReader, CTAlphaModulateFixedEffect.type, (XmlOptions) null);
        }

        public static CTAlphaModulateFixedEffect parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(xMLStreamReader, CTAlphaModulateFixedEffect.type, xmlOptions);
        }

        public static CTAlphaModulateFixedEffect parse(Node node) throws XmlException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(node, CTAlphaModulateFixedEffect.type, (XmlOptions) null);
        }

        public static CTAlphaModulateFixedEffect parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(node, CTAlphaModulateFixedEffect.type, xmlOptions);
        }

        public static CTAlphaModulateFixedEffect parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(xMLInputStream, CTAlphaModulateFixedEffect.type, (XmlOptions) null);
        }

        public static CTAlphaModulateFixedEffect parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTAlphaModulateFixedEffect) POIXMLTypeLoader.parse(xMLInputStream, CTAlphaModulateFixedEffect.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTAlphaModulateFixedEffect.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTAlphaModulateFixedEffect.type, xmlOptions);
        }

        private Factory() {
        }
    }

    int getAmt();

    STPositivePercentage xgetAmt();

    boolean isSetAmt();

    void setAmt(int i);

    void xsetAmt(STPositivePercentage sTPositivePercentage);

    void unsetAmt();
}
