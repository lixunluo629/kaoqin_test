package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTRst.class */
public interface CTRst extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTRst.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctrsta472type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTRst$Factory.class */
    public static final class Factory {
        public static CTRst newInstance() {
            return (CTRst) POIXMLTypeLoader.newInstance(CTRst.type, null);
        }

        public static CTRst newInstance(XmlOptions xmlOptions) {
            return (CTRst) POIXMLTypeLoader.newInstance(CTRst.type, xmlOptions);
        }

        public static CTRst parse(String str) throws XmlException {
            return (CTRst) POIXMLTypeLoader.parse(str, CTRst.type, (XmlOptions) null);
        }

        public static CTRst parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTRst) POIXMLTypeLoader.parse(str, CTRst.type, xmlOptions);
        }

        public static CTRst parse(File file) throws XmlException, IOException {
            return (CTRst) POIXMLTypeLoader.parse(file, CTRst.type, (XmlOptions) null);
        }

        public static CTRst parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRst) POIXMLTypeLoader.parse(file, CTRst.type, xmlOptions);
        }

        public static CTRst parse(URL url) throws XmlException, IOException {
            return (CTRst) POIXMLTypeLoader.parse(url, CTRst.type, (XmlOptions) null);
        }

        public static CTRst parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRst) POIXMLTypeLoader.parse(url, CTRst.type, xmlOptions);
        }

        public static CTRst parse(InputStream inputStream) throws XmlException, IOException {
            return (CTRst) POIXMLTypeLoader.parse(inputStream, CTRst.type, (XmlOptions) null);
        }

        public static CTRst parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRst) POIXMLTypeLoader.parse(inputStream, CTRst.type, xmlOptions);
        }

        public static CTRst parse(Reader reader) throws XmlException, IOException {
            return (CTRst) POIXMLTypeLoader.parse(reader, CTRst.type, (XmlOptions) null);
        }

        public static CTRst parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTRst) POIXMLTypeLoader.parse(reader, CTRst.type, xmlOptions);
        }

        public static CTRst parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTRst) POIXMLTypeLoader.parse(xMLStreamReader, CTRst.type, (XmlOptions) null);
        }

        public static CTRst parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTRst) POIXMLTypeLoader.parse(xMLStreamReader, CTRst.type, xmlOptions);
        }

        public static CTRst parse(Node node) throws XmlException {
            return (CTRst) POIXMLTypeLoader.parse(node, CTRst.type, (XmlOptions) null);
        }

        public static CTRst parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTRst) POIXMLTypeLoader.parse(node, CTRst.type, xmlOptions);
        }

        public static CTRst parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTRst) POIXMLTypeLoader.parse(xMLInputStream, CTRst.type, (XmlOptions) null);
        }

        public static CTRst parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTRst) POIXMLTypeLoader.parse(xMLInputStream, CTRst.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRst.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTRst.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getT();

    STXstring xgetT();

    boolean isSetT();

    void setT(String str);

    void xsetT(STXstring sTXstring);

    void unsetT();

    List<CTRElt> getRList();

    CTRElt[] getRArray();

    CTRElt getRArray(int i);

    int sizeOfRArray();

    void setRArray(CTRElt[] cTREltArr);

    void setRArray(int i, CTRElt cTRElt);

    CTRElt insertNewR(int i);

    CTRElt addNewR();

    void removeR(int i);

    List<CTPhoneticRun> getRPhList();

    CTPhoneticRun[] getRPhArray();

    CTPhoneticRun getRPhArray(int i);

    int sizeOfRPhArray();

    void setRPhArray(CTPhoneticRun[] cTPhoneticRunArr);

    void setRPhArray(int i, CTPhoneticRun cTPhoneticRun);

    CTPhoneticRun insertNewRPh(int i);

    CTPhoneticRun addNewRPh();

    void removeRPh(int i);

    CTPhoneticPr getPhoneticPr();

    boolean isSetPhoneticPr();

    void setPhoneticPr(CTPhoneticPr cTPhoneticPr);

    CTPhoneticPr addNewPhoneticPr();

    void unsetPhoneticPr();
}
