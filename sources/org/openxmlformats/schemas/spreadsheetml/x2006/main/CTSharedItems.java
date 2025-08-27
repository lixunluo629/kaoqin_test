package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSharedItems.class */
public interface CTSharedItems extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSharedItems.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctshareditems677atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSharedItems$Factory.class */
    public static final class Factory {
        public static CTSharedItems newInstance() {
            return (CTSharedItems) POIXMLTypeLoader.newInstance(CTSharedItems.type, null);
        }

        public static CTSharedItems newInstance(XmlOptions xmlOptions) {
            return (CTSharedItems) POIXMLTypeLoader.newInstance(CTSharedItems.type, xmlOptions);
        }

        public static CTSharedItems parse(String str) throws XmlException {
            return (CTSharedItems) POIXMLTypeLoader.parse(str, CTSharedItems.type, (XmlOptions) null);
        }

        public static CTSharedItems parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSharedItems) POIXMLTypeLoader.parse(str, CTSharedItems.type, xmlOptions);
        }

        public static CTSharedItems parse(File file) throws XmlException, IOException {
            return (CTSharedItems) POIXMLTypeLoader.parse(file, CTSharedItems.type, (XmlOptions) null);
        }

        public static CTSharedItems parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSharedItems) POIXMLTypeLoader.parse(file, CTSharedItems.type, xmlOptions);
        }

        public static CTSharedItems parse(URL url) throws XmlException, IOException {
            return (CTSharedItems) POIXMLTypeLoader.parse(url, CTSharedItems.type, (XmlOptions) null);
        }

        public static CTSharedItems parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSharedItems) POIXMLTypeLoader.parse(url, CTSharedItems.type, xmlOptions);
        }

        public static CTSharedItems parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSharedItems) POIXMLTypeLoader.parse(inputStream, CTSharedItems.type, (XmlOptions) null);
        }

        public static CTSharedItems parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSharedItems) POIXMLTypeLoader.parse(inputStream, CTSharedItems.type, xmlOptions);
        }

        public static CTSharedItems parse(Reader reader) throws XmlException, IOException {
            return (CTSharedItems) POIXMLTypeLoader.parse(reader, CTSharedItems.type, (XmlOptions) null);
        }

        public static CTSharedItems parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSharedItems) POIXMLTypeLoader.parse(reader, CTSharedItems.type, xmlOptions);
        }

        public static CTSharedItems parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSharedItems) POIXMLTypeLoader.parse(xMLStreamReader, CTSharedItems.type, (XmlOptions) null);
        }

        public static CTSharedItems parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSharedItems) POIXMLTypeLoader.parse(xMLStreamReader, CTSharedItems.type, xmlOptions);
        }

        public static CTSharedItems parse(Node node) throws XmlException {
            return (CTSharedItems) POIXMLTypeLoader.parse(node, CTSharedItems.type, (XmlOptions) null);
        }

        public static CTSharedItems parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSharedItems) POIXMLTypeLoader.parse(node, CTSharedItems.type, xmlOptions);
        }

        public static CTSharedItems parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSharedItems) POIXMLTypeLoader.parse(xMLInputStream, CTSharedItems.type, (XmlOptions) null);
        }

        public static CTSharedItems parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSharedItems) POIXMLTypeLoader.parse(xMLInputStream, CTSharedItems.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSharedItems.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSharedItems.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTMissing> getMList();

    CTMissing[] getMArray();

    CTMissing getMArray(int i);

    int sizeOfMArray();

    void setMArray(CTMissing[] cTMissingArr);

    void setMArray(int i, CTMissing cTMissing);

    CTMissing insertNewM(int i);

    CTMissing addNewM();

    void removeM(int i);

    List<CTNumber> getNList();

    CTNumber[] getNArray();

    CTNumber getNArray(int i);

    int sizeOfNArray();

    void setNArray(CTNumber[] cTNumberArr);

    void setNArray(int i, CTNumber cTNumber);

    CTNumber insertNewN(int i);

    CTNumber addNewN();

    void removeN(int i);

    List<CTBoolean> getBList();

    CTBoolean[] getBArray();

    CTBoolean getBArray(int i);

    int sizeOfBArray();

    void setBArray(CTBoolean[] cTBooleanArr);

    void setBArray(int i, CTBoolean cTBoolean);

    CTBoolean insertNewB(int i);

    CTBoolean addNewB();

    void removeB(int i);

    List<CTError> getEList();

    CTError[] getEArray();

    CTError getEArray(int i);

    int sizeOfEArray();

    void setEArray(CTError[] cTErrorArr);

    void setEArray(int i, CTError cTError);

    CTError insertNewE(int i);

    CTError addNewE();

    void removeE(int i);

    List<CTString> getSList();

    CTString[] getSArray();

    CTString getSArray(int i);

    int sizeOfSArray();

    void setSArray(CTString[] cTStringArr);

    void setSArray(int i, CTString cTString);

    CTString insertNewS(int i);

    CTString addNewS();

    void removeS(int i);

    List<CTDateTime> getDList();

    CTDateTime[] getDArray();

    CTDateTime getDArray(int i);

    int sizeOfDArray();

    void setDArray(CTDateTime[] cTDateTimeArr);

    void setDArray(int i, CTDateTime cTDateTime);

    CTDateTime insertNewD(int i);

    CTDateTime addNewD();

    void removeD(int i);

    boolean getContainsSemiMixedTypes();

    XmlBoolean xgetContainsSemiMixedTypes();

    boolean isSetContainsSemiMixedTypes();

    void setContainsSemiMixedTypes(boolean z);

    void xsetContainsSemiMixedTypes(XmlBoolean xmlBoolean);

    void unsetContainsSemiMixedTypes();

    boolean getContainsNonDate();

    XmlBoolean xgetContainsNonDate();

    boolean isSetContainsNonDate();

    void setContainsNonDate(boolean z);

    void xsetContainsNonDate(XmlBoolean xmlBoolean);

    void unsetContainsNonDate();

    boolean getContainsDate();

    XmlBoolean xgetContainsDate();

    boolean isSetContainsDate();

    void setContainsDate(boolean z);

    void xsetContainsDate(XmlBoolean xmlBoolean);

    void unsetContainsDate();

    boolean getContainsString();

    XmlBoolean xgetContainsString();

    boolean isSetContainsString();

    void setContainsString(boolean z);

    void xsetContainsString(XmlBoolean xmlBoolean);

    void unsetContainsString();

    boolean getContainsBlank();

    XmlBoolean xgetContainsBlank();

    boolean isSetContainsBlank();

    void setContainsBlank(boolean z);

    void xsetContainsBlank(XmlBoolean xmlBoolean);

    void unsetContainsBlank();

    boolean getContainsMixedTypes();

    XmlBoolean xgetContainsMixedTypes();

    boolean isSetContainsMixedTypes();

    void setContainsMixedTypes(boolean z);

    void xsetContainsMixedTypes(XmlBoolean xmlBoolean);

    void unsetContainsMixedTypes();

    boolean getContainsNumber();

    XmlBoolean xgetContainsNumber();

    boolean isSetContainsNumber();

    void setContainsNumber(boolean z);

    void xsetContainsNumber(XmlBoolean xmlBoolean);

    void unsetContainsNumber();

    boolean getContainsInteger();

    XmlBoolean xgetContainsInteger();

    boolean isSetContainsInteger();

    void setContainsInteger(boolean z);

    void xsetContainsInteger(XmlBoolean xmlBoolean);

    void unsetContainsInteger();

    double getMinValue();

    XmlDouble xgetMinValue();

    boolean isSetMinValue();

    void setMinValue(double d);

    void xsetMinValue(XmlDouble xmlDouble);

    void unsetMinValue();

    double getMaxValue();

    XmlDouble xgetMaxValue();

    boolean isSetMaxValue();

    void setMaxValue(double d);

    void xsetMaxValue(XmlDouble xmlDouble);

    void unsetMaxValue();

    Calendar getMinDate();

    XmlDateTime xgetMinDate();

    boolean isSetMinDate();

    void setMinDate(Calendar calendar);

    void xsetMinDate(XmlDateTime xmlDateTime);

    void unsetMinDate();

    Calendar getMaxDate();

    XmlDateTime xgetMaxDate();

    boolean isSetMaxDate();

    void setMaxDate(Calendar calendar);

    void xsetMaxDate(XmlDateTime xmlDateTime);

    void unsetMaxDate();

    long getCount();

    XmlUnsignedInt xgetCount();

    boolean isSetCount();

    void setCount(long j);

    void xsetCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetCount();

    boolean getLongText();

    XmlBoolean xgetLongText();

    boolean isSetLongText();

    void setLongText(boolean z);

    void xsetLongText(XmlBoolean xmlBoolean);

    void unsetLongText();
}
