package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlByte;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlLong;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlShort;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedByte;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.XmlUnsignedLong;
import org.apache.xmlbeans.XmlUnsignedShort;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/docPropsVTypes/CTVector.class */
public interface CTVector extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTVector.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctvectorc3e2type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/docPropsVTypes/CTVector$Factory.class */
    public static final class Factory {
        public static CTVector newInstance() {
            return (CTVector) POIXMLTypeLoader.newInstance(CTVector.type, null);
        }

        public static CTVector newInstance(XmlOptions xmlOptions) {
            return (CTVector) POIXMLTypeLoader.newInstance(CTVector.type, xmlOptions);
        }

        public static CTVector parse(String str) throws XmlException {
            return (CTVector) POIXMLTypeLoader.parse(str, CTVector.type, (XmlOptions) null);
        }

        public static CTVector parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTVector) POIXMLTypeLoader.parse(str, CTVector.type, xmlOptions);
        }

        public static CTVector parse(File file) throws XmlException, IOException {
            return (CTVector) POIXMLTypeLoader.parse(file, CTVector.type, (XmlOptions) null);
        }

        public static CTVector parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVector) POIXMLTypeLoader.parse(file, CTVector.type, xmlOptions);
        }

        public static CTVector parse(URL url) throws XmlException, IOException {
            return (CTVector) POIXMLTypeLoader.parse(url, CTVector.type, (XmlOptions) null);
        }

        public static CTVector parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVector) POIXMLTypeLoader.parse(url, CTVector.type, xmlOptions);
        }

        public static CTVector parse(InputStream inputStream) throws XmlException, IOException {
            return (CTVector) POIXMLTypeLoader.parse(inputStream, CTVector.type, (XmlOptions) null);
        }

        public static CTVector parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVector) POIXMLTypeLoader.parse(inputStream, CTVector.type, xmlOptions);
        }

        public static CTVector parse(Reader reader) throws XmlException, IOException {
            return (CTVector) POIXMLTypeLoader.parse(reader, CTVector.type, (XmlOptions) null);
        }

        public static CTVector parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVector) POIXMLTypeLoader.parse(reader, CTVector.type, xmlOptions);
        }

        public static CTVector parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTVector) POIXMLTypeLoader.parse(xMLStreamReader, CTVector.type, (XmlOptions) null);
        }

        public static CTVector parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTVector) POIXMLTypeLoader.parse(xMLStreamReader, CTVector.type, xmlOptions);
        }

        public static CTVector parse(Node node) throws XmlException {
            return (CTVector) POIXMLTypeLoader.parse(node, CTVector.type, (XmlOptions) null);
        }

        public static CTVector parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTVector) POIXMLTypeLoader.parse(node, CTVector.type, xmlOptions);
        }

        public static CTVector parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTVector) POIXMLTypeLoader.parse(xMLInputStream, CTVector.type, (XmlOptions) null);
        }

        public static CTVector parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTVector) POIXMLTypeLoader.parse(xMLInputStream, CTVector.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTVector.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTVector.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTVariant> getVariantList();

    CTVariant[] getVariantArray();

    CTVariant getVariantArray(int i);

    int sizeOfVariantArray();

    void setVariantArray(CTVariant[] cTVariantArr);

    void setVariantArray(int i, CTVariant cTVariant);

    CTVariant insertNewVariant(int i);

    CTVariant addNewVariant();

    void removeVariant(int i);

    List<Byte> getI1List();

    byte[] getI1Array();

    byte getI1Array(int i);

    List<XmlByte> xgetI1List();

    XmlByte[] xgetI1Array();

    XmlByte xgetI1Array(int i);

    int sizeOfI1Array();

    void setI1Array(byte[] bArr);

    void setI1Array(int i, byte b);

    void xsetI1Array(XmlByte[] xmlByteArr);

    void xsetI1Array(int i, XmlByte xmlByte);

    void insertI1(int i, byte b);

    void addI1(byte b);

    XmlByte insertNewI1(int i);

    XmlByte addNewI1();

    void removeI1(int i);

    List<Short> getI2List();

    short[] getI2Array();

    short getI2Array(int i);

    List<XmlShort> xgetI2List();

    XmlShort[] xgetI2Array();

    XmlShort xgetI2Array(int i);

    int sizeOfI2Array();

    void setI2Array(short[] sArr);

    void setI2Array(int i, short s);

    void xsetI2Array(XmlShort[] xmlShortArr);

    void xsetI2Array(int i, XmlShort xmlShort);

    void insertI2(int i, short s);

    void addI2(short s);

    XmlShort insertNewI2(int i);

    XmlShort addNewI2();

    void removeI2(int i);

    List<Integer> getI4List();

    int[] getI4Array();

    int getI4Array(int i);

    List<XmlInt> xgetI4List();

    XmlInt[] xgetI4Array();

    XmlInt xgetI4Array(int i);

    int sizeOfI4Array();

    void setI4Array(int[] iArr);

    void setI4Array(int i, int i2);

    void xsetI4Array(XmlInt[] xmlIntArr);

    void xsetI4Array(int i, XmlInt xmlInt);

    void insertI4(int i, int i2);

    void addI4(int i);

    XmlInt insertNewI4(int i);

    XmlInt addNewI4();

    void removeI4(int i);

    List<Long> getI8List();

    long[] getI8Array();

    long getI8Array(int i);

    List<XmlLong> xgetI8List();

    XmlLong[] xgetI8Array();

    XmlLong xgetI8Array(int i);

    int sizeOfI8Array();

    void setI8Array(long[] jArr);

    void setI8Array(int i, long j);

    void xsetI8Array(XmlLong[] xmlLongArr);

    void xsetI8Array(int i, XmlLong xmlLong);

    void insertI8(int i, long j);

    void addI8(long j);

    XmlLong insertNewI8(int i);

    XmlLong addNewI8();

    void removeI8(int i);

    List<Short> getUi1List();

    short[] getUi1Array();

    short getUi1Array(int i);

    List<XmlUnsignedByte> xgetUi1List();

    XmlUnsignedByte[] xgetUi1Array();

    XmlUnsignedByte xgetUi1Array(int i);

    int sizeOfUi1Array();

    void setUi1Array(short[] sArr);

    void setUi1Array(int i, short s);

    void xsetUi1Array(XmlUnsignedByte[] xmlUnsignedByteArr);

    void xsetUi1Array(int i, XmlUnsignedByte xmlUnsignedByte);

    void insertUi1(int i, short s);

    void addUi1(short s);

    XmlUnsignedByte insertNewUi1(int i);

    XmlUnsignedByte addNewUi1();

    void removeUi1(int i);

    List<Integer> getUi2List();

    int[] getUi2Array();

    int getUi2Array(int i);

    List<XmlUnsignedShort> xgetUi2List();

    XmlUnsignedShort[] xgetUi2Array();

    XmlUnsignedShort xgetUi2Array(int i);

    int sizeOfUi2Array();

    void setUi2Array(int[] iArr);

    void setUi2Array(int i, int i2);

    void xsetUi2Array(XmlUnsignedShort[] xmlUnsignedShortArr);

    void xsetUi2Array(int i, XmlUnsignedShort xmlUnsignedShort);

    void insertUi2(int i, int i2);

    void addUi2(int i);

    XmlUnsignedShort insertNewUi2(int i);

    XmlUnsignedShort addNewUi2();

    void removeUi2(int i);

    List<Long> getUi4List();

    long[] getUi4Array();

    long getUi4Array(int i);

    List<XmlUnsignedInt> xgetUi4List();

    XmlUnsignedInt[] xgetUi4Array();

    XmlUnsignedInt xgetUi4Array(int i);

    int sizeOfUi4Array();

    void setUi4Array(long[] jArr);

    void setUi4Array(int i, long j);

    void xsetUi4Array(XmlUnsignedInt[] xmlUnsignedIntArr);

    void xsetUi4Array(int i, XmlUnsignedInt xmlUnsignedInt);

    void insertUi4(int i, long j);

    void addUi4(long j);

    XmlUnsignedInt insertNewUi4(int i);

    XmlUnsignedInt addNewUi4();

    void removeUi4(int i);

    List<BigInteger> getUi8List();

    BigInteger[] getUi8Array();

    BigInteger getUi8Array(int i);

    List<XmlUnsignedLong> xgetUi8List();

    XmlUnsignedLong[] xgetUi8Array();

    XmlUnsignedLong xgetUi8Array(int i);

    int sizeOfUi8Array();

    void setUi8Array(BigInteger[] bigIntegerArr);

    void setUi8Array(int i, BigInteger bigInteger);

    void xsetUi8Array(XmlUnsignedLong[] xmlUnsignedLongArr);

    void xsetUi8Array(int i, XmlUnsignedLong xmlUnsignedLong);

    void insertUi8(int i, BigInteger bigInteger);

    void addUi8(BigInteger bigInteger);

    XmlUnsignedLong insertNewUi8(int i);

    XmlUnsignedLong addNewUi8();

    void removeUi8(int i);

    List<Float> getR4List();

    float[] getR4Array();

    float getR4Array(int i);

    List<XmlFloat> xgetR4List();

    XmlFloat[] xgetR4Array();

    XmlFloat xgetR4Array(int i);

    int sizeOfR4Array();

    void setR4Array(float[] fArr);

    void setR4Array(int i, float f);

    void xsetR4Array(XmlFloat[] xmlFloatArr);

    void xsetR4Array(int i, XmlFloat xmlFloat);

    void insertR4(int i, float f);

    void addR4(float f);

    XmlFloat insertNewR4(int i);

    XmlFloat addNewR4();

    void removeR4(int i);

    List<Double> getR8List();

    double[] getR8Array();

    double getR8Array(int i);

    List<XmlDouble> xgetR8List();

    XmlDouble[] xgetR8Array();

    XmlDouble xgetR8Array(int i);

    int sizeOfR8Array();

    void setR8Array(double[] dArr);

    void setR8Array(int i, double d);

    void xsetR8Array(XmlDouble[] xmlDoubleArr);

    void xsetR8Array(int i, XmlDouble xmlDouble);

    void insertR8(int i, double d);

    void addR8(double d);

    XmlDouble insertNewR8(int i);

    XmlDouble addNewR8();

    void removeR8(int i);

    List<String> getLpstrList();

    String[] getLpstrArray();

    String getLpstrArray(int i);

    List<XmlString> xgetLpstrList();

    XmlString[] xgetLpstrArray();

    XmlString xgetLpstrArray(int i);

    int sizeOfLpstrArray();

    void setLpstrArray(String[] strArr);

    void setLpstrArray(int i, String str);

    void xsetLpstrArray(XmlString[] xmlStringArr);

    void xsetLpstrArray(int i, XmlString xmlString);

    void insertLpstr(int i, String str);

    void addLpstr(String str);

    XmlString insertNewLpstr(int i);

    XmlString addNewLpstr();

    void removeLpstr(int i);

    List<String> getLpwstrList();

    String[] getLpwstrArray();

    String getLpwstrArray(int i);

    List<XmlString> xgetLpwstrList();

    XmlString[] xgetLpwstrArray();

    XmlString xgetLpwstrArray(int i);

    int sizeOfLpwstrArray();

    void setLpwstrArray(String[] strArr);

    void setLpwstrArray(int i, String str);

    void xsetLpwstrArray(XmlString[] xmlStringArr);

    void xsetLpwstrArray(int i, XmlString xmlString);

    void insertLpwstr(int i, String str);

    void addLpwstr(String str);

    XmlString insertNewLpwstr(int i);

    XmlString addNewLpwstr();

    void removeLpwstr(int i);

    List<String> getBstrList();

    String[] getBstrArray();

    String getBstrArray(int i);

    List<XmlString> xgetBstrList();

    XmlString[] xgetBstrArray();

    XmlString xgetBstrArray(int i);

    int sizeOfBstrArray();

    void setBstrArray(String[] strArr);

    void setBstrArray(int i, String str);

    void xsetBstrArray(XmlString[] xmlStringArr);

    void xsetBstrArray(int i, XmlString xmlString);

    void insertBstr(int i, String str);

    void addBstr(String str);

    XmlString insertNewBstr(int i);

    XmlString addNewBstr();

    void removeBstr(int i);

    List<Calendar> getDateList();

    Calendar[] getDateArray();

    Calendar getDateArray(int i);

    List<XmlDateTime> xgetDateList();

    XmlDateTime[] xgetDateArray();

    XmlDateTime xgetDateArray(int i);

    int sizeOfDateArray();

    void setDateArray(Calendar[] calendarArr);

    void setDateArray(int i, Calendar calendar);

    void xsetDateArray(XmlDateTime[] xmlDateTimeArr);

    void xsetDateArray(int i, XmlDateTime xmlDateTime);

    void insertDate(int i, Calendar calendar);

    void addDate(Calendar calendar);

    XmlDateTime insertNewDate(int i);

    XmlDateTime addNewDate();

    void removeDate(int i);

    List<Calendar> getFiletimeList();

    Calendar[] getFiletimeArray();

    Calendar getFiletimeArray(int i);

    List<XmlDateTime> xgetFiletimeList();

    XmlDateTime[] xgetFiletimeArray();

    XmlDateTime xgetFiletimeArray(int i);

    int sizeOfFiletimeArray();

    void setFiletimeArray(Calendar[] calendarArr);

    void setFiletimeArray(int i, Calendar calendar);

    void xsetFiletimeArray(XmlDateTime[] xmlDateTimeArr);

    void xsetFiletimeArray(int i, XmlDateTime xmlDateTime);

    void insertFiletime(int i, Calendar calendar);

    void addFiletime(Calendar calendar);

    XmlDateTime insertNewFiletime(int i);

    XmlDateTime addNewFiletime();

    void removeFiletime(int i);

    List<Boolean> getBoolList();

    boolean[] getBoolArray();

    boolean getBoolArray(int i);

    List<XmlBoolean> xgetBoolList();

    XmlBoolean[] xgetBoolArray();

    XmlBoolean xgetBoolArray(int i);

    int sizeOfBoolArray();

    void setBoolArray(boolean[] zArr);

    void setBoolArray(int i, boolean z);

    void xsetBoolArray(XmlBoolean[] xmlBooleanArr);

    void xsetBoolArray(int i, XmlBoolean xmlBoolean);

    void insertBool(int i, boolean z);

    void addBool(boolean z);

    XmlBoolean insertNewBool(int i);

    XmlBoolean addNewBool();

    void removeBool(int i);

    List<String> getCyList();

    String[] getCyArray();

    String getCyArray(int i);

    List<STCy> xgetCyList();

    STCy[] xgetCyArray();

    STCy xgetCyArray(int i);

    int sizeOfCyArray();

    void setCyArray(String[] strArr);

    void setCyArray(int i, String str);

    void xsetCyArray(STCy[] sTCyArr);

    void xsetCyArray(int i, STCy sTCy);

    void insertCy(int i, String str);

    void addCy(String str);

    STCy insertNewCy(int i);

    STCy addNewCy();

    void removeCy(int i);

    List<String> getErrorList();

    String[] getErrorArray();

    String getErrorArray(int i);

    List<STError> xgetErrorList();

    STError[] xgetErrorArray();

    STError xgetErrorArray(int i);

    int sizeOfErrorArray();

    void setErrorArray(String[] strArr);

    void setErrorArray(int i, String str);

    void xsetErrorArray(STError[] sTErrorArr);

    void xsetErrorArray(int i, STError sTError);

    void insertError(int i, String str);

    void addError(String str);

    STError insertNewError(int i);

    STError addNewError();

    void removeError(int i);

    List<String> getClsidList();

    String[] getClsidArray();

    String getClsidArray(int i);

    List<STClsid> xgetClsidList();

    STClsid[] xgetClsidArray();

    STClsid xgetClsidArray(int i);

    int sizeOfClsidArray();

    void setClsidArray(String[] strArr);

    void setClsidArray(int i, String str);

    void xsetClsidArray(STClsid[] sTClsidArr);

    void xsetClsidArray(int i, STClsid sTClsid);

    void insertClsid(int i, String str);

    void addClsid(String str);

    STClsid insertNewClsid(int i);

    STClsid addNewClsid();

    void removeClsid(int i);

    List<CTCf> getCfList();

    CTCf[] getCfArray();

    CTCf getCfArray(int i);

    int sizeOfCfArray();

    void setCfArray(CTCf[] cTCfArr);

    void setCfArray(int i, CTCf cTCf);

    CTCf insertNewCf(int i);

    CTCf addNewCf();

    void removeCf(int i);

    STVectorBaseType$Enum getBaseType();

    STVectorBaseType xgetBaseType();

    void setBaseType(STVectorBaseType$Enum sTVectorBaseType$Enum);

    void xsetBaseType(STVectorBaseType sTVectorBaseType);

    long getSize();

    XmlUnsignedInt xgetSize();

    void setSize(long j);

    void xsetSize(XmlUnsignedInt xmlUnsignedInt);
}
