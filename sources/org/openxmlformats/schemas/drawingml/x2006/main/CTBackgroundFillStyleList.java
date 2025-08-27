package org.openxmlformats.schemas.drawingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTBackgroundFillStyleList.class */
public interface CTBackgroundFillStyleList extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTBackgroundFillStyleList.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctbackgroundfillstylelist13cftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTBackgroundFillStyleList$Factory.class */
    public static final class Factory {
        public static CTBackgroundFillStyleList newInstance() {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.newInstance(CTBackgroundFillStyleList.type, null);
        }

        public static CTBackgroundFillStyleList newInstance(XmlOptions xmlOptions) {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.newInstance(CTBackgroundFillStyleList.type, xmlOptions);
        }

        public static CTBackgroundFillStyleList parse(String str) throws XmlException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(str, CTBackgroundFillStyleList.type, (XmlOptions) null);
        }

        public static CTBackgroundFillStyleList parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(str, CTBackgroundFillStyleList.type, xmlOptions);
        }

        public static CTBackgroundFillStyleList parse(File file) throws XmlException, IOException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(file, CTBackgroundFillStyleList.type, (XmlOptions) null);
        }

        public static CTBackgroundFillStyleList parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(file, CTBackgroundFillStyleList.type, xmlOptions);
        }

        public static CTBackgroundFillStyleList parse(URL url) throws XmlException, IOException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(url, CTBackgroundFillStyleList.type, (XmlOptions) null);
        }

        public static CTBackgroundFillStyleList parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(url, CTBackgroundFillStyleList.type, xmlOptions);
        }

        public static CTBackgroundFillStyleList parse(InputStream inputStream) throws XmlException, IOException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(inputStream, CTBackgroundFillStyleList.type, (XmlOptions) null);
        }

        public static CTBackgroundFillStyleList parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(inputStream, CTBackgroundFillStyleList.type, xmlOptions);
        }

        public static CTBackgroundFillStyleList parse(Reader reader) throws XmlException, IOException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(reader, CTBackgroundFillStyleList.type, (XmlOptions) null);
        }

        public static CTBackgroundFillStyleList parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(reader, CTBackgroundFillStyleList.type, xmlOptions);
        }

        public static CTBackgroundFillStyleList parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(xMLStreamReader, CTBackgroundFillStyleList.type, (XmlOptions) null);
        }

        public static CTBackgroundFillStyleList parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(xMLStreamReader, CTBackgroundFillStyleList.type, xmlOptions);
        }

        public static CTBackgroundFillStyleList parse(Node node) throws XmlException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(node, CTBackgroundFillStyleList.type, (XmlOptions) null);
        }

        public static CTBackgroundFillStyleList parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(node, CTBackgroundFillStyleList.type, xmlOptions);
        }

        public static CTBackgroundFillStyleList parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(xMLInputStream, CTBackgroundFillStyleList.type, (XmlOptions) null);
        }

        public static CTBackgroundFillStyleList parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTBackgroundFillStyleList) POIXMLTypeLoader.parse(xMLInputStream, CTBackgroundFillStyleList.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBackgroundFillStyleList.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBackgroundFillStyleList.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTNoFillProperties> getNoFillList();

    CTNoFillProperties[] getNoFillArray();

    CTNoFillProperties getNoFillArray(int i);

    int sizeOfNoFillArray();

    void setNoFillArray(CTNoFillProperties[] cTNoFillPropertiesArr);

    void setNoFillArray(int i, CTNoFillProperties cTNoFillProperties);

    CTNoFillProperties insertNewNoFill(int i);

    CTNoFillProperties addNewNoFill();

    void removeNoFill(int i);

    List<CTSolidColorFillProperties> getSolidFillList();

    CTSolidColorFillProperties[] getSolidFillArray();

    CTSolidColorFillProperties getSolidFillArray(int i);

    int sizeOfSolidFillArray();

    void setSolidFillArray(CTSolidColorFillProperties[] cTSolidColorFillPropertiesArr);

    void setSolidFillArray(int i, CTSolidColorFillProperties cTSolidColorFillProperties);

    CTSolidColorFillProperties insertNewSolidFill(int i);

    CTSolidColorFillProperties addNewSolidFill();

    void removeSolidFill(int i);

    List<CTGradientFillProperties> getGradFillList();

    CTGradientFillProperties[] getGradFillArray();

    CTGradientFillProperties getGradFillArray(int i);

    int sizeOfGradFillArray();

    void setGradFillArray(CTGradientFillProperties[] cTGradientFillPropertiesArr);

    void setGradFillArray(int i, CTGradientFillProperties cTGradientFillProperties);

    CTGradientFillProperties insertNewGradFill(int i);

    CTGradientFillProperties addNewGradFill();

    void removeGradFill(int i);

    List<CTBlipFillProperties> getBlipFillList();

    CTBlipFillProperties[] getBlipFillArray();

    CTBlipFillProperties getBlipFillArray(int i);

    int sizeOfBlipFillArray();

    void setBlipFillArray(CTBlipFillProperties[] cTBlipFillPropertiesArr);

    void setBlipFillArray(int i, CTBlipFillProperties cTBlipFillProperties);

    CTBlipFillProperties insertNewBlipFill(int i);

    CTBlipFillProperties addNewBlipFill();

    void removeBlipFill(int i);

    List<CTPatternFillProperties> getPattFillList();

    CTPatternFillProperties[] getPattFillArray();

    CTPatternFillProperties getPattFillArray(int i);

    int sizeOfPattFillArray();

    void setPattFillArray(CTPatternFillProperties[] cTPatternFillPropertiesArr);

    void setPattFillArray(int i, CTPatternFillProperties cTPatternFillProperties);

    CTPatternFillProperties insertNewPattFill(int i);

    CTPatternFillProperties addNewPattFill();

    void removePattFill(int i);

    List<CTGroupFillProperties> getGrpFillList();

    CTGroupFillProperties[] getGrpFillArray();

    CTGroupFillProperties getGrpFillArray(int i);

    int sizeOfGrpFillArray();

    void setGrpFillArray(CTGroupFillProperties[] cTGroupFillPropertiesArr);

    void setGrpFillArray(int i, CTGroupFillProperties cTGroupFillProperties);

    CTGroupFillProperties insertNewGrpFill(int i);

    CTGroupFillProperties addNewGrpFill();

    void removeGrpFill(int i);
}
