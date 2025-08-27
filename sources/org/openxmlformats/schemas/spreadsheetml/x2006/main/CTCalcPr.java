package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCalcPr.class */
public interface CTCalcPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCalcPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcalcprd480type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCalcPr$Factory.class */
    public static final class Factory {
        public static CTCalcPr newInstance() {
            return (CTCalcPr) POIXMLTypeLoader.newInstance(CTCalcPr.type, null);
        }

        public static CTCalcPr newInstance(XmlOptions xmlOptions) {
            return (CTCalcPr) POIXMLTypeLoader.newInstance(CTCalcPr.type, xmlOptions);
        }

        public static CTCalcPr parse(String str) throws XmlException {
            return (CTCalcPr) POIXMLTypeLoader.parse(str, CTCalcPr.type, (XmlOptions) null);
        }

        public static CTCalcPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCalcPr) POIXMLTypeLoader.parse(str, CTCalcPr.type, xmlOptions);
        }

        public static CTCalcPr parse(File file) throws XmlException, IOException {
            return (CTCalcPr) POIXMLTypeLoader.parse(file, CTCalcPr.type, (XmlOptions) null);
        }

        public static CTCalcPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCalcPr) POIXMLTypeLoader.parse(file, CTCalcPr.type, xmlOptions);
        }

        public static CTCalcPr parse(URL url) throws XmlException, IOException {
            return (CTCalcPr) POIXMLTypeLoader.parse(url, CTCalcPr.type, (XmlOptions) null);
        }

        public static CTCalcPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCalcPr) POIXMLTypeLoader.parse(url, CTCalcPr.type, xmlOptions);
        }

        public static CTCalcPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCalcPr) POIXMLTypeLoader.parse(inputStream, CTCalcPr.type, (XmlOptions) null);
        }

        public static CTCalcPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCalcPr) POIXMLTypeLoader.parse(inputStream, CTCalcPr.type, xmlOptions);
        }

        public static CTCalcPr parse(Reader reader) throws XmlException, IOException {
            return (CTCalcPr) POIXMLTypeLoader.parse(reader, CTCalcPr.type, (XmlOptions) null);
        }

        public static CTCalcPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCalcPr) POIXMLTypeLoader.parse(reader, CTCalcPr.type, xmlOptions);
        }

        public static CTCalcPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCalcPr) POIXMLTypeLoader.parse(xMLStreamReader, CTCalcPr.type, (XmlOptions) null);
        }

        public static CTCalcPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCalcPr) POIXMLTypeLoader.parse(xMLStreamReader, CTCalcPr.type, xmlOptions);
        }

        public static CTCalcPr parse(Node node) throws XmlException {
            return (CTCalcPr) POIXMLTypeLoader.parse(node, CTCalcPr.type, (XmlOptions) null);
        }

        public static CTCalcPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCalcPr) POIXMLTypeLoader.parse(node, CTCalcPr.type, xmlOptions);
        }

        public static CTCalcPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCalcPr) POIXMLTypeLoader.parse(xMLInputStream, CTCalcPr.type, (XmlOptions) null);
        }

        public static CTCalcPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCalcPr) POIXMLTypeLoader.parse(xMLInputStream, CTCalcPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCalcPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCalcPr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    long getCalcId();

    XmlUnsignedInt xgetCalcId();

    boolean isSetCalcId();

    void setCalcId(long j);

    void xsetCalcId(XmlUnsignedInt xmlUnsignedInt);

    void unsetCalcId();

    STCalcMode.Enum getCalcMode();

    STCalcMode xgetCalcMode();

    boolean isSetCalcMode();

    void setCalcMode(STCalcMode.Enum r1);

    void xsetCalcMode(STCalcMode sTCalcMode);

    void unsetCalcMode();

    boolean getFullCalcOnLoad();

    XmlBoolean xgetFullCalcOnLoad();

    boolean isSetFullCalcOnLoad();

    void setFullCalcOnLoad(boolean z);

    void xsetFullCalcOnLoad(XmlBoolean xmlBoolean);

    void unsetFullCalcOnLoad();

    STRefMode$Enum getRefMode();

    STRefMode xgetRefMode();

    boolean isSetRefMode();

    void setRefMode(STRefMode$Enum sTRefMode$Enum);

    void xsetRefMode(STRefMode sTRefMode);

    void unsetRefMode();

    boolean getIterate();

    XmlBoolean xgetIterate();

    boolean isSetIterate();

    void setIterate(boolean z);

    void xsetIterate(XmlBoolean xmlBoolean);

    void unsetIterate();

    long getIterateCount();

    XmlUnsignedInt xgetIterateCount();

    boolean isSetIterateCount();

    void setIterateCount(long j);

    void xsetIterateCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetIterateCount();

    double getIterateDelta();

    XmlDouble xgetIterateDelta();

    boolean isSetIterateDelta();

    void setIterateDelta(double d);

    void xsetIterateDelta(XmlDouble xmlDouble);

    void unsetIterateDelta();

    boolean getFullPrecision();

    XmlBoolean xgetFullPrecision();

    boolean isSetFullPrecision();

    void setFullPrecision(boolean z);

    void xsetFullPrecision(XmlBoolean xmlBoolean);

    void unsetFullPrecision();

    boolean getCalcCompleted();

    XmlBoolean xgetCalcCompleted();

    boolean isSetCalcCompleted();

    void setCalcCompleted(boolean z);

    void xsetCalcCompleted(XmlBoolean xmlBoolean);

    void unsetCalcCompleted();

    boolean getCalcOnSave();

    XmlBoolean xgetCalcOnSave();

    boolean isSetCalcOnSave();

    void setCalcOnSave(boolean z);

    void xsetCalcOnSave(XmlBoolean xmlBoolean);

    void unsetCalcOnSave();

    boolean getConcurrentCalc();

    XmlBoolean xgetConcurrentCalc();

    boolean isSetConcurrentCalc();

    void setConcurrentCalc(boolean z);

    void xsetConcurrentCalc(XmlBoolean xmlBoolean);

    void unsetConcurrentCalc();

    long getConcurrentManualCount();

    XmlUnsignedInt xgetConcurrentManualCount();

    boolean isSetConcurrentManualCount();

    void setConcurrentManualCount(long j);

    void xsetConcurrentManualCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetConcurrentManualCount();

    boolean getForceFullCalc();

    XmlBoolean xgetForceFullCalc();

    boolean isSetForceFullCalc();

    void setForceFullCalc(boolean z);

    void xsetForceFullCalc(XmlBoolean xmlBoolean);

    void unsetForceFullCalc();
}
