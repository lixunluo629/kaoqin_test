package org.apache.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/FilterXmlObject.class */
public abstract class FilterXmlObject implements XmlObject, SimpleValue, DelegateXmlObject {
    @Override // org.apache.xmlbeans.XmlObject
    public SchemaType schemaType() {
        return underlyingXmlObject().schemaType();
    }

    @Override // org.apache.xmlbeans.XmlObject
    public boolean validate() {
        return underlyingXmlObject().validate();
    }

    @Override // org.apache.xmlbeans.XmlObject
    public boolean validate(XmlOptions options) {
        return underlyingXmlObject().validate(options);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] selectPath(String path) {
        return underlyingXmlObject().selectPath(path);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] selectPath(String path, XmlOptions options) {
        return underlyingXmlObject().selectPath(path, options);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] execQuery(String query) {
        return underlyingXmlObject().execQuery(query);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] execQuery(String query, XmlOptions options) {
        return underlyingXmlObject().execQuery(query, options);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject changeType(SchemaType newType) {
        return underlyingXmlObject().changeType(newType);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public boolean isNil() {
        return underlyingXmlObject().isNil();
    }

    @Override // org.apache.xmlbeans.XmlObject
    public void setNil() {
        underlyingXmlObject().setNil();
    }

    @Override // org.apache.xmlbeans.XmlObject
    public boolean isImmutable() {
        return underlyingXmlObject().isImmutable();
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject set(XmlObject srcObj) {
        return underlyingXmlObject().set(srcObj);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject copy() {
        return underlyingXmlObject().copy();
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject copy(XmlOptions options) {
        return underlyingXmlObject().copy(options);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public boolean valueEquals(XmlObject obj) {
        return underlyingXmlObject().valueEquals(obj);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public int valueHashCode() {
        return underlyingXmlObject().valueHashCode();
    }

    @Override // org.apache.xmlbeans.XmlObject
    public int compareTo(Object obj) {
        return underlyingXmlObject().compareTo(obj);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public int compareValue(XmlObject obj) {
        return underlyingXmlObject().compareValue(obj);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Object monitor() {
        return underlyingXmlObject().monitor();
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XmlDocumentProperties documentProperties() {
        return underlyingXmlObject().documentProperties();
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XmlCursor newCursor() {
        return underlyingXmlObject().newCursor();
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XMLInputStream newXMLInputStream() {
        return underlyingXmlObject().newXMLInputStream();
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XMLStreamReader newXMLStreamReader() {
        return underlyingXmlObject().newXMLStreamReader();
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public String xmlText() {
        return underlyingXmlObject().xmlText();
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public InputStream newInputStream() {
        return underlyingXmlObject().newInputStream();
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Reader newReader() {
        return underlyingXmlObject().newReader();
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Node newDomNode() {
        return underlyingXmlObject().newDomNode();
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Node getDomNode() {
        return underlyingXmlObject().getDomNode();
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(ContentHandler ch2, LexicalHandler lh) throws SAXException {
        underlyingXmlObject().save(ch2, lh);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(File file) throws IOException {
        underlyingXmlObject().save(file);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(OutputStream os) throws IOException {
        underlyingXmlObject().save(os);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(Writer w) throws IOException {
        underlyingXmlObject().save(w);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XMLInputStream newXMLInputStream(XmlOptions options) {
        return underlyingXmlObject().newXMLInputStream(options);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public XMLStreamReader newXMLStreamReader(XmlOptions options) {
        return underlyingXmlObject().newXMLStreamReader(options);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public String xmlText(XmlOptions options) {
        return underlyingXmlObject().xmlText(options);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public InputStream newInputStream(XmlOptions options) {
        return underlyingXmlObject().newInputStream(options);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Reader newReader(XmlOptions options) {
        return underlyingXmlObject().newReader(options);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public Node newDomNode(XmlOptions options) {
        return underlyingXmlObject().newDomNode(options);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(ContentHandler ch2, LexicalHandler lh, XmlOptions options) throws SAXException {
        underlyingXmlObject().save(ch2, lh, options);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(File file, XmlOptions options) throws IOException {
        underlyingXmlObject().save(file, options);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(OutputStream os, XmlOptions options) throws IOException {
        underlyingXmlObject().save(os, options);
    }

    @Override // org.apache.xmlbeans.XmlTokenSource
    public void save(Writer w, XmlOptions options) throws IOException {
        underlyingXmlObject().save(w, options);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public SchemaType instanceType() {
        return ((SimpleValue) underlyingXmlObject()).instanceType();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public String stringValue() {
        return ((SimpleValue) underlyingXmlObject()).stringValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public boolean booleanValue() {
        return ((SimpleValue) underlyingXmlObject()).booleanValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public byte byteValue() {
        return ((SimpleValue) underlyingXmlObject()).byteValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public short shortValue() {
        return ((SimpleValue) underlyingXmlObject()).shortValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public int intValue() {
        return ((SimpleValue) underlyingXmlObject()).intValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public long longValue() {
        return ((SimpleValue) underlyingXmlObject()).longValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public BigInteger bigIntegerValue() {
        return ((SimpleValue) underlyingXmlObject()).bigIntegerValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public BigDecimal bigDecimalValue() {
        return ((SimpleValue) underlyingXmlObject()).bigDecimalValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public float floatValue() {
        return ((SimpleValue) underlyingXmlObject()).floatValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public double doubleValue() {
        return ((SimpleValue) underlyingXmlObject()).doubleValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public byte[] byteArrayValue() {
        return ((SimpleValue) underlyingXmlObject()).byteArrayValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public StringEnumAbstractBase enumValue() {
        return ((SimpleValue) underlyingXmlObject()).enumValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public Calendar calendarValue() {
        return ((SimpleValue) underlyingXmlObject()).calendarValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public Date dateValue() {
        return ((SimpleValue) underlyingXmlObject()).dateValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public GDate gDateValue() {
        return ((SimpleValue) underlyingXmlObject()).gDateValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public GDuration gDurationValue() {
        return ((SimpleValue) underlyingXmlObject()).gDurationValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public QName qNameValue() {
        return ((SimpleValue) underlyingXmlObject()).qNameValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public List listValue() {
        return ((SimpleValue) underlyingXmlObject()).listValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public List xlistValue() {
        return ((SimpleValue) underlyingXmlObject()).xlistValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public Object objectValue() {
        return ((SimpleValue) underlyingXmlObject()).objectValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(String obj) {
        ((SimpleValue) underlyingXmlObject()).set(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(boolean v) {
        ((SimpleValue) underlyingXmlObject()).set(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(byte v) {
        ((SimpleValue) underlyingXmlObject()).set(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(short v) {
        ((SimpleValue) underlyingXmlObject()).set(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(int v) {
        ((SimpleValue) underlyingXmlObject()).set(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(long v) {
        ((SimpleValue) underlyingXmlObject()).set(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(BigInteger obj) {
        ((SimpleValue) underlyingXmlObject()).set(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(BigDecimal obj) {
        ((SimpleValue) underlyingXmlObject()).set(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(float v) {
        ((SimpleValue) underlyingXmlObject()).set(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(double v) {
        ((SimpleValue) underlyingXmlObject()).set(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(byte[] obj) {
        ((SimpleValue) underlyingXmlObject()).set(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(StringEnumAbstractBase obj) {
        ((SimpleValue) underlyingXmlObject()).set(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(Calendar obj) {
        ((SimpleValue) underlyingXmlObject()).set(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(Date obj) {
        ((SimpleValue) underlyingXmlObject()).set(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(GDateSpecification obj) {
        ((SimpleValue) underlyingXmlObject()).set(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(GDurationSpecification obj) {
        ((SimpleValue) underlyingXmlObject()).set(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(QName obj) {
        ((SimpleValue) underlyingXmlObject()).set(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void set(List obj) {
        ((SimpleValue) underlyingXmlObject()).set(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public String getStringValue() {
        return ((SimpleValue) underlyingXmlObject()).getStringValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public boolean getBooleanValue() {
        return ((SimpleValue) underlyingXmlObject()).getBooleanValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public byte getByteValue() {
        return ((SimpleValue) underlyingXmlObject()).getByteValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public short getShortValue() {
        return ((SimpleValue) underlyingXmlObject()).getShortValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public int getIntValue() {
        return ((SimpleValue) underlyingXmlObject()).getIntValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public long getLongValue() {
        return ((SimpleValue) underlyingXmlObject()).getLongValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public BigInteger getBigIntegerValue() {
        return ((SimpleValue) underlyingXmlObject()).getBigIntegerValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public BigDecimal getBigDecimalValue() {
        return ((SimpleValue) underlyingXmlObject()).getBigDecimalValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public float getFloatValue() {
        return ((SimpleValue) underlyingXmlObject()).getFloatValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public double getDoubleValue() {
        return ((SimpleValue) underlyingXmlObject()).getDoubleValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public byte[] getByteArrayValue() {
        return ((SimpleValue) underlyingXmlObject()).getByteArrayValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public StringEnumAbstractBase getEnumValue() {
        return ((SimpleValue) underlyingXmlObject()).getEnumValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public Calendar getCalendarValue() {
        return ((SimpleValue) underlyingXmlObject()).getCalendarValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public Date getDateValue() {
        return ((SimpleValue) underlyingXmlObject()).getDateValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public GDate getGDateValue() {
        return ((SimpleValue) underlyingXmlObject()).getGDateValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public GDuration getGDurationValue() {
        return ((SimpleValue) underlyingXmlObject()).getGDurationValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public QName getQNameValue() {
        return ((SimpleValue) underlyingXmlObject()).getQNameValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public List getListValue() {
        return ((SimpleValue) underlyingXmlObject()).getListValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public List xgetListValue() {
        return ((SimpleValue) underlyingXmlObject()).xgetListValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public Object getObjectValue() {
        return ((SimpleValue) underlyingXmlObject()).getObjectValue();
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setStringValue(String obj) {
        ((SimpleValue) underlyingXmlObject()).setStringValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setBooleanValue(boolean v) {
        ((SimpleValue) underlyingXmlObject()).setBooleanValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setByteValue(byte v) {
        ((SimpleValue) underlyingXmlObject()).setByteValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setShortValue(short v) {
        ((SimpleValue) underlyingXmlObject()).setShortValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setIntValue(int v) {
        ((SimpleValue) underlyingXmlObject()).setIntValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setLongValue(long v) {
        ((SimpleValue) underlyingXmlObject()).setLongValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setBigIntegerValue(BigInteger obj) {
        ((SimpleValue) underlyingXmlObject()).setBigIntegerValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setBigDecimalValue(BigDecimal obj) {
        ((SimpleValue) underlyingXmlObject()).setBigDecimalValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setFloatValue(float v) {
        ((SimpleValue) underlyingXmlObject()).setFloatValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setDoubleValue(double v) {
        ((SimpleValue) underlyingXmlObject()).setDoubleValue(v);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setByteArrayValue(byte[] obj) {
        ((SimpleValue) underlyingXmlObject()).setByteArrayValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setEnumValue(StringEnumAbstractBase obj) {
        ((SimpleValue) underlyingXmlObject()).setEnumValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setCalendarValue(Calendar obj) {
        ((SimpleValue) underlyingXmlObject()).setCalendarValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setDateValue(Date obj) {
        ((SimpleValue) underlyingXmlObject()).setDateValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setGDateValue(GDate obj) {
        ((SimpleValue) underlyingXmlObject()).setGDateValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setGDurationValue(GDuration obj) {
        ((SimpleValue) underlyingXmlObject()).setGDurationValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setQNameValue(QName obj) {
        ((SimpleValue) underlyingXmlObject()).setQNameValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setListValue(List obj) {
        ((SimpleValue) underlyingXmlObject()).setListValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void setObjectValue(Object obj) {
        ((SimpleValue) underlyingXmlObject()).setObjectValue(obj);
    }

    @Override // org.apache.xmlbeans.SimpleValue
    public void objectSet(Object obj) {
        ((SimpleValue) underlyingXmlObject()).objectSet(obj);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] selectChildren(QName elementName) {
        return underlyingXmlObject().selectChildren(elementName);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] selectChildren(String elementUri, String elementLocalName) {
        return underlyingXmlObject().selectChildren(elementUri, elementLocalName);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] selectChildren(QNameSet elementNameSet) {
        return underlyingXmlObject().selectChildren(elementNameSet);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject selectAttribute(QName attributeName) {
        return underlyingXmlObject().selectAttribute(attributeName);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject selectAttribute(String attributeUri, String attributeLocalName) {
        return underlyingXmlObject().selectAttribute(attributeUri, attributeLocalName);
    }

    @Override // org.apache.xmlbeans.XmlObject
    public XmlObject[] selectAttributes(QNameSet attributeNameSet) {
        return underlyingXmlObject().selectAttributes(attributeNameSet);
    }
}
