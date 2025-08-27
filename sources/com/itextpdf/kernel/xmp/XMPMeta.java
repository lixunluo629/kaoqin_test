package com.itextpdf.kernel.xmp;

import com.itextpdf.kernel.xmp.options.IteratorOptions;
import com.itextpdf.kernel.xmp.options.ParseOptions;
import com.itextpdf.kernel.xmp.options.PropertyOptions;
import com.itextpdf.kernel.xmp.properties.XMPProperty;
import java.util.Calendar;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/XMPMeta.class */
public interface XMPMeta extends Cloneable {
    XMPProperty getProperty(String str, String str2) throws XMPException;

    XMPProperty getArrayItem(String str, String str2, int i) throws XMPException;

    int countArrayItems(String str, String str2) throws XMPException;

    XMPProperty getStructField(String str, String str2, String str3, String str4) throws XMPException;

    XMPProperty getQualifier(String str, String str2, String str3, String str4) throws XMPException;

    void setProperty(String str, String str2, Object obj, PropertyOptions propertyOptions) throws XMPException;

    void setProperty(String str, String str2, Object obj) throws XMPException;

    void setArrayItem(String str, String str2, int i, String str3, PropertyOptions propertyOptions) throws XMPException;

    void setArrayItem(String str, String str2, int i, String str3) throws XMPException;

    void insertArrayItem(String str, String str2, int i, String str3, PropertyOptions propertyOptions) throws XMPException;

    void insertArrayItem(String str, String str2, int i, String str3) throws XMPException;

    void appendArrayItem(String str, String str2, PropertyOptions propertyOptions, String str3, PropertyOptions propertyOptions2) throws XMPException;

    void appendArrayItem(String str, String str2, String str3) throws XMPException;

    void setStructField(String str, String str2, String str3, String str4, String str5, PropertyOptions propertyOptions) throws XMPException;

    void setStructField(String str, String str2, String str3, String str4, String str5) throws XMPException;

    void setQualifier(String str, String str2, String str3, String str4, String str5, PropertyOptions propertyOptions) throws XMPException;

    void setQualifier(String str, String str2, String str3, String str4, String str5) throws XMPException;

    void deleteProperty(String str, String str2);

    void deleteArrayItem(String str, String str2, int i);

    void deleteStructField(String str, String str2, String str3, String str4);

    void deleteQualifier(String str, String str2, String str3, String str4);

    boolean doesPropertyExist(String str, String str2);

    boolean doesArrayItemExist(String str, String str2, int i);

    boolean doesStructFieldExist(String str, String str2, String str3, String str4);

    boolean doesQualifierExist(String str, String str2, String str3, String str4);

    XMPProperty getLocalizedText(String str, String str2, String str3, String str4) throws XMPException;

    void setLocalizedText(String str, String str2, String str3, String str4, String str5, PropertyOptions propertyOptions) throws XMPException;

    void setLocalizedText(String str, String str2, String str3, String str4, String str5) throws XMPException;

    Boolean getPropertyBoolean(String str, String str2) throws XMPException;

    Integer getPropertyInteger(String str, String str2) throws XMPException;

    Long getPropertyLong(String str, String str2) throws XMPException;

    Double getPropertyDouble(String str, String str2) throws XMPException;

    XMPDateTime getPropertyDate(String str, String str2) throws XMPException;

    Calendar getPropertyCalendar(String str, String str2) throws XMPException;

    byte[] getPropertyBase64(String str, String str2) throws XMPException;

    String getPropertyString(String str, String str2) throws XMPException;

    void setPropertyBoolean(String str, String str2, boolean z, PropertyOptions propertyOptions) throws XMPException;

    void setPropertyBoolean(String str, String str2, boolean z) throws XMPException;

    void setPropertyInteger(String str, String str2, int i, PropertyOptions propertyOptions) throws XMPException;

    void setPropertyInteger(String str, String str2, int i) throws XMPException;

    void setPropertyLong(String str, String str2, long j, PropertyOptions propertyOptions) throws XMPException;

    void setPropertyLong(String str, String str2, long j) throws XMPException;

    void setPropertyDouble(String str, String str2, double d, PropertyOptions propertyOptions) throws XMPException;

    void setPropertyDouble(String str, String str2, double d) throws XMPException;

    void setPropertyDate(String str, String str2, XMPDateTime xMPDateTime, PropertyOptions propertyOptions) throws XMPException;

    void setPropertyDate(String str, String str2, XMPDateTime xMPDateTime) throws XMPException;

    void setPropertyCalendar(String str, String str2, Calendar calendar, PropertyOptions propertyOptions) throws XMPException;

    void setPropertyCalendar(String str, String str2, Calendar calendar) throws XMPException;

    void setPropertyBase64(String str, String str2, byte[] bArr, PropertyOptions propertyOptions) throws XMPException;

    void setPropertyBase64(String str, String str2, byte[] bArr) throws XMPException;

    XMPIterator iterator() throws XMPException;

    XMPIterator iterator(IteratorOptions iteratorOptions) throws XMPException;

    XMPIterator iterator(String str, String str2, IteratorOptions iteratorOptions) throws XMPException;

    String getObjectName();

    void setObjectName(String str);

    String getPacketHeader();

    Object clone();

    void sort();

    void normalize(ParseOptions parseOptions) throws XMPException;

    String dumpObject();
}
