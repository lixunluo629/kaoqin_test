package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Calendar;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBase64Binary;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlByte;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlDecimal;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/docPropsVTypes/CTVariant.class */
public interface CTVariant extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTVariant.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctvariantedcatype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/docPropsVTypes/CTVariant$Factory.class */
    public static final class Factory {
        public static CTVariant newInstance() {
            return (CTVariant) POIXMLTypeLoader.newInstance(CTVariant.type, null);
        }

        public static CTVariant newInstance(XmlOptions xmlOptions) {
            return (CTVariant) POIXMLTypeLoader.newInstance(CTVariant.type, xmlOptions);
        }

        public static CTVariant parse(String str) throws XmlException {
            return (CTVariant) POIXMLTypeLoader.parse(str, CTVariant.type, (XmlOptions) null);
        }

        public static CTVariant parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTVariant) POIXMLTypeLoader.parse(str, CTVariant.type, xmlOptions);
        }

        public static CTVariant parse(File file) throws XmlException, IOException {
            return (CTVariant) POIXMLTypeLoader.parse(file, CTVariant.type, (XmlOptions) null);
        }

        public static CTVariant parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVariant) POIXMLTypeLoader.parse(file, CTVariant.type, xmlOptions);
        }

        public static CTVariant parse(URL url) throws XmlException, IOException {
            return (CTVariant) POIXMLTypeLoader.parse(url, CTVariant.type, (XmlOptions) null);
        }

        public static CTVariant parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVariant) POIXMLTypeLoader.parse(url, CTVariant.type, xmlOptions);
        }

        public static CTVariant parse(InputStream inputStream) throws XmlException, IOException {
            return (CTVariant) POIXMLTypeLoader.parse(inputStream, CTVariant.type, (XmlOptions) null);
        }

        public static CTVariant parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVariant) POIXMLTypeLoader.parse(inputStream, CTVariant.type, xmlOptions);
        }

        public static CTVariant parse(Reader reader) throws XmlException, IOException {
            return (CTVariant) POIXMLTypeLoader.parse(reader, CTVariant.type, (XmlOptions) null);
        }

        public static CTVariant parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTVariant) POIXMLTypeLoader.parse(reader, CTVariant.type, xmlOptions);
        }

        public static CTVariant parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTVariant) POIXMLTypeLoader.parse(xMLStreamReader, CTVariant.type, (XmlOptions) null);
        }

        public static CTVariant parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTVariant) POIXMLTypeLoader.parse(xMLStreamReader, CTVariant.type, xmlOptions);
        }

        public static CTVariant parse(Node node) throws XmlException {
            return (CTVariant) POIXMLTypeLoader.parse(node, CTVariant.type, (XmlOptions) null);
        }

        public static CTVariant parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTVariant) POIXMLTypeLoader.parse(node, CTVariant.type, xmlOptions);
        }

        public static CTVariant parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTVariant) POIXMLTypeLoader.parse(xMLInputStream, CTVariant.type, (XmlOptions) null);
        }

        public static CTVariant parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTVariant) POIXMLTypeLoader.parse(xMLInputStream, CTVariant.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTVariant.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTVariant.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTVariant getVariant();

    boolean isSetVariant();

    void setVariant(CTVariant cTVariant);

    CTVariant addNewVariant();

    void unsetVariant();

    CTVector getVector();

    boolean isSetVector();

    void setVector(CTVector cTVector);

    CTVector addNewVector();

    void unsetVector();

    CTArray getArray();

    boolean isSetArray();

    void setArray(CTArray cTArray);

    CTArray addNewArray();

    void unsetArray();

    byte[] getBlob();

    XmlBase64Binary xgetBlob();

    boolean isSetBlob();

    void setBlob(byte[] bArr);

    void xsetBlob(XmlBase64Binary xmlBase64Binary);

    void unsetBlob();

    byte[] getOblob();

    XmlBase64Binary xgetOblob();

    boolean isSetOblob();

    void setOblob(byte[] bArr);

    void xsetOblob(XmlBase64Binary xmlBase64Binary);

    void unsetOblob();

    CTEmpty getEmpty();

    boolean isSetEmpty();

    void setEmpty(CTEmpty cTEmpty);

    CTEmpty addNewEmpty();

    void unsetEmpty();

    CTNull getNull();

    boolean isSetNull();

    void setNull(CTNull cTNull);

    CTNull addNewNull();

    void unsetNull();

    byte getI1();

    XmlByte xgetI1();

    boolean isSetI1();

    void setI1(byte b);

    void xsetI1(XmlByte xmlByte);

    void unsetI1();

    short getI2();

    XmlShort xgetI2();

    boolean isSetI2();

    void setI2(short s);

    void xsetI2(XmlShort xmlShort);

    void unsetI2();

    int getI4();

    XmlInt xgetI4();

    boolean isSetI4();

    void setI4(int i);

    void xsetI4(XmlInt xmlInt);

    void unsetI4();

    long getI8();

    XmlLong xgetI8();

    boolean isSetI8();

    void setI8(long j);

    void xsetI8(XmlLong xmlLong);

    void unsetI8();

    int getInt();

    XmlInt xgetInt();

    boolean isSetInt();

    void setInt(int i);

    void xsetInt(XmlInt xmlInt);

    void unsetInt();

    short getUi1();

    XmlUnsignedByte xgetUi1();

    boolean isSetUi1();

    void setUi1(short s);

    void xsetUi1(XmlUnsignedByte xmlUnsignedByte);

    void unsetUi1();

    int getUi2();

    XmlUnsignedShort xgetUi2();

    boolean isSetUi2();

    void setUi2(int i);

    void xsetUi2(XmlUnsignedShort xmlUnsignedShort);

    void unsetUi2();

    long getUi4();

    XmlUnsignedInt xgetUi4();

    boolean isSetUi4();

    void setUi4(long j);

    void xsetUi4(XmlUnsignedInt xmlUnsignedInt);

    void unsetUi4();

    BigInteger getUi8();

    XmlUnsignedLong xgetUi8();

    boolean isSetUi8();

    void setUi8(BigInteger bigInteger);

    void xsetUi8(XmlUnsignedLong xmlUnsignedLong);

    void unsetUi8();

    long getUint();

    XmlUnsignedInt xgetUint();

    boolean isSetUint();

    void setUint(long j);

    void xsetUint(XmlUnsignedInt xmlUnsignedInt);

    void unsetUint();

    float getR4();

    XmlFloat xgetR4();

    boolean isSetR4();

    void setR4(float f);

    void xsetR4(XmlFloat xmlFloat);

    void unsetR4();

    double getR8();

    XmlDouble xgetR8();

    boolean isSetR8();

    void setR8(double d);

    void xsetR8(XmlDouble xmlDouble);

    void unsetR8();

    BigDecimal getDecimal();

    XmlDecimal xgetDecimal();

    boolean isSetDecimal();

    void setDecimal(BigDecimal bigDecimal);

    void xsetDecimal(XmlDecimal xmlDecimal);

    void unsetDecimal();

    String getLpstr();

    XmlString xgetLpstr();

    boolean isSetLpstr();

    void setLpstr(String str);

    void xsetLpstr(XmlString xmlString);

    void unsetLpstr();

    String getLpwstr();

    XmlString xgetLpwstr();

    boolean isSetLpwstr();

    void setLpwstr(String str);

    void xsetLpwstr(XmlString xmlString);

    void unsetLpwstr();

    String getBstr();

    XmlString xgetBstr();

    boolean isSetBstr();

    void setBstr(String str);

    void xsetBstr(XmlString xmlString);

    void unsetBstr();

    Calendar getDate();

    XmlDateTime xgetDate();

    boolean isSetDate();

    void setDate(Calendar calendar);

    void xsetDate(XmlDateTime xmlDateTime);

    void unsetDate();

    Calendar getFiletime();

    XmlDateTime xgetFiletime();

    boolean isSetFiletime();

    void setFiletime(Calendar calendar);

    void xsetFiletime(XmlDateTime xmlDateTime);

    void unsetFiletime();

    boolean getBool();

    XmlBoolean xgetBool();

    boolean isSetBool();

    void setBool(boolean z);

    void xsetBool(XmlBoolean xmlBoolean);

    void unsetBool();

    String getCy();

    STCy xgetCy();

    boolean isSetCy();

    void setCy(String str);

    void xsetCy(STCy sTCy);

    void unsetCy();

    String getError();

    STError xgetError();

    boolean isSetError();

    void setError(String str);

    void xsetError(STError sTError);

    void unsetError();

    byte[] getStream();

    XmlBase64Binary xgetStream();

    boolean isSetStream();

    void setStream(byte[] bArr);

    void xsetStream(XmlBase64Binary xmlBase64Binary);

    void unsetStream();

    byte[] getOstream();

    XmlBase64Binary xgetOstream();

    boolean isSetOstream();

    void setOstream(byte[] bArr);

    void xsetOstream(XmlBase64Binary xmlBase64Binary);

    void unsetOstream();

    byte[] getStorage();

    XmlBase64Binary xgetStorage();

    boolean isSetStorage();

    void setStorage(byte[] bArr);

    void xsetStorage(XmlBase64Binary xmlBase64Binary);

    void unsetStorage();

    byte[] getOstorage();

    XmlBase64Binary xgetOstorage();

    boolean isSetOstorage();

    void setOstorage(byte[] bArr);

    void xsetOstorage(XmlBase64Binary xmlBase64Binary);

    void unsetOstorage();

    CTVstream getVstream();

    boolean isSetVstream();

    void setVstream(CTVstream cTVstream);

    CTVstream addNewVstream();

    void unsetVstream();

    String getClsid();

    STClsid xgetClsid();

    boolean isSetClsid();

    void setClsid(String str);

    void xsetClsid(STClsid sTClsid);

    void unsetClsid();

    CTCf getCf();

    boolean isSetCf();

    void setCf(CTCf cTCf);

    CTCf addNewCf();

    void unsetCf();
}
