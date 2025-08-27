package org.apache.xmlbeans;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.namespace.QName;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SimpleValue.class */
public interface SimpleValue extends XmlObject {
    SchemaType instanceType();

    String getStringValue();

    boolean getBooleanValue();

    byte getByteValue();

    short getShortValue();

    int getIntValue();

    long getLongValue();

    BigInteger getBigIntegerValue();

    BigDecimal getBigDecimalValue();

    float getFloatValue();

    double getDoubleValue();

    byte[] getByteArrayValue();

    StringEnumAbstractBase getEnumValue();

    Calendar getCalendarValue();

    Date getDateValue();

    GDate getGDateValue();

    GDuration getGDurationValue();

    QName getQNameValue();

    List getListValue();

    List xgetListValue();

    Object getObjectValue();

    void setStringValue(String str);

    void setBooleanValue(boolean z);

    void setByteValue(byte b);

    void setShortValue(short s);

    void setIntValue(int i);

    void setLongValue(long j);

    void setBigIntegerValue(BigInteger bigInteger);

    void setBigDecimalValue(BigDecimal bigDecimal);

    void setFloatValue(float f);

    void setDoubleValue(double d);

    void setByteArrayValue(byte[] bArr);

    void setEnumValue(StringEnumAbstractBase stringEnumAbstractBase);

    void setCalendarValue(Calendar calendar);

    void setDateValue(Date date);

    void setGDateValue(GDate gDate);

    void setGDurationValue(GDuration gDuration);

    void setQNameValue(QName qName);

    void setListValue(List list);

    void setObjectValue(Object obj);

    String stringValue();

    boolean booleanValue();

    byte byteValue();

    short shortValue();

    int intValue();

    long longValue();

    BigInteger bigIntegerValue();

    BigDecimal bigDecimalValue();

    float floatValue();

    double doubleValue();

    byte[] byteArrayValue();

    StringEnumAbstractBase enumValue();

    Calendar calendarValue();

    Date dateValue();

    GDate gDateValue();

    GDuration gDurationValue();

    QName qNameValue();

    List listValue();

    List xlistValue();

    Object objectValue();

    void set(String str);

    void set(boolean z);

    void set(byte b);

    void set(short s);

    void set(int i);

    void set(long j);

    void set(BigInteger bigInteger);

    void set(BigDecimal bigDecimal);

    void set(float f);

    void set(double d);

    void set(byte[] bArr);

    void set(StringEnumAbstractBase stringEnumAbstractBase);

    void set(Calendar calendar);

    void set(Date date);

    void set(GDateSpecification gDateSpecification);

    void set(GDurationSpecification gDurationSpecification);

    void set(QName qName);

    void set(List list);

    void objectSet(Object obj);
}
